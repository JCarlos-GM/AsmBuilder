package runner;

import java.io.File;

// Busca DOSBox en el sistema y lanza el .exe dentro del emulador
public class DosBoxLauncher {

    // Rutas comunes de instalacion de DOSBox en Windows
    private static final String[] COMMON_PATHS = {
        "C:\\Program Files (x86)\\DOSBox-0.74\\dosbox.exe",
        "C:\\Program Files (x86)\\DOSBox-0.74-3\\dosbox.exe",
        "C:\\Program Files (x86)\\DOSBox-0.74.3\\dosbox.exe",
        "C:\\Program Files\\DOSBox-0.74\\dosbox.exe",
        "C:\\Program Files\\DOSBox-0.74-3\\dosbox.exe",
        "C:\\Program Files\\DOSBox-0.74.3\\dosbox.exe"
    };

    public static void launch(File exeFile) {
        String dosbox = findDosBox();

        if (dosbox == null) {
            System.out.println("No se encontro DOSBox.");
            System.out.println("Instala DOSBox o agregalo al PATH del sistema.");
            System.out.println("Descarga: https://www.dosbox.com/download.php?main=1");
            return;
        }

        String folder = exeFile.getParentFile().getAbsolutePath();
        String exeName = exeFile.getName();

        System.out.println("Lanzando DOSBox...");

        try {
            // Monta la carpeta del .exe como unidad C: y lo ejecuta
            // pause al final para que el usuario pueda ver la salida antes de cerrar
            new ProcessBuilder(
                dosbox,
                "-c", "mount c " + folder,
                "-c", "c:",
                "-c", exeName,
                "-c", "pause"
            ).start();
        } catch (Exception e) {
            System.out.println("Error al lanzar DOSBox: " + e.getMessage());
        }
    }

    private static String findDosBox() {
        // Buscar en rutas conocidas
        for (String path : COMMON_PATHS) {
            if (new File(path).exists()) return path;
        }

        // Intentar con "dosbox" en el PATH del sistema
        try {
            int code = new ProcessBuilder("dosbox", "--version")
                .start().waitFor();
            if (code == 0) return "dosbox";
        } catch (Exception ignored) {}

        return null;
    }
}
