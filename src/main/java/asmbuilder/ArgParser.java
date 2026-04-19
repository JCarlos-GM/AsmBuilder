package asmbuilder;

// Interpreta los argumentos de la terminal y llena un objeto Options
public class ArgParser {

    public static Options parse(String[] args) {
        // args[0] = "new", empezamos desde [1]
        Options options = new Options();
        int i = 1;

        // Si el primer argumento no empieza con -- es el nombre del archivo
        if (i < args.length && !args[i].startsWith("--")) {
            String name = args[i];
            if (!name.endsWith(".asm")) {
                name = name + ".asm";
            }
            options.setFileName(name);
            i++;
        }

        for (; i < args.length; i++) {
            switch (args[i]) {
                case "--template":  break; // indica modo plantilla, no necesita accion extra
                case "--empty":     break; // plantilla base, es el comportamiento por defecto
                case "--print":     options.setPrint(true);      break;
                case "--for":       options.setForLoop(true);    break;
                case "--dowhile":   options.setDoWhile(true);    break;
                case "--if":        options.setIfCond(true);     break;
                case "--switch":    options.setSwitchCond(true); break;
                case "--vars":      options.setVars(true);       break;
                case "--while":     options.setWhileLoop(true);  break;
                case "--array":     options.setArray(true);      break;
                case "--cursor":    options.setCursor(true);     break;
                case "--screen":    options.setScreen(true);     break;
                case "--color":     options.setColor(true);      break;
                case "--delay":     options.setDelay(true);      break;
                case "--stack":     options.setStack(true);      break;
                case "--string":    options.setString(true);     break;
                case "--upper":     options.setUpper(true);      break;
                case "--input":     options.setInput(true);      break;
                case "--rect":      options.setRect(true);       break;
                default:
                    System.out.println("Opcion desconocida: " + args[i]);
                    return null;
            }
        }

        return options;
    }
}
