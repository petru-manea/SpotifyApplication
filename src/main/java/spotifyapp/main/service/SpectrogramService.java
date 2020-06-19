package spotifyapp.main.service;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface SpectrogramService {

    BufferedImage convertAudio(File audioFile)
            throws IOException, UnsupportedAudioFileException, ExecutionException, InterruptedException;
}
