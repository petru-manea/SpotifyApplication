package spotifyapp.main.service.impl;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.dto.ProcessedAudioTypeDTO;
import spotifyapp.main.service.ImageEncoderService;
import spotifyapp.main.service.ImagePredictorService;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImagePredictorServiceImpl implements ImagePredictorService {

  private final String[] predictionTypes =
      new String[] {
        "blues", "classical", "country", "disco", "hiphop", "jazz", "metal", "pop", "reggae", "rock"
      };

  private static final Logger LOGGER = Logger.getLogger(ImagePredictorServiceImpl.class.getName());

  private static final int WIDTH = 1366;
  private static final int HEIGHT = 96;

  private final ImageEncoderService imageEncoderService;

  @Autowired
  public ImagePredictorServiceImpl(ImageEncoderService imageEncoderService) {
    this.imageEncoderService = imageEncoderService;
  }

  @Override
  public List<ProcessedAudioTypeDTO> predictImage(BufferedImage image) {
    float[] predictedValues = imageEncoderService.encodeImage(image, WIDTH, HEIGHT);
    List<ProcessedAudioTypeDTO> types = new ArrayList<>();
    if (predictedValues != null) {
      Map<String, Float> predictionMap = new HashMap<>();
      for (int i = 0; i < predictedValues.length; ++i) {
        predictionMap.put(predictionTypes[i], predictedValues[i]);
      }
      ProcessedAudioTypeDTO mainType = getMainType(predictionMap);
      if (mainType != null) {
        types.add(mainType);
        ProcessedAudioTypeDTO subType = getSubType(predictionMap, mainType.getPredictionValue());
        if (subType != null) {
          types.add(subType);
        }
      } else {
        LOGGER.error("Predicted is null!");
      }

    } else {
      LOGGER.error("Predicted is null!");
    }
    return types;
  }

  private ProcessedAudioTypeDTO getMainType(Map<String, Float> predictionMap) {
    Map.Entry<String, Float> maxPrediction = null;
    for (Map.Entry<String, Float> entry : predictionMap.entrySet()) {
      if (maxPrediction == null
          || (entry.getValue().compareTo(maxPrediction.getValue()) > 0
              && entry.getValue() > 0
              && entry.getValue() <= 1)) {
        maxPrediction = entry;
      }
    }
    if (maxPrediction != null) {
      return new ProcessedAudioTypeDTO(
          PredictedTypeDTO.get(maxPrediction.getKey()), maxPrediction.getValue());
    }
    return null;
  }

  private ProcessedAudioTypeDTO getSubType(Map<String, Float> predictionMap, Float maxPrediction) {
    Map.Entry<String, Float> secondMaxPrediction = null;
    for (Map.Entry<String, Float> entry : predictionMap.entrySet()) {
      if (secondMaxPrediction == null
          || (entry.getValue().compareTo(secondMaxPrediction.getValue()) > 0
              && entry.getValue() > 0
              && entry.getValue() <= 1
              && entry.getValue() < maxPrediction)) {
        secondMaxPrediction = entry;
      }
    }
    if (secondMaxPrediction != null) {
      return new ProcessedAudioTypeDTO(
          PredictedTypeDTO.get(secondMaxPrediction.getKey()), secondMaxPrediction.getValue());
    }
    return null;
  }
}
