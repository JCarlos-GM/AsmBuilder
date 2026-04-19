package asmbuilder;

// Construye el contenido .asm combinando secciones segun los flags del usuario
public class TemplateBuilder {

    private static final String NL = "\n";
    private static final String T1 = "    ";
    private static final String T2 = "        ";

    // Convierte un entero a literal hex TASM (ej: 10 -> "0ah", 256 -> "100h")
    private static String hex(int value) {
        String h = Integer.toHexString(value) + "h";
        return Character.isLetter(h.charAt(0)) ? "0" + h : h;
    }

    private final Options options;

    public TemplateBuilder(Options options) {
        this.options = options;
    }

    // Genera una linea con indentacion de 4 espacios
    private String l1(String text) { return T1 + text + NL; }

    // Genera una linea con indentacion de 8 espacios (dentro de etiqueta)
    private String l2(String text) { return T2 + text + NL; }

    public String build() {
        return buildHeader() + buildDataSection() + buildCodeSection();
    }

    private String buildHeader() {
        return "; Tamaño del programa" + NL
             + ".model small"          + NL
             + ";"                     + NL
             + ".stack 50h"            + NL + NL;
    }

    private String buildDataSection() {
        String data = ".data" + NL;

        if (!hasDataVars())          data += l1("; Coloca aqui tus variables");
        if (options.isPrint()
         && !options.isIfCond()
         && !options.isSwitchCond()) {
            String msg = options.getCustomMsg() != null ? options.getCustomMsg() : "Hola Mundo";
            data += l1("msg db '" + msg + "$'");
        }
        if (options.isVars())        data += l1("numero  db 05")
                                          + l1("mensaje db 'Texto$'")
                                          + l1("bandera db 00");
        if (options.isIfCond())      data += l1("opc db 01")
                                          + l1("msg db 'Condicion verdadera$'");
        if (options.isSwitchCond())  data += l1("opc  db 01")
                                          + l1("msg1 db 'Opcion uno$'")
                                          + l1("msg2 db 'Opcion dos$'")
                                          + l1("msg3 db 'Opcion no valida$'");
        if (options.isString()) {
            String s = options.getCustomStr() != null ? options.getCustomStr() : "hola";
            data += l1("cad db '" + s + "'") + l1("col db 00h");
        }
        if (options.isUpper()) {
            String s = options.getCustomStr() != null ? options.getCustomStr() + "$" : "hola mundo$";
            data += l1("cad db '" + s + "'");
        }
        if (options.isInput()) {
            String msg = options.getCustomMsg() != null ? options.getCustomMsg() : "Presiona una tecla";
            data += l1("msg db '" + msg + "$'");
        }
        if (options.isArray())       data += l1("arr db 01h, 02h, 03h, 04h, 05h");

        return data + NL;
    }

    private boolean hasDataVars() {
        return options.isPrint() || options.isIfCond() || options.isSwitchCond()
            || options.isVars()  || options.isString() || options.isUpper()
            || options.isInput() || options.isArray();
    }

    private String buildCodeSection() {
        String code = ".code" + NL;

        if (hasDataVars())
            code += l1("mov ax, @data") + l1("mov ds, ax");

        code += NL + buildBody();

        if (!needsExitInBody())
            code += l1("; Finalizar el programa") + l1("mov ah, 4ch") + l1("int 21h");

        return code + "end" + NL;
    }

    // Templates que manejan su propia salida dentro del cuerpo (tienen etiqueta fin:)
    private boolean needsExitInBody() {
        return options.isIfCond() || options.isSwitchCond()
            || options.isUpper() || options.isInput();
    }

    private String buildBody() {
        if (options.isCursor() && options.isDelay())   return buildCursorDelayBody();
        if (options.isScreen() && options.isCursor())  return buildScreenCursorBody();
        if (options.isForLoop())    return buildForBody();
        if (options.isWhileLoop())  return buildWhileBody();
        if (options.isDoWhile())    return buildDoWhileBody();
        if (options.isIfCond())     return buildIfBody();
        if (options.isSwitchCond()) return buildSwitchBody();
        if (options.isPrint())      return buildPrintBody();
        if (options.isVars())       return buildVarsBody();
        if (options.isArray())      return buildArrayBody();
        if (options.isCursor())     return buildCursorBody();
        if (options.isScreen())     return buildScreenBody();
        if (options.isColor())      return buildColorBody();
        if (options.isDelay())      return buildDelayBody();
        if (options.isStack())      return buildStackBody();
        if (options.isString())     return buildStringBody();
        if (options.isUpper())      return buildUpperBody();
        if (options.isInput())      return buildInputBody();
        if (options.isRect())       return buildRectBody();
        if (options.isDiagonal())   return buildDiagonalBody();
        if (options.isRectSwap())   return buildRectSwapBody();
        if (options.isAlphabet())   return buildAlphabetBody();
        return l1("; Coloca aqui tu codigo") + NL;
    }

    // --- Templates genericos ---

    private String buildForBody() {
        String cx   = options.getLoopCount() > 0 ? hex(options.getLoopCount()) : "0ah";
        String body = options.isPrint()
            ? l2("mov ah, 09h") + l2("mov dx, offset msg") + l2("int 21h") + NL
            : l2("; Coloca aqui tu codigo") + NL;

        return l1("mov cx, " + cx) + NL
             + l1("ciclo:") + body
             + l2("loop ciclo") + NL;
    }

    private String buildWhileBody() {
        String cx = options.getLoopCount() > 0 ? hex(options.getLoopCount()) : "0ah";
        return l1("mov cx, " + cx) + NL
             + l1("mientras:")
             + l2("cmp cx, 00h")
             + l2("jz  fin_mientras") + NL
             + l2("; Coloca aqui tu codigo") + NL
             + l2("dec cx")
             + l2("jmp mientras") + NL
             + l1("fin_mientras:") + NL;
    }

    private String buildDoWhileBody() {
        String cx   = options.getLoopCount() > 0 ? hex(options.getLoopCount()) : "0ah";
        String body = options.isPrint()
            ? l2("mov ah, 09h") + l2("mov dx, offset msg") + l2("int 21h") + NL
            : l2("; Coloca aqui tu codigo") + NL;

        return l1("mov cx, " + cx) + NL
             + l1("inicio:") + body
             + l2("dec cx")
             + l2("jnz inicio") + NL;
    }

    private String buildPrintBody() {
        return l1("; Imprimir mensaje")
             + l1("mov ah, 09h")
             + l1("mov dx, offset msg")
             + l1("int 21h") + NL;
    }

    private String buildIfBody() {
        return l1("; Comparar y saltar segun condicion")
             + l1("cmp opc, 01")
             + l1("jz  verdadero")
             + l1("jmp falso") + NL
             + l1("verdadero:")
             + l2("mov ah, 09h") + l2("mov dx, offset msg") + l2("int 21h")
             + l2("jmp fin") + NL
             + l1("falso:")
             + l2("; Coloca aqui tu codigo") + NL
             + l1("fin:")
             + l2("; Finalizar el programa") + l2("mov ah, 4ch") + l2("int 21h");
    }

    private String buildSwitchBody() {
        return l1("; Comparar opc contra cada opcion")
             + l1("cmp opc, 01") + l1("jz  opcion1") + NL
             + l1("cmp opc, 02") + l1("jz  opcion2") + NL
             + l1("jmp opcion3") + NL
             + l1("opcion1:") + l2("mov dx, offset msg1") + l2("jmp imprimir") + NL
             + l1("opcion2:") + l2("mov dx, offset msg2") + l2("jmp imprimir") + NL
             + l1("opcion3:") + l2("mov dx, offset msg3") + NL
             + l1("imprimir:") + l2("mov ah, 09h") + l2("int 21h") + NL
             + l1("fin:")
             + l2("; Finalizar el programa") + l2("mov ah, 4ch") + l2("int 21h");
    }

    private String buildVarsBody() {
        return l1("; Cargar y modificar una variable")
             + l1("mov al, numero")
             + l1("inc al")
             + l1("mov numero, al") + NL;
    }

    private String buildArrayBody() {
        return l1("; Apuntar SI al inicio del arreglo")
             + l1("mov si, offset arr")
             + l1("mov cx, 05h") + NL
             + l1("ciclo:")
             + l2("mov al, [si]")
             + l2("; Coloca aqui tu codigo") + NL
             + l2("inc si")
             + l2("loop ciclo") + NL;
    }

    // --- Templates especificos ---

    private String buildCursorBody() {
        String row = options.getCursorRow() >= 0 ? String.valueOf(options.getCursorRow()) : "12";
        String col = options.getCursorCol() >= 0 ? String.valueOf(options.getCursorCol()) : "40";
        return l1("; Posicionar el cursor")
             + l1("mov ah, 02h")
             + l1("mov bh, 00h")
             + l1("mov dh, " + row)
             + l1("mov dl, " + col)
             + l1("int 10h") + NL;
    }

    private String buildScreenBody() {
        return l1("; Limpiar pantalla completa")
             + l1("mov ah, 06h")
             + l1("mov al, 00h")
             + l1("mov bh, 07h")
             + l1("mov cx, 0000h")
             + l1("mov dx, 184fh")
             + l1("int 10h") + NL;
    }

    private String buildScreenCursorBody() {
        return buildScreenBody()
             + l1("; Posicionar el cursor en fila 0, columna 0")
             + l1("mov ah, 02h")
             + l1("mov bh, 00h")
             + l1("mov dh, 00h")
             + l1("mov dl, 00h")
             + l1("int 10h") + NL;
    }

    private String buildCursorDelayBody() {
        return l1("; Posicionar el cursor al centro de la pantalla")
             + l1("mov ah, 02h")
             + l1("mov bh, 00h")
             + l1("mov dh, 12")
             + l1("mov dl, 40")
             + l1("int 10h") + NL
             + buildDelayBody()
             + l1("; Regresar el cursor al inicio")
             + l1("mov ah, 02h")
             + l1("mov bh, 00h")
             + l1("mov dh, 00h")
             + l1("mov dl, 00h")
             + l1("int 10h") + NL;
    }

    private String buildColorBody() {
        return l1("; Imprimir caracter con color")
             + l1("mov ah, 09h")
             + l1("mov al, 'A'")
             + l1("mov bh, 00h")
             + l1("mov bl, 0eh")
             + l1("mov cx, 01h")
             + l1("int 10h") + NL;
    }

    private String buildDelayBody() {
        int secs   = options.getDelaySecs() > 0 ? options.getDelaySecs() : 1;
        long micros = (long) secs * 1_000_000;
        String cx  = hex((int)(micros >> 16));
        String dx  = hex((int)(micros & 0xFFFF));
        return l1("; Esperar " + secs + " segundo(s)")
             + l1("mov ah, 86h")
             + l1("mov cx, " + cx)
             + l1("mov dx, " + dx)
             + l1("int 15h") + NL;
    }

    private String buildStackBody() {
        return l1("; Meter valores en la pila")
             + l1("mov bx, 1234h") + l1("push bx") + NL
             + l1("mov bx, 00ffh") + l1("push bx") + NL
             + l1("; Sacar valores de la pila (orden inverso al que entraron)")
             + l1("pop ax") + l1("pop ax") + NL;
    }

    private String buildStringBody() {
        int len = options.getCustomStr() != null ? options.getCustomStr().length() : 4;
        return l1("; Apuntar SI al inicio de la cadena")
             + l1("mov si, offset cad") + NL
             + l1("mov cx, " + hex(len))
             + l1("mov bh, 00h")
             + l1("mov bl, 0eh")
             + l1("mov dh, 05h") + NL
             + l1("ciclo:")
             + l2("push cx") + NL
             + l2("; Posicionar cursor en la columna actual")
             + l2("mov dl, col") + l2("mov ah, 02h") + l2("int 10h") + l2("inc col") + NL
             + l2("; Imprimir el caracter actual")
             + l2("mov ah, 09h") + l2("mov al, [si]") + l2("mov cx, 01h") + l2("int 10h")
             + l2("inc si") + NL
             + l2("pop cx")
             + l1("loop ciclo") + NL;
    }

    private String buildUpperBody() {
        return l1("; Apuntar SI al inicio de la cadena")
             + l1("mov si, offset cad") + NL
             + l1("siguiente:")
             + l2("mov al, [si]") + l2("cmp al, '$'") + l2("je  fin") + NL
             + l2("; Convertir a mayuscula solo si es minuscula (a-z)")
             + l2("cmp al, 'a'") + l2("jb  imprimir")
             + l2("cmp al, 'z'") + l2("ja  imprimir")
             + l2("sub al, 20h") + NL
             + l1("imprimir:")
             + l2("mov dl, al") + l2("mov ah, 02h") + l2("int 21h") + NL
             + l2("inc si") + l2("jmp siguiente") + NL
             + l1("fin:")
             + l2("; Finalizar el programa") + l2("mov ah, 4ch") + l2("int 21h");
    }

    private String buildInputBody() {
        return l1("; Imprimir mensaje de espera")
             + l1("mov ah, 09h") + l1("mov dx, offset msg") + l1("int 21h") + NL
             + l1("ciclo:")
             + l2("mov ah, 0bh") + l2("int 21h") + l2("cmp al, 00h") + l2("jz  ciclo") + NL
             + l1("fin:")
             + l2("; Finalizar el programa") + l2("mov ah, 4ch") + l2("int 21h");
    }

    private String buildRectBody() {
        return l1("; Dibujar rectangulo con color de fondo")
             + l1("mov ah, 06h") + l1("mov al, 00h")
             + l1("mov bh, 40h")
             + l1("mov ch, 05") + l1("mov cl, 05")
             + l1("mov dh, 15") + l1("mov dl, 25")
             + l1("int 10h") + NL;
    }

    private String buildDiagonalBody() {
        return l1("; Limpiar pantalla a negro")
             + l1("mov ah, 06h") + l1("mov al, 00h") + l1("mov bh, 00h")
             + l1("mov cx, 0000h") + l1("mov dx, 184fh") + l1("int 10h") + NL
             + l1("; Posicion inicial")
             + l1("mov dh, 00h")
             + l1("mov dl, 00h")
             + l1("mov cx, " + (options.getDiagonalSteps() > 0 ? hex(options.getDiagonalSteps()) : "14h")) + NL
             + l1("ciclo:")
             + l2("inc dh")
             + l2("inc dl") + NL
             + l2("push cx")
             + l2("push dx") + NL
             + l2("; Mover cursor")
             + l2("mov ah, 02h") + l2("mov bh, 00h") + l2("int 10h") + NL
             + l2("; Imprimir caracter con color amarillo en la posicion actual")
             + l2("mov ah, 09h") + l2("mov al, 0dbh")
             + l2("mov bh, 00h") + l2("mov bl, 0eh")
             + l2("mov cx, 01h") + l2("int 10h") + NL
             + l2("; Delay")
             + l2("mov ah, 86h") + l2("mov cx, 0007h") + l2("mov dx, 0a120h") + l2("int 15h") + NL
             + l2("pop dx")
             + l2("pop cx")
             + l2("dec cx")
             + l2("jnz ciclo") + NL;
    }

    private String buildRectSwapBody() {
        return l1("; Limpiar pantalla")
             + l1("mov ah, 06h") + l1("mov al, 00h") + l1("mov bh, 07h")
             + l1("mov cx, 0000h") + l1("mov dx, 184fh") + l1("int 10h") + NL
             + l1("mov cx, " + (options.getRectSwapCycles() > 0 ? hex(options.getRectSwapCycles()) : "0fh")) + NL
             + l1("ciclo:")
             + l2("push cx") + NL
             + l2("; Primer rectangulo rojo")
             + l2("mov ah, 06h") + l2("mov al, 00h") + l2("mov bh, 40h")
             + l2("mov ch, 05h") + l2("mov cl, 05h")
             + l2("mov dh, 15h") + l2("mov dl, 25h") + l2("int 10h") + NL
             + l2("; Segundo rectangulo blanco")
             + l2("mov ah, 06h") + l2("mov al, 00h") + l2("mov bh, 0f0h")
             + l2("mov ch, 05h") + l2("mov cl, 35h")
             + l2("mov dh, 15h") + l2("mov dl, 55h") + l2("int 10h") + NL
             + l2("; Delay")
             + l2("mov ah, 86h") + l2("mov cx, 0007h") + l2("mov dx, 0a120h") + l2("int 15h") + NL
             + l2("; Intercambiar colores")
             + l2("mov ah, 06h") + l2("mov al, 00h") + l2("mov bh, 0f0h")
             + l2("mov ch, 05h") + l2("mov cl, 05h")
             + l2("mov dh, 15h") + l2("mov dl, 25h") + l2("int 10h") + NL
             + l2("mov ah, 06h") + l2("mov al, 00h") + l2("mov bh, 40h")
             + l2("mov ch, 05h") + l2("mov cl, 35h")
             + l2("mov dh, 15h") + l2("mov dl, 55h") + l2("int 10h") + NL
             + l2("; Delay")
             + l2("mov ah, 86h") + l2("mov cx, 0007h") + l2("mov dx, 0a120h") + l2("int 15h") + NL
             + l2("pop cx")
             + l2("loop ciclo") + NL;
    }

    private String buildAlphabetBody() {
        return l1("; Imprimir el abecedario letra por letra con delay")
             + l1("mov al, 'A'")
             + l1("mov cx, 1ah") + NL
             + l1("ciclo:")
             + l2("push cx")
             + l2("push ax") + NL
             + l2("; Imprimir letra")
             + l2("mov dl, al")
             + l2("mov ah, 02h")
             + l2("int 21h") + NL
             + l2("; Salto de linea (CR + LF)")
             + l2("mov dl, 0dh") + l2("int 21h")
             + l2("mov dl, 0ah") + l2("int 21h") + NL
             + l2("; Delay")
             + l2("mov ah, 86h")
             + l2("mov cx, 000fh")
             + l2("mov dx, 4240h")
             + l2("int 15h") + NL
             + l2("pop ax")
             + l2("inc al")
             + l2("pop cx")
             + l2("loop ciclo") + NL;
    }
}
