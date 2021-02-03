package spotifyapp.main.model.mapper;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spotifyapp.main.model.dto.ProcessedAudioDTO;
import spotifyapp.main.model.dto.ProcessedAudioTypeDTO;
import spotifyapp.main.model.entity.ProcessedAudioEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProcessedAudioMapper {

  private static final Logger LOGGER = Logger.getLogger(ProcessedAudioMapper.class.getName());

  private final PredictedTypeMapper predictedTypeMapper;

  @Autowired
  public ProcessedAudioMapper(PredictedTypeMapper predictedTypeMapper) {
    this.predictedTypeMapper = predictedTypeMapper;
  }

  public List<ProcessedAudioDTO> mapEntitiesToDtos(List<ProcessedAudioEntity> entities) {
    if (entities == null) {
      return null;
    }

    List<ProcessedAudioDTO> dtos = new ArrayList<>();
    for (ProcessedAudioEntity entity : entities) {
      dtos.add(mapEntityToDto(entity));
    }
    return dtos;
  }

  public List<ProcessedAudioEntity> mapDtosToEntities(List<ProcessedAudioDTO> dtos) {
    if (dtos == null) {
      return null;
    }
    List<ProcessedAudioEntity> entities = new ArrayList<>();

    for (ProcessedAudioDTO dto : dtos) {
      entities.add(mapDtoToEntity(dto));
    }
    return entities;
  }

  public ProcessedAudioDTO mapEntityToDto(ProcessedAudioEntity entity) {
    if (entity == null) {
      return null;
    }

    ProcessedAudioDTO dto = new ProcessedAudioDTO();
    dto.setId(entity.getId());
    dto.setFilename(entity.getFilename());
    dto.setFileType(entity.getFileType());
    dto.setDescription(entity.getDescription());
    dto.setSuccess(entity.getSuccess());
    dto.setMainType(
        new ProcessedAudioTypeDTO(
            predictedTypeMapper.mapEntityToDto(entity.getMainPredictedType()),
            entity.getMainPredictedValue()));
    if (entity.getSubPredictedType() != null) {
      dto.setSubType(
          new ProcessedAudioTypeDTO(
              predictedTypeMapper.mapEntityToDto(entity.getSubPredictedType()),
              entity.getSubPredictedValue()));
    }

    return dto;
  }

  public ProcessedAudioEntity mapDtoToEntity(ProcessedAudioDTO dto) {
    if (dto == null) {
      return null;
    }

    ProcessedAudioEntity entity = new ProcessedAudioEntity();
    entity.setId(dto.getId());
    entity.setFilename(dto.getFilename());
    entity.setFileType(dto.getFileType());
    entity.setDescription(dto.getDescription());
    try {
      entity.setFile(dto.getFile().getBytes());
    } catch (IOException e) {
      LOGGER.error("Could not convert from DTO to Entity: " + e.getMessage());
    }
    entity.setSuccess(dto.getSuccess());
    entity.setMainPredictedType(
        predictedTypeMapper.mapDtoToEntity(dto.getMainType().getPredictedType()));
    entity.setMainPredictedValue(dto.getMainType().getPredictionValue());
    if (dto.getSubType() != null) {
      entity.setSubPredictedType(
          predictedTypeMapper.mapDtoToEntity(dto.getSubType().getPredictedType()));
      entity.setSubPredictedValue(dto.getSubType().getPredictionValue());
    }

    return entity;
  }
}
