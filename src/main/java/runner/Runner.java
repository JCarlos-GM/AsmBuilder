package runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Orquesta la compilacion y ejecucion rapida de un .asm con TASM/TLINK/DOSBox
public class Runner {

    public static void run(String[] args) {
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

        File dir  = asmFile.getParentFile();
        String base = fileName.replace(".asm", "");

        File tasm  = Compiler.findBinary(dir, "tasm.exe");
        File tlink = Compiler.findBinary(dir, "tlink.exe");

        if (tasm == null) {
            System.out.println("No se encontro tasm.exe en " + dir + " ni en bin/");
            return;
        }
        if (tlink == null) {
            System.out.println("No se encontro tlink.exe en " + dir + " ni en bin/");
            return;
        }

        String dosbox = DosBoxLauncher.findDosBox();
        if (dosbox == null) {
            System.out.println("No se encontro DOSBox.");
            System.out.println("Instala DOSBox o agregalo al PATH del sistema.");
            return;
        }

        System.out.println("Archivo: " + asmFile.getAbsolutePath());
        System.out.println("Lanzando DOSBox (compilando y ejecutando)...");

        List<String> cmd = new ArrayList<>();
        cmd.add(dosbox);

        // Montar carpeta del .asm como unidad C:
        cmd.add("-c"); cmd.add("mount c " + dir.getAbsolutePath());

        String tasmCmd, tlinkCmd;
        String tasmDir = tasm.getParentFile().getAbsolutePath();

        if (tasmDir.equalsIgnoreCase(dir.getAbsolutePath())) {
            // tasm/tlink estan en la misma carpeta que el .asm
            tasmCmd  = "tasm " + base;
            tlinkCmd = "tlink " + base;
        } else {
            // tasm/tlink estan en otra carpeta (ej. bin/); montarla como unidad B:
            cmd.add("-c"); cmd.add("mount b " + tasmDir);
            tasmCmd  = "b:\\tasm " + base;
            tlinkCmd = "b:\\tlink " + base;
        }

        cmd.add("-c"); cmd.add("c:");
        cmd.add("-c"); cmd.add(tasmCmd);
        cmd.add("-c"); cmd.add(tlinkCmd);
        cmd.add("-c"); cmd.add(base + ".exe");
        cmd.add("-c"); cmd.add("pause");

        try {
            new ProcessBuilder(cmd).start();
        } catch (Exception e) {
            System.out.println("Error al lanzar DOSBox: " + e.getMessage());
        }
    }
}
