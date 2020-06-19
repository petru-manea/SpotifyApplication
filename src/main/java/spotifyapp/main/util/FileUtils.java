package spotifyapp.main.util;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    public static File convertToWave(File file) {
        if (!file.getPath().toLowerCase().endsWith(".wav")) {
            LOGGER.info("converting " + file.getName() + " to wav file");

            File temp = null;
            try {
                temp = File.createTempFile(file.getName().split("\\.")[0], ".wav");
                temp.deleteOnExit();
                Converter converter = new Converter();
                converter.convert(file.getAbsolutePath(), temp.getAbsolutePath());
            } catch (IOException | JavaLayerException e) {
                LOGGER.error(e.getMessage());
            }
            if (temp != null) {
                LOGGER.info("successfully convert " + file.getName() + " to " + temp.getName());
            }
            return temp;
        } else {
            return file;
        }
    }

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
        FileUtils.createFolderIfNotExists("/tmp");

        String filePath = "/tmp/" + UUID.randomUUID().toString() + ".au";
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(bytes);
        fos.close();

        return new File(filePath);
    }

    public static void deleteFile(File audioFile) {
        try {
            audioFile.delete();
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    public static File storeUploadedFile(byte[] bytes) throws IOException {
        FileUtils.createFolderIfNotExists("uploaded_audio_samples");

        String filePath = "uploaded_audio_samples/" + UUID.randomUUID().toString() + ".au";
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(bytes);
        fos.close();

        return new File(filePath);
    }

    private static void createFolderIfNotExists(String folderPath) {
        File f = new File(folderPath);
        if (f.exists()) {
            f.mkdir();
        }
    }
}
