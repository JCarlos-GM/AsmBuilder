package asmbuilder;

// Construye el contenido .asm combinando secciones segun los flags del usuario
public class TemplateBuilder {

    private static final String NL = "\n";
    private static final String T1 = "    "; // 4 espacios - codigo general
    private static final String T2 = "        "; // 8 espacios - codigo dentro de etiqueta

    private final Options options;

    public TemplateBuilder(Options options) {
        this.options = options;
    }

    public String build() {
        return buildHeader()
             + buildDataSection()
             + buildCodeSection();
    }

    // Encabezado: comentario, .model y .stack
    private String buildHeader() {
        return "; Tamaño del programa" + NL
             + ".model small"          + NL
             + ";"                     + NL
             + ".stack 50h"            + NL
             + NL;
    }

    // .data con variables o comentario guia si no hay ninguna
    private String buildDataSection() {
        StringBuilder data = new StringBuilder();
        data.append(".data").append(NL);

        if (!hasDataVars()) {
            data.append(T1).append("; Coloca aqui tus variables").append(NL);
        }

        if (options.isPrint() && !options.isIfCond() && !options.isSwitchCond()) {
            data.append(T1).append("msg db 'Hola Mundo$'").append(NL);
        }

        if (options.isVars()) {
            data.append(T1).append("numero  db 05").append(NL);
            data.append(T1).append("mensaje db 'Texto$'").append(NL);
            data.append(T1).append("bandera db 00").append(NL);
        }

        if (options.isIfCond()) {
            data.append(T1).append("opc db 01").append(NL);
            data.append(T1).append("msg db 'Condicion verdadera$'").append(NL);
        }

        if (options.isSwitchCond()) {
            data.append(T1).append("opc  db 01").append(NL);
            data.append(T1).append("msg1 db 'Opcion uno$'").append(NL);
            data.append(T1).append("msg2 db 'Opcion dos$'").append(NL);
            data.append(T1).append("msg3 db 'Opcion no valida$'").append(NL);
        }

        if (options.isString()) {
            data.append(T1).append("cad db 'hola'").append(NL);
            data.append(T1).append("col db 00h").append(NL);
        }

        if (options.isUpper()) {
            data.append(T1).append("cad db 'hola mundo$'").append(NL);
        }

        if (options.isInput()) {
            data.append(T1).append("msg db 'Presiona una tecla$'").append(NL);
        }

        data.append(NL);
        return data.toString();
    }

    // Devuelve true cuando hay variables declaradas en .data
    private boolean hasDataVars() {
        return options.isPrint() || options.isIfCond() || options.isSwitchCond()
            || options.isVars() || options.isString() || options.isUpper()
            || options.isInput();
    }

    // .code con inicializacion (si hay variables), cuerpo y salida
    private String buildCodeSection() {
        StringBuilder code = new StringBuilder();
        code.append(".code").append(NL);

        // Solo inicializar ds cuando hay variables en .data
        if (hasDataVars()) {
            code.append(T1).append("mov ax, @data").append(NL);
            code.append(T1).append("mov ds, ax").append(NL);
        }

        code.append(NL);
        code.append(buildBody());

        // Algunos templates incluyen la salida en su propio cuerpo (con fin:)
        if (!needsExitInBody()) {
            code.append(T1).append("; Finalizar el programa").append(NL);
            code.append(T1).append("mov ah, 4ch").append(NL);
            code.append(T1).append("int 21h").append(NL);
        }

        code.append("end").append(NL);

        return code.toString();
    }

    // Templates que contienen su propia etiqueta fin: con la salida del programa
    private boolean needsExitInBody() {
        return options.isIfCond() || options.isSwitchCond()
            || options.isUpper() || options.isInput();
    }

    // Decide que cuerpo generar segun los flags (combinaciones primero)
    private String buildBody() {
        // Combinaciones especificas
        if (options.isCursor() && options.isDelay()) return buildCursorDelayBody();
        if (options.isScreen() && options.isCursor()) return buildScreenCursorBody();

        // Templates individuales genericos
        if (options.isForLoop())    return buildForBody();
        if (options.isDoWhile())    return buildDoWhileBody();
        if (options.isIfCond())     return buildIfBody();
        if (options.isSwitchCond()) return buildSwitchBody();
        if (options.isPrint())      return buildPrintBody();
        if (options.isVars())       return buildVarsBody();

        // Templates individuales especificos
        if (options.isCursor())     return buildCursorBody();
        if (options.isScreen())     return buildScreenBody();
        if (options.isColor())      return buildColorBody();
        if (options.isDelay())      return buildDelayBody();
        if (options.isStack())      return buildStackBody();
        if (options.isString())     return buildStringBody();
        if (options.isUpper())      return buildUpperBody();
        if (options.isInput())      return buildInputBody();
        if (options.isRect())       return buildRectBody();

        // --empty: solo el comentario guia
        return T1 + "; Coloca aqui tu codigo" + NL + NL;
    }

    // --- Templates genericos ---

    // Ciclo FOR usando la instruccion loop (decrementa cx automaticamente)
    private String buildForBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("mov cx, 0ah").append(NL).append(NL); // 10 repeticiones

        sb.append(T1).append("ciclo:").append(NL);

        if (options.isPrint()) {
            sb.append(T2).append("mov ah, 09h").append(NL);
            sb.append(T2).append("mov dx, offset msg").append(NL);
            sb.append(T2).append("int 21h").append(NL).append(NL);
        } else {
            sb.append(T2).append("; Coloca aqui tu codigo").append(NL).append(NL);
        }

        sb.append(T2).append("loop ciclo").append(NL).append(NL);
        return sb.toString();
    }

    // Ciclo DO-WHILE: ejecuta al menos una vez, luego verifica cx con dec/jnz
    private String buildDoWhileBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("mov cx, 0ah").append(NL).append(NL); // 10 repeticiones

        sb.append(T1).append("inicio:").append(NL);

        if (options.isPrint()) {
            sb.append(T2).append("mov ah, 09h").append(NL);
            sb.append(T2).append("mov dx, offset msg").append(NL);
            sb.append(T2).append("int 21h").append(NL).append(NL);
        } else {
            sb.append(T2).append("; Coloca aqui tu codigo").append(NL).append(NL);
        }

        // dec + jnz es la forma de do-while: cuerpo primero, condicion al final
        sb.append(T2).append("dec cx").append(NL);
        sb.append(T2).append("jnz inicio").append(NL).append(NL);

        return sb.toString();
    }

    // Impresion simple con int 21h servicio 09h
    private String buildPrintBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Imprimir mensaje").append(NL);
        sb.append(T1).append("mov ah, 09h").append(NL);
        sb.append(T1).append("mov dx, offset msg").append(NL);
        sb.append(T1).append("int 21h").append(NL).append(NL);
        return sb.toString();
    }

    // IF: cmp + salto condicional jz/jnz
    private String buildIfBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Comparar y saltar segun condicion").append(NL);
        sb.append(T1).append("cmp opc, 01").append(NL);
        sb.append(T1).append("jz  verdadero").append(NL);
        sb.append(T1).append("jmp falso").append(NL).append(NL);

        sb.append(T1).append("verdadero:").append(NL);
        sb.append(T2).append("mov ah, 09h").append(NL);
        sb.append(T2).append("mov dx, offset msg").append(NL);
        sb.append(T2).append("int 21h").append(NL);
        sb.append(T2).append("jmp fin").append(NL).append(NL);

        sb.append(T1).append("falso:").append(NL);
        sb.append(T2).append("; Coloca aqui tu codigo").append(NL).append(NL);

        sb.append(T1).append("fin:").append(NL);
        sb.append(T2).append("; Finalizar el programa").append(NL);
        sb.append(T2).append("mov ah, 4ch").append(NL);
        sb.append(T2).append("int 21h").append(NL);

        return sb.toString();
    }

    // SWITCH: multiples cmp encadenados, patron de cond4.asm
    private String buildSwitchBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Comparar opc contra cada opcion").append(NL);
        sb.append(T1).append("cmp opc, 01").append(NL);
        sb.append(T1).append("jz  opcion1").append(NL).append(NL);

        sb.append(T1).append("cmp opc, 02").append(NL);
        sb.append(T1).append("jz  opcion2").append(NL).append(NL);

        sb.append(T1).append("jmp opcion3").append(NL).append(NL);

        sb.append(T1).append("opcion1:").append(NL);
        sb.append(T2).append("mov dx, offset msg1").append(NL);
        sb.append(T2).append("jmp imprimir").append(NL).append(NL);

        sb.append(T1).append("opcion2:").append(NL);
        sb.append(T2).append("mov dx, offset msg2").append(NL);
        sb.append(T2).append("jmp imprimir").append(NL).append(NL);

        sb.append(T1).append("opcion3:").append(NL);
        sb.append(T2).append("mov dx, offset msg3").append(NL).append(NL);

        sb.append(T1).append("imprimir:").append(NL);
        sb.append(T2).append("mov ah, 09h").append(NL);
        sb.append(T2).append("int 21h").append(NL).append(NL);

        sb.append(T1).append("fin:").append(NL);
        sb.append(T2).append("; Finalizar el programa").append(NL);
        sb.append(T2).append("mov ah, 4ch").append(NL);
        sb.append(T2).append("int 21h").append(NL);

        return sb.toString();
    }

    // Variables: muestra como declarar y acceder a variables en .data
    private String buildVarsBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Cargar y modificar una variable").append(NL);
        sb.append(T1).append("mov al, numero").append(NL);
        sb.append(T1).append("inc al").append(NL);
        sb.append(T1).append("mov numero, al").append(NL).append(NL);
        return sb.toString();
    }

    // --- Templates especificos ---

    // Posicionar el cursor en una fila y columna especifica (INT 10h servicio 02h)
    private String buildCursorBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Posicionar el cursor").append(NL);
        sb.append(T1).append("mov ah, 02h").append(NL);
        sb.append(T1).append("mov bh, 00h").append(NL); // pagina siempre 0
        sb.append(T1).append("mov dh, 12").append(NL);  // fila   (0-24)
        sb.append(T1).append("mov dl, 40").append(NL);  // columna (0-79)
        sb.append(T1).append("int 10h").append(NL).append(NL);
        return sb.toString();
    }

    // Limpiar la pantalla completa (INT 10h servicio 06h con AL=0)
    private String buildScreenBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Limpiar pantalla completa").append(NL);
        sb.append(T1).append("mov ah, 06h").append(NL);
        sb.append(T1).append("mov al, 00h").append(NL);
        sb.append(T1).append("mov bh, 07h").append(NL);   // color de fondo (gris)
        sb.append(T1).append("mov cx, 0000h").append(NL); // esquina superior izquierda
        sb.append(T1).append("mov dx, 184fh").append(NL); // esquina inferior derecha
        sb.append(T1).append("int 10h").append(NL).append(NL);
        return sb.toString();
    }

    // Limpiar pantalla y luego posicionar el cursor (combinacion de cur.asm y cursor.asm)
    private String buildScreenCursorBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(buildScreenBody());

        sb.append(T1).append("; Posicionar el cursor en fila 0, columna 0").append(NL);
        sb.append(T1).append("mov ah, 02h").append(NL);
        sb.append(T1).append("mov bh, 00h").append(NL);
        sb.append(T1).append("mov dh, 00h").append(NL);
        sb.append(T1).append("mov dl, 00h").append(NL);
        sb.append(T1).append("int 10h").append(NL).append(NL);
        return sb.toString();
    }

    // Posicionar cursor, esperar y regresar al origen (patron de cur2.asm)
    private String buildCursorDelayBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Posicionar el cursor al centro de la pantalla").append(NL);
        sb.append(T1).append("mov ah, 02h").append(NL);
        sb.append(T1).append("mov bh, 00h").append(NL);
        sb.append(T1).append("mov dh, 12").append(NL);  // fila centro
        sb.append(T1).append("mov dl, 40").append(NL);  // columna centro
        sb.append(T1).append("int 10h").append(NL).append(NL);

        sb.append(buildDelayBody());

        sb.append(T1).append("; Regresar el cursor al inicio").append(NL);
        sb.append(T1).append("mov ah, 02h").append(NL);
        sb.append(T1).append("mov bh, 00h").append(NL);
        sb.append(T1).append("mov dh, 00h").append(NL);
        sb.append(T1).append("mov dl, 00h").append(NL);
        sb.append(T1).append("int 10h").append(NL).append(NL);
        return sb.toString();
    }

    // Imprimir un caracter con color usando INT 10h (patron de code2.asm)
    private String buildColorBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Imprimir caracter con color").append(NL);
        sb.append(T1).append("mov ah, 09h").append(NL);
        sb.append(T1).append("mov al, 'A'").append(NL); // caracter a imprimir
        sb.append(T1).append("mov bh, 00h").append(NL); // pagina 0
        sb.append(T1).append("mov bl, 0eh").append(NL); // color (amarillo)
        sb.append(T1).append("mov cx, 01h").append(NL); // repeticiones
        sb.append(T1).append("int 10h").append(NL).append(NL);
        return sb.toString();
    }

    // Esperar 1 segundo con INT 15h (tiempo en microsegundos)
    private String buildDelayBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Esperar 1 segundo (1,000,000 microsegundos)").append(NL);
        sb.append(T1).append("mov ah, 86h").append(NL);
        sb.append(T1).append("mov cx, 000fh").append(NL); // parte alta
        sb.append(T1).append("mov dx, 4240h").append(NL); // parte baja
        sb.append(T1).append("int 15h").append(NL).append(NL);
        return sb.toString();
    }

    // Push y pop de la pila (patron de pila.asm)
    private String buildStackBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Meter valores en la pila").append(NL);
        sb.append(T1).append("mov bx, 1234h").append(NL);
        sb.append(T1).append("push bx").append(NL).append(NL);

        sb.append(T1).append("mov bx, 00ffh").append(NL);
        sb.append(T1).append("push bx").append(NL).append(NL);

        sb.append(T1).append("; Sacar valores de la pila (orden inverso al que entraron)").append(NL);
        sb.append(T1).append("pop ax").append(NL);
        sb.append(T1).append("pop ax").append(NL).append(NL);
        return sb.toString();
    }

    // Recorrer una cadena e imprimir cada caracter con posicion (patron de strg2.asm)
    private String buildStringBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Apuntar SI al inicio de la cadena").append(NL);
        sb.append(T1).append("mov si, offset cad").append(NL).append(NL);

        sb.append(T1).append("mov cx, 04h").append(NL); // longitud de la cadena
        sb.append(T1).append("mov bh, 00h").append(NL);
        sb.append(T1).append("mov bl, 0eh").append(NL); // color amarillo
        sb.append(T1).append("mov dh, 05h").append(NL).append(NL); // fila

        sb.append(T1).append("ciclo:").append(NL);
        sb.append(T2).append("push cx").append(NL).append(NL);

        sb.append(T2).append("; Posicionar cursor en la columna actual").append(NL);
        sb.append(T2).append("mov dl, col").append(NL);
        sb.append(T2).append("mov ah, 02h").append(NL);
        sb.append(T2).append("int 10h").append(NL);
        sb.append(T2).append("inc col").append(NL).append(NL);

        sb.append(T2).append("; Imprimir el caracter actual").append(NL);
        sb.append(T2).append("mov ah, 09h").append(NL);
        sb.append(T2).append("mov al, [si]").append(NL); // caracter al que apunta SI
        sb.append(T2).append("mov cx, 01h").append(NL);
        sb.append(T2).append("int 10h").append(NL);
        sb.append(T2).append("inc si").append(NL).append(NL);

        sb.append(T2).append("pop cx").append(NL);
        sb.append(T1).append("loop ciclo").append(NL).append(NL);
        return sb.toString();
    }

    // Recorrer una cadena y convertir cada letra minuscula a mayuscula (patron de uperc.asm)
    private String buildUpperBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Apuntar SI al inicio de la cadena").append(NL);
        sb.append(T1).append("mov si, offset cad").append(NL).append(NL);

        sb.append(T1).append("siguiente:").append(NL);
        sb.append(T2).append("mov al, [si]").append(NL);    // obtener caracter actual
        sb.append(T2).append("cmp al, '$'").append(NL);     // fin de cadena
        sb.append(T2).append("je  fin").append(NL).append(NL);

        sb.append(T2).append("; Convertir a mayuscula solo si es minuscula (a-z)").append(NL);
        sb.append(T2).append("cmp al, 'a'").append(NL);
        sb.append(T2).append("jb  imprimir").append(NL);
        sb.append(T2).append("cmp al, 'z'").append(NL);
        sb.append(T2).append("ja  imprimir").append(NL);
        sb.append(T2).append("sub al, 20h").append(NL).append(NL); // diferencia ASCII mayuscula/minuscula

        sb.append(T1).append("imprimir:").append(NL);
        sb.append(T2).append("mov dl, al").append(NL);
        sb.append(T2).append("mov ah, 02h").append(NL);
        sb.append(T2).append("int 21h").append(NL).append(NL);

        sb.append(T2).append("inc si").append(NL);
        sb.append(T2).append("jmp siguiente").append(NL).append(NL);

        sb.append(T1).append("fin:").append(NL);
        sb.append(T2).append("; Finalizar el programa").append(NL);
        sb.append(T2).append("mov ah, 4ch").append(NL);
        sb.append(T2).append("int 21h").append(NL);

        return sb.toString();
    }

    // Esperar a que el usuario presione una tecla (patron de tecla.asm)
    private String buildInputBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Imprimir mensaje de espera").append(NL);
        sb.append(T1).append("mov ah, 09h").append(NL);
        sb.append(T1).append("mov dx, offset msg").append(NL);
        sb.append(T1).append("int 21h").append(NL).append(NL);

        // 0Bh verifica si hay tecla disponible: devuelve FFh si hay, 00h si no
        sb.append(T1).append("ciclo:").append(NL);
        sb.append(T2).append("mov ah, 0bh").append(NL);
        sb.append(T2).append("int 21h").append(NL);
        sb.append(T2).append("cmp al, 00h").append(NL);
        sb.append(T2).append("jz  ciclo").append(NL).append(NL);

        sb.append(T1).append("fin:").append(NL);
        sb.append(T2).append("; Finalizar el programa").append(NL);
        sb.append(T2).append("mov ah, 4ch").append(NL);
        sb.append(T2).append("int 21h").append(NL);

        return sb.toString();
    }

    // Dibujar un rectangulo de color (INT 10h servicio 06h, patron de rectswap.asm)
    private String buildRectBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("; Dibujar rectangulo con color de fondo").append(NL);
        sb.append(T1).append("mov ah, 06h").append(NL);
        sb.append(T1).append("mov al, 00h").append(NL);
        sb.append(T1).append("mov bh, 40h").append(NL); // color de fondo (rojo)
        sb.append(T1).append("mov ch, 05").append(NL);  // fila superior
        sb.append(T1).append("mov cl, 05").append(NL);  // columna izquierda
        sb.append(T1).append("mov dh, 15").append(NL);  // fila inferior
        sb.append(T1).append("mov dl, 25").append(NL);  // columna derecha
        sb.append(T1).append("int 10h").append(NL).append(NL);
        return sb.toString();
    }
}
