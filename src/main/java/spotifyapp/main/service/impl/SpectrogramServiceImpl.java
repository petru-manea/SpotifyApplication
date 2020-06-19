package spotifyapp.main.service.impl;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import spotifyapp.main.model.FFTAudioProcessor;
import spotifyapp.main.service.SpectrogramService;

import javax.imageio.ImageIO;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class SpectrogramServiceImpl implements SpectrogramService, PitchDetectionHandler {

    private static final Logger LOGGER = Logger.getLogger(SpectrogramServiceImpl.class.getName());

    private static final int WIDTH = 1366;
    private static final int HEIGHT = 96;
    private static final int BUFFER_SIZE = 1024 * 4;
    private static final int OVERLAP = 768 * 4;
    private BufferedImage bufferedImage =
            new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

    private AudioProcessor fftAudioProcessor = new FFTAudioProcessor(bufferedImage, WIDTH, HEIGHT);

    @Override
    public BufferedImage convertAudio(File audioFile)
            throws IOException, UnsupportedAudioFileException {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, BUFFER_SIZE, OVERLAP);

        dispatcher.addAudioProcessor(fftAudioProcessor);

        dispatcher.run();

        return bufferedImage;
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        File file = new File("mel_img");
        System.out.println(file.getAbsolutePath());
        if (file.isDirectory()) {
            for (File class_folder : file.listFiles()) {
                if (class_folder.isDirectory()) {
                    for (File f : class_folder.listFiles()) {
                        String file_path = f.getAbsolutePath();
                        if (file_path.endsWith("au")) {
                            System.out.println("Converting " + file_path + " ...");
                            String output_image_path = file_path + ".png";
                            File outputFile = new File(output_image_path);

                            if (outputFile.exists()) continue;
//              BufferedImage bufferedImage =
//                      new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
//              FFTAudioProcessor audioProcessor = new FFTAudioProcessor(bufferedImage, WIDTH, HEIGHT);
//              audioProcessor.init(bufferedImage, WIDTH, HEIGHT);
                            SpectrogramServiceImpl melGram = new SpectrogramServiceImpl();
                            BufferedImage image = melGram.convertAudio(f);

                            ImageIO.write(image, "png", outputFile);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {

    }
}
