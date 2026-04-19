package asmbuilder;

// Punto de entrada. Lee el comando y lo delega al handler correspondiente
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        switch (command) {
            case "new":
                handleNew(args);
                break;
            case "help":
                printHelp();
                break;
            default:
                System.out.println("Comando desconocido: " + command);
                System.out.println("Usa 'asmb help' para ver los comandos disponibles.");
        }
    }

    private static void handleNew(String[] args) {
        Options options = ArgParser.parse(args);
        if (options == null) {
            System.out.println("Usa 'asmb help' para ver el uso correcto.");
            return;
        }

        String content = new TemplateBuilder(options).build();
        AsmFileWriter.write(options.getFileName(), content);
    }

    private static void printHelp() {
        System.out.println("");
        System.out.println("AsmBuilder - Generador de plantillas .asm (TASM/DOS)");
        System.out.println("");
        System.out.println("Uso:");
        System.out.println("  asmb new <archivo> --template [opciones]");
        System.out.println("  asmb new <archivo> --template --for --print");
        System.out.println("");
        System.out.println("Comandos:");
        System.out.println("  new     Crea un nuevo archivo .asm");
        System.out.println("  help    Muestra esta ayuda");
        System.out.println("");
        System.out.println("Opciones de plantilla:");
        System.out.println("  --empty     Plantilla base sin codigo (por defecto)");
        System.out.println("  --print     Imprime un mensaje con INT 21H");
        System.out.println("  --for       Ciclo FOR con instruccion LOOP");
        System.out.println("  --dowhile   Ciclo DO-WHILE con DEC/JNZ");
        System.out.println("  --if        Condicional IF con CMP/JZ");
        System.out.println("  --switch    Switch con multiples CMP encadenados");
        System.out.println("  --vars      Variables en .DATA con ejemplos de acceso");
        System.out.println("");
        System.out.println("Combinaciones:");
        System.out.println("  --for --print      Ciclo que imprime en cada iteracion");
        System.out.println("  --dowhile --print  DO-WHILE que imprime en cada iteracion");
        System.out.println("");
        System.out.println("Ejemplos:");
        System.out.println("  asmb new hola --template --empty");
        System.out.println("  asmb new hola --template --print");
        System.out.println("  asmb new hola --template --for --print");
        System.out.println("  asmb new hola --template --dowhile");
        System.out.println("  asmb new hola --template --switch");
        System.out.println("");
    }
}
