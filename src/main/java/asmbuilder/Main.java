package asmbuilder;

// Punto de entrada. Lee el comando y lo delega al handler correspondiente
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        switch (args[0]) {
            case "new":  handleNew(args);         break;
            case "run":  runner.Runner.run(args); break;
            case "help": printHelp();             break;
            default:
                System.out.println("Comando desconocido: " + args[0]);
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
        AsmFileWriter.write(options, content);
    }

    private static void printHelp() {
        System.out.println("");
        System.out.println("AsmBuilder - Generador de plantillas .asm (TASM/DOS)");
        System.out.println("");
        System.out.println("Comandos:");
        System.out.println("  new   Crea un nuevo archivo .asm");
        System.out.println("  run   Compila y ejecuta un .asm con TASM/TLINK/DOSBox");
        System.out.println("  help  Muestra esta ayuda");
        System.out.println("");
        System.out.println("--- new ---");
        System.out.println("  Uso: asmb new <archivo> --template [flags de plantilla] [flags de salida]");
        System.out.println("");
        System.out.println("  Flags de salida:");
        System.out.println("  --path <ruta>   Guardar en una carpeta diferente al directorio actual");
        System.out.println("  --folder        Crear subcarpeta con el nombre del programa y copiar tasm/tlink");
        System.out.println("");
        System.out.println("  Genericos:");
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
        System.out.println("  Especificos:");
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
        System.out.println("  Combinaciones:");
        System.out.println("  --for --print        Ciclo que imprime en cada iteracion");
        System.out.println("  --dowhile --print    DO-WHILE que imprime en cada iteracion");
        System.out.println("  --cursor --delay     Mover cursor, esperar y regresar al origen");
        System.out.println("  --screen --cursor    Limpiar pantalla y posicionar cursor");
        System.out.println("");
        System.out.println("--- run ---");
        System.out.println("  Uso: asmb run <archivo.asm>");
        System.out.println("  Ensambla con TASM, enlaza con TLINK y abre DOSBox automaticamente.");
        System.out.println("  Requiere tasm.exe y tlink.exe en la misma carpeta o en bin/.");
        System.out.println("  Requiere DOSBox instalado.");
        System.out.println("");
        System.out.println("Ejemplos:");
        System.out.println("  asmb new hola --template --print");
        System.out.println("  asmb new hola --template --for --print --folder");
        System.out.println("  asmb new hola --template --print --path C:\\programas");
        System.out.println("  asmb run hola.asm");
        System.out.println("");
    }
}
