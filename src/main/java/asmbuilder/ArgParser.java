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
            // Flags que requieren el siguiente argumento como valor
            if (args[i].equals("--path")) {
                if (i + 1 >= args.length) { System.out.println("--path requiere una ruta."); return null; }
                options.setOutputPath(args[++i]);
                continue;
            }
            if (args[i].equals("--msg")) {
                if (i + 1 >= args.length) { System.out.println("--msg requiere un texto."); return null; }
                options.setCustomMsg(args[++i]);
                continue;
            }
            if (args[i].equals("--str")) {
                if (i + 1 >= args.length) { System.out.println("--str requiere un texto."); return null; }
                options.setCustomStr(args[++i]);
                continue;
            }
            if (args[i].equals("--count")) {
                if (i + 1 >= args.length) { System.out.println("--count requiere un numero."); return null; }
                options.setLoopCount(parseInt(args[++i], "--count"));
                continue;
            }
            if (args[i].equals("--row")) {
                if (i + 1 >= args.length) { System.out.println("--row requiere un numero."); return null; }
                options.setCursorRow(parseInt(args[++i], "--row"));
                continue;
            }
            if (args[i].equals("--col")) {
                if (i + 1 >= args.length) { System.out.println("--col requiere un numero."); return null; }
                options.setCursorCol(parseInt(args[++i], "--col"));
                continue;
            }
            if (args[i].equals("--sec")) {
                if (i + 1 >= args.length) { System.out.println("--sec requiere un numero."); return null; }
                options.setDelaySecs(parseInt(args[++i], "--sec"));
                continue;
            }
            if (args[i].equals("--steps")) {
                if (i + 1 >= args.length) { System.out.println("--steps requiere un numero."); return null; }
                options.setDiagonalSteps(parseInt(args[++i], "--steps"));
                continue;
            }
            if (args[i].equals("--cycles")) {
                if (i + 1 >= args.length) { System.out.println("--cycles requiere un numero."); return null; }
                options.setRectSwapCycles(parseInt(args[++i], "--cycles"));
                continue;
            }

            switch (args[i]) {
                case "--template":  break; // indica modo plantilla, no necesita accion extra
                case "--empty":     break; // plantilla base, es el comportamiento por defecto
                case "--folder":    options.setCreateFolder(true); break;
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
                case "--diagonal":  options.setDiagonal(true);   break;
                case "--rectswap":  options.setRectSwap(true);   break;
                case "--abc":       options.setAlphabet(true);   break;
                default:
                    System.out.println("Opcion desconocida: " + args[i]);
                    return null;
            }
        }

        return options;
    }

    private static int parseInt(String value, String flag) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println(flag + " requiere un numero entero. Se uso el valor por defecto.");
            return -1;
        }
    }
}
