package runner;

import java.io.File;

// Orquesta la compilacion y ejecucion rapida de un .asm con TASM/TLINK/DOSBox
public class Runner {

    public static void run(String[] args) {
        // args[0] = "run"
        if (args.length < 2) {
            System.out.println("Uso: asmb run <archivo.asm>");
            System.out.println("Ejemplo: asmb run hola.asm");
            return;
        }

        String fileName = args[1];
        if (!fileName.endsWith(".asm")) fileName += ".asm";

        File asmFile = new File(System.getProperty("user.dir"), fileName);

        if (!asmFile.exists()) {
            System.out.println("Archivo no encontrado: " + asmFile.getAbsolutePath());
            return;
        }

        System.out.println("Archivo: " + asmFile.getAbsolutePath());
        System.out.println("");

        boolean ok = Compiler.compile(asmFile);
        if (!ok) return;

        System.out.println("");

        String exeName = fileName.replace(".asm", ".exe");
        File exeFile = new File(asmFile.getParentFile(), exeName);

        DosBoxLauncher.launch(exeFile);
    }
}
