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
        System.out.println("");
        System.out.println("Comandos:");
        System.out.println("  new     Crea un nuevo archivo .asm");
        System.out.println("  help    Muestra esta ayuda");
        System.out.println("");
        System.out.println("--- Genericos ---");
        System.out.println("  --empty     Estructura base sin codigo");
        System.out.println("  --print     Imprime un mensaje (INT 21h, servicio 09h)");
        System.out.println("  --for       Ciclo FOR con instruccion LOOP y CX como contador");
        System.out.println("  --while     Ciclo WHILE: verifica condicion antes de ejecutar");
        System.out.println("  --dowhile   Ciclo DO-WHILE con DEC/JNZ");
        System.out.println("  --if        Condicional IF con CMP/JZ");
        System.out.println("  --switch    Switch con multiples CMP encadenados");
        System.out.println("  --vars      Variables en .data con ejemplos de acceso");
        System.out.println("  --array     Arreglo en .data recorrido con SI como puntero");
        System.out.println("");
        System.out.println("--- Especificos ---");
        System.out.println("  --cursor    Posicionar el cursor en fila/columna (INT 10h)");
        System.out.println("  --screen    Limpiar la pantalla completa (INT 10h)");
        System.out.println("  --color     Imprimir caracter con color (INT 10h, servicio 09h)");
        System.out.println("  --delay     Esperar 1 segundo (INT 15h)");
        System.out.println("  --stack     Operaciones push/pop en la pila");
        System.out.println("  --string    Recorrer cadena e imprimir cada caracter con posicion");
        System.out.println("  --upper     Recorrer cadena y convertir minusculas a mayusculas");
        System.out.println("  --input     Esperar a que el usuario presione una tecla (INT 21h)");
        System.out.println("  --rect      Dibujar rectangulo con color de fondo (INT 10h)");
        System.out.println("");
        System.out.println("--- Combinaciones ---");
        System.out.println("  --for --print        Ciclo que imprime en cada iteracion");
        System.out.println("  --dowhile --print    DO-WHILE que imprime en cada iteracion");
        System.out.println("  --cursor --delay     Mover cursor, esperar y regresar al origen");
        System.out.println("  --screen --cursor    Limpiar pantalla y posicionar cursor");
        System.out.println("");
        System.out.println("Ejemplos:");
        System.out.println("  asmb new hola --template --empty");
        System.out.println("  asmb new hola --template --for --print");
        System.out.println("  asmb new hola --template --cursor --delay");
        System.out.println("  asmb new hola --template --upper");
        System.out.println("  asmb new hola --template --rect");
        System.out.println("");
    }
}
