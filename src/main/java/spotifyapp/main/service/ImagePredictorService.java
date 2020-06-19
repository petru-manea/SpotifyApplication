package spotifyapp.main.service;

import spotifyapp.main.model.dto.ProcessedAudioTypeDTO;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ImagePredictorService {

    List<ProcessedAudioTypeDTO> predictImage(BufferedImage image);

}
