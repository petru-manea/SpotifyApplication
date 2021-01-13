package spotifyapp.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spotifyapp.main.dao.ProcessedAudioDAO;
import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.dto.ProcessedAudioDTO;
import spotifyapp.main.model.entity.ProcessedAudioEntity;
import spotifyapp.main.model.mapper.PredictedTypeMapper;
import spotifyapp.main.model.mapper.ProcessedAudioMapper;
import spotifyapp.main.service.ProcessedAudioService;

import java.util.List;

@Service
public class ProcessedAudioServiceImpl implements ProcessedAudioService {

  private static final String PROCESSED_AUDIO_IS_NULL_EXCEPTION = "Processed audio is null!";

  private final ProcessedAudioDAO processedAudioDAO;
  private final ProcessedAudioMapper processedAudioMapper;
  private final PredictedTypeMapper predictedTypeMapper;

  @Autowired
  public ProcessedAudioServiceImpl(
      ProcessedAudioDAO processedAudioDAO,
      ProcessedAudioMapper processedAudioMapper,
      PredictedTypeMapper predictedTypeMapper) {
    this.processedAudioDAO = processedAudioDAO;
    this.processedAudioMapper = processedAudioMapper;
    this.predictedTypeMapper = predictedTypeMapper;
  }

  @Override
  public ProcessedAudioDTO saveProcessedAudio(ProcessedAudioDTO processedAudioDTO) {
    if (processedAudioDTO != null) {
      ProcessedAudioEntity savedProcessedAudio =
          processedAudioDAO.save(processedAudioMapper.mapDtoToEntity(processedAudioDTO));
      return processedAudioMapper.mapEntityToDto(savedProcessedAudio);
    } else {
      throw new IllegalArgumentException(PROCESSED_AUDIO_IS_NULL_EXCEPTION);
    }
  }

  @Override
  public void updateProcessedAudio(ProcessedAudioDTO processedAudioDTO) {
    if (processedAudioDTO != null) {
      processedAudioDAO.update(processedAudioMapper.mapDtoToEntity(processedAudioDTO));
    } else {
      throw new IllegalArgumentException(PROCESSED_AUDIO_IS_NULL_EXCEPTION);
    }
  }

  @Override
  public void deleteProcessedAudio(ProcessedAudioDTO processedAudioDTO) {
    if (processedAudioDTO != null) {
      processedAudioDAO.delete(processedAudioMapper.mapDtoToEntity(processedAudioDTO));
    } else {
      throw new IllegalArgumentException(PROCESSED_AUDIO_IS_NULL_EXCEPTION);
    }
  }

  @Override
  public List<ProcessedAudioDTO> getProcessedAudios() {
    return processedAudioMapper.mapEntitiesToDtos(processedAudioDAO.findAll());
  }

  @Override
  public ProcessedAudioDTO getProcessedAudioById(String id) {
    return processedAudioMapper.mapEntityToDto(processedAudioDAO.findById(Integer.parseInt(id)));
  }

  @Override
  public List<ProcessedAudioDTO> getProcessedAudiosByGenre(PredictedTypeDTO genre) {
    return processedAudioMapper.mapEntitiesToDtos(
        processedAudioDAO.findByGenre(predictedTypeMapper.mapDtoToEntity(genre)));
  }

  @Override
  public byte[] getProcessedAudioFileById(String id) {
    return processedAudioDAO.findById(Integer.parseInt(id)).getFile();
  }
}
