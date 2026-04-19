package asmbuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

// Escribe el contenido .asm en un archivo en el directorio actual
public class AsmFileWriter {

    public static void write(String fileName, String content) {
        // Guardamos en la carpeta desde donde se ejecuta el comando
        String path = Paths.get(System.getProperty("user.dir"), fileName).toString();

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
            System.out.println("Archivo creado: " + path);
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    }
}
