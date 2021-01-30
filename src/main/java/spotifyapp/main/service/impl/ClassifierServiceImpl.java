package spotifyapp.main.service.impl;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotifyapp.main.model.dto.ProcessedAudioTypeDTO;
import spotifyapp.main.service.ClassifierService;
import spotifyapp.main.service.ImagePredictorService;
import spotifyapp.main.service.SpectrogramService;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ClassifierServiceImpl implements ClassifierService {

  private static final Logger LOGGER = Logger.getLogger(ClassifierServiceImpl.class.getName());

  private final ImagePredictorService imagePredictorService;
  private final SpectrogramService spectrogramService;

  @Autowired
  public ClassifierServiceImpl(
      ImagePredictorService imagePredictorService, SpectrogramService spectrogramService) {
    this.imagePredictorService = imagePredictorService;
    this.spectrogramService = spectrogramService;
  }

  @Override
  public List<ProcessedAudioTypeDTO> convertAndPredict(File audioFile) {
    BufferedImage image;
    try {
      image = spectrogramService.convertAudio(audioFile);
      if (image != null) {
        return imagePredictorService.predictImage(image);
      }
    } catch (IOException
        | InterruptedException
        | ExecutionException
        | UnsupportedAudioFileException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.error("Image or Predict is null!");
    return null;
  }
}
