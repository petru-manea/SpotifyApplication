package spotifyapp.main.service;

import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.dto.ProcessedAudioDTO;

import java.util.List;

public interface ProcessedAudioService {

    ProcessedAudioDTO saveProcessedAudio(ProcessedAudioDTO processedAudioDTO);

    void updateProcessedAudio(ProcessedAudioDTO processedAudioDTO);

    void deleteProcessedAudio(ProcessedAudioDTO processedAudioDTO);

    List<ProcessedAudioDTO> getProcessedAudios();

    ProcessedAudioDTO getProcessedAudioById(String id);

    List<ProcessedAudioDTO> getProcessedAudiosByGenre(PredictedTypeDTO genre);
}
