package runner;

import java.io.File;

// Ejecuta tasm.exe y tlink.exe sobre un archivo .asm para producir el .exe
public class Compiler {

    public static boolean compile(File asmFile) {
        File dir      = asmFile.getParentFile();
        String name   = asmFile.getName();
        String base   = name.replace(".asm", "");

        File tasm  = findBinary(dir, "tasm.exe");
        File tlink = findBinary(dir, "tlink.exe");

        if (tasm == null) {
            System.out.println("No se encontro tasm.exe en " + dir + " ni en bin/");
            return false;
        }
        if (tlink == null) {
            System.out.println("No se encontro tlink.exe en " + dir + " ni en bin/");
            return false;
        }

        System.out.println("Ensamblando con TASM...");
        if (!run(dir, tasm.getAbsolutePath(), name)) {
            System.out.println("Error al ensamblar. Revisa los errores de TASM.");
            return false;
        }

        System.out.println("Enlazando con TLINK...");
        if (!run(dir, tlink.getAbsolutePath(), base + ".obj")) {
            System.out.println("Error al enlazar. Revisa los errores de TLINK.");
            return false;
        }

        System.out.println("Compilacion exitosa: " + base + ".exe");
        return true;
    }

    // Busca el ejecutable en el directorio del .asm y luego en <ASMBUILDER_HOME>/bin/
    private static File findBinary(File dir, String name) {
        File local = new File(dir, name);
        if (local.exists()) return local;

        String home = System.getProperty("ASMBUILDER_HOME", "");
        File fromBin = new File(home + "bin" + File.separator + name);
        if (fromBin.exists()) return fromBin;

        // Tambien busca en templates/ por si estan ahi todavia
        File fromTemplates = new File(home + "templates" + File.separator + name.toUpperCase());
        if (fromTemplates.exists()) return fromTemplates;

        return null;
    }

    private static boolean run(File dir, String... command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(dir);
            pb.inheritIO();
            return pb.start().waitFor() == 0;
        } catch (Exception e) {
            System.out.println("Error al ejecutar proceso: " + e.getMessage());
            return false;
        }
    }
}
