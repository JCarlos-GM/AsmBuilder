package asmbuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

// Escribe el contenido .asm en disco, con soporte de ruta personalizada y creacion de carpeta
public class AsmFileWriter {

    public static void write(Options options, String content) {
        // Determinar directorio base: --path o directorio actual
        String base = options.getOutputPath() != null
            ? options.getOutputPath()
            : System.getProperty("user.dir");

        String baseName = options.getFileName().replace(".asm", "");
        File targetDir;

        if (options.isCreateFolder()) {
            // Crear subcarpeta con el nombre del programa
            targetDir = new File(base, baseName);
            targetDir.mkdirs();
            copyBinaries(targetDir);
        } else {
            targetDir = new File(base);
            targetDir.mkdirs(); // crea la ruta si no existe (aplica cuando se usa --path)
        }

        File asmFile = new File(targetDir, options.getFileName());

        try (FileWriter writer = new FileWriter(asmFile)) {
            writer.write(content);
            System.out.println("Archivo creado: " + asmFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    }

    // Copia tasm.exe y tlink.exe al directorio destino
    // Los busca en <ASMBUILDER_HOME>/bin/ y luego en <ASMBUILDER_HOME>/templates/
    private static void copyBinaries(File targetDir) {
        String home = System.getProperty("ASMBUILDER_HOME", "");
        String[] names = { "tasm.exe", "tlink.exe" };

        for (String name : names) {
            File src = findBinary(home, name);
            if (src == null) {
                System.out.println("Advertencia: no se encontro " + name + " en bin/ ni en templates/");
                continue;
            }
            File dest = new File(targetDir, name);
            try {
                Files.copy(src.toPath(), dest.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Copiado: " + name);
            } catch (IOException e) {
                System.out.println("Error al copiar " + name + ": " + e.getMessage());
            }
        }
    }

    private static File findBinary(String home, String name) {
        // Buscar primero en bin/, luego en templates/ (donde estan actualmente)
        File fromBin       = new File(home + "bin" + File.separator + name);
        File fromTemplates = new File(home + "templates" + File.separator + name.toUpperCase());
        File fromTemplatesLower = new File(home + "templates" + File.separator + name);

        if (fromBin.exists())            return fromBin;
        if (fromTemplates.exists())      return fromTemplates;
        if (fromTemplatesLower.exists()) return fromTemplatesLower;
        return null;
    }
}
