package spotifyapp.main.service;

import spotifyapp.main.model.dto.ProcessedAudioTypeDTO;

import java.io.File;
import java.util.List;

public interface ClassifierService {

    List<ProcessedAudioTypeDTO> convertAndPredict(File audioFile);

}
