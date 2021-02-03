package spotifyapp.main.util;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import org.jboss.logging.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer, 0, 1024)) > 0) {
            mem.write(buffer, 0, len);
        }
        return mem.toByteArray();
    }

    public static File createTempFile(byte[] bytes) throws IOException {
        FileUtils.createFolderIfNotExists("tmp");

        String filePath = "tmp/" + UUID.randomUUID().toString() + ".au";
        Path path = Paths.get(filePath);
        Files.createFile(path);
        Files.write(path, bytes);

        return new File(filePath);
    }

    public static void deleteFile(File audioFile) {
        try {
            audioFile.delete();
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    private static void createFolderIfNotExists(String folderPath) {
        Path path = Paths.get(folderPath);
        if (Files.notExists(path)) {
            try {
                LOGGER.info("Directory has been created at path: " + path);
                Files.createDirectories(path);
            } catch (IOException e) {
                LOGGER.error("Directory could not be created at path: " + path, e);
                e.printStackTrace();
            }
        }
    }
}
