package asmbuilder;

// Construye el contenido .asm combinando secciones segun los flags del usuario
public class TemplateBuilder {

    private static final String NL  = "\n";
    private static final String T1  = "    ";      // 4 espacios - nivel PROC
    private static final String T2  = "        ";  // 8 espacios - nivel dentro de etiqueta

    private final Options options;

    public TemplateBuilder(Options options) {
        this.options = options;
    }

    public String build() {
        return buildHeader()
             + buildDataSection()
             + buildCodeSection();
    }

    // .MODEL, .STACK
    private String buildHeader() {
        return ".MODEL SMALL" + NL
             + ".STACK 100H" + NL
             + NL;
    }

    // .DATA con las variables que necesiten los flags activos
    private String buildDataSection() {
        StringBuilder data = new StringBuilder();
        data.append(".DATA").append(NL);

        if (options.isPrint() && !options.isIfCond() && !options.isSwitchCond()) {
            data.append(T1).append("MSG DB 'Hola Mundo$'").append(NL);
        }

        if (options.isVars()) {
            data.append(T1).append("NUMERO  DB 05").append(NL);
            data.append(T1).append("MENSAJE DB 'Texto$'").append(NL);
            data.append(T1).append("BANDERA DB 00").append(NL);
        }

        if (options.isIfCond()) {
            data.append(T1).append("OPC DB 01").append(NL);
            data.append(T1).append("MSG DB 'Condicion verdadera$'").append(NL);
        }

        if (options.isSwitchCond()) {
            data.append(T1).append("OPC  DB 01").append(NL);
            data.append(T1).append("MSG1 DB 'Opcion uno$'").append(NL);
            data.append(T1).append("MSG2 DB 'Opcion dos$'").append(NL);
            data.append(T1).append("MSG3 DB 'Opcion no valida$'").append(NL);
        }

        data.append(NL);
        return data.toString();
    }

    // .CODE con MAIN PROC, inicializacion, cuerpo y salida
    private String buildCodeSection() {
        StringBuilder code = new StringBuilder();
        code.append(".CODE").append(NL).append(NL);
        code.append("MAIN PROC").append(NL);
        code.append(T1).append("MOV AX, @DATA").append(NL);
        code.append(T1).append("MOV DS, AX").append(NL).append(NL);

        code.append(buildBody());

        // --if y --switch ya incluyen la salida dentro del cuerpo (en fin:)
        if (!options.isIfCond() && !options.isSwitchCond()) {
            code.append(T1).append("MOV AH, 4CH").append(NL);
            code.append(T1).append("INT 21H").append(NL);
        }

        code.append("MAIN ENDP").append(NL);
        code.append("END MAIN").append(NL);

        return code.toString();
    }

    // Decide que cuerpo generar segun los flags
    private String buildBody() {
        if (options.isForLoop())    return buildForBody();
        if (options.isDoWhile())    return buildDoWhileBody();
        if (options.isIfCond())     return buildIfBody();
        if (options.isSwitchCond()) return buildSwitchBody();
        if (options.isPrint())      return buildPrintBody();
        if (options.isVars())       return buildVarsBody();
        return ""; // --empty: sin cuerpo
    }

    // Ciclo FOR usando la instruccion LOOP (decrementa CX automaticamente)
    private String buildForBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("MOV CX, 0AH").append(NL).append(NL); // 10 repeticiones

        sb.append(T1).append("ciclo:").append(NL);

        if (options.isPrint()) {
            sb.append(T2).append("MOV AH, 09H").append(NL);
            sb.append(T2).append("MOV DX, OFFSET MSG").append(NL);
            sb.append(T2).append("INT 21H").append(NL).append(NL);
        }

        sb.append(T2).append("LOOP ciclo").append(NL).append(NL);
        return sb.toString();
    }

    // Ciclo DO-WHILE: ejecuta al menos una vez, luego verifica CX con DEC/JNZ
    private String buildDoWhileBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("MOV CX, 0AH").append(NL).append(NL); // 10 repeticiones

        sb.append(T1).append("inicio:").append(NL);

        if (options.isPrint()) {
            sb.append(T2).append("MOV AH, 09H").append(NL);
            sb.append(T2).append("MOV DX, OFFSET MSG").append(NL);
            sb.append(T2).append("INT 21H").append(NL).append(NL);
        }

        // DEC + JNZ es la forma de do-while: cuerpo primero, condicion al final
        sb.append(T2).append("DEC CX").append(NL);
        sb.append(T2).append("JNZ inicio").append(NL).append(NL);

        return sb.toString();
    }

    // Impresion simple con INT 21H servicio 09H
    private String buildPrintBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("MOV AH, 09H").append(NL);
        sb.append(T1).append("MOV DX, OFFSET MSG").append(NL);
        sb.append(T1).append("INT 21H").append(NL).append(NL);
        return sb.toString();
    }

    // IF: CMP + salto condicional JZ/JNZ
    private String buildIfBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("CMP OPC, 01").append(NL);
        sb.append(T1).append("JZ  verdadero").append(NL);
        sb.append(T1).append("JMP falso").append(NL).append(NL);

        sb.append(T1).append("verdadero:").append(NL);
        sb.append(T2).append("MOV AH, 09H").append(NL);
        sb.append(T2).append("MOV DX, OFFSET MSG").append(NL);
        sb.append(T2).append("INT 21H").append(NL);
        sb.append(T2).append("JMP fin").append(NL).append(NL);

        sb.append(T1).append("falso:").append(NL);
        sb.append(T2).append("; condicion no cumplida").append(NL).append(NL);

        // fin incluye la salida del programa
        sb.append(T1).append("fin:").append(NL);
        sb.append(T2).append("MOV AH, 4CH").append(NL);
        sb.append(T2).append("INT 21H").append(NL);

        return sb.toString();
    }

    // SWITCH: multiples CMP encadenados, patron de cond4.asm
    private String buildSwitchBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("CMP OPC, 01").append(NL);
        sb.append(T1).append("JZ  opcion1").append(NL).append(NL);

        sb.append(T1).append("CMP OPC, 02").append(NL);
        sb.append(T1).append("JZ  opcion2").append(NL).append(NL);

        sb.append(T1).append("JMP opcion3").append(NL).append(NL);

        sb.append(T1).append("opcion1:").append(NL);
        sb.append(T2).append("MOV DX, OFFSET MSG1").append(NL);
        sb.append(T2).append("JMP imprimir").append(NL).append(NL);

        sb.append(T1).append("opcion2:").append(NL);
        sb.append(T2).append("MOV DX, OFFSET MSG2").append(NL);
        sb.append(T2).append("JMP imprimir").append(NL).append(NL);

        sb.append(T1).append("opcion3:").append(NL);
        sb.append(T2).append("MOV DX, OFFSET MSG3").append(NL).append(NL);

        sb.append(T1).append("imprimir:").append(NL);
        sb.append(T2).append("MOV AH, 09H").append(NL);
        sb.append(T2).append("INT 21H").append(NL).append(NL);

        // fin incluye la salida del programa
        sb.append(T1).append("fin:").append(NL);
        sb.append(T2).append("MOV AH, 4CH").append(NL);
        sb.append(T2).append("INT 21H").append(NL);

        return sb.toString();
    }

    // Variables: muestra como declarar y acceder a variables en .DATA
    private String buildVarsBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(T1).append("MOV AL, NUMERO").append(NL).append(NL);
        sb.append(T1).append("INC AL").append(NL);
        sb.append(T1).append("MOV NUMERO, AL").append(NL).append(NL);
        return sb.toString();
    }
}
