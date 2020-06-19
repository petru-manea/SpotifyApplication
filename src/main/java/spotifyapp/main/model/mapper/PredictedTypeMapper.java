package spotifyapp.main.model.mapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.entity.PredictedTypeEntity;

@Component
public class PredictedTypeMapper {

    private static final Logger LOGGER = Logger.getLogger(PredictedTypeMapper.class.getName());

    private static final BiMap<PredictedTypeEntity, PredictedTypeDTO> PREDICTED_TYPE_MAPPER =
            new ImmutableBiMap.Builder<PredictedTypeEntity, PredictedTypeDTO>()
                    .put(PredictedTypeEntity.BLUES, PredictedTypeDTO.BLUES)
                    .put(PredictedTypeEntity.CLASSICAL, PredictedTypeDTO.CLASSICAL)
                    .put(PredictedTypeEntity.COUNTRY, PredictedTypeDTO.COUNTRY)
                    .put(PredictedTypeEntity.DISCO, PredictedTypeDTO.DISCO)
                    .put(PredictedTypeEntity.HIPHOP, PredictedTypeDTO.HIPHOP)
                    .put(PredictedTypeEntity.JAZZ, PredictedTypeDTO.JAZZ)
                    .put(PredictedTypeEntity.METAL, PredictedTypeDTO.METAL)
                    .put(PredictedTypeEntity.POP, PredictedTypeDTO.POP)
                    .put(PredictedTypeEntity.REGGAE, PredictedTypeDTO.REGGAE)
                    .put(PredictedTypeEntity.ROCK, PredictedTypeDTO.ROCK)
                    .put(PredictedTypeEntity.UNKNOWN, PredictedTypeDTO.UNKNOWN)
                    .build();

    public PredictedTypeDTO mapEntityToDto(PredictedTypeEntity entity) {
        if (entity == null) {
            return null;
        }

        PredictedTypeDTO dto = PREDICTED_TYPE_MAPPER.get(entity);

        if (dto == null) {
            LOGGER.error("Unknown predicted type [" + entity.name() + "]");
            throw new IllegalArgumentException("Unknown predicted type [" + entity.name() + "]");
        }

        return dto;
    }

    public PredictedTypeEntity mapDtoToEntity(PredictedTypeDTO dto) {
        if (dto == null) {
            return null;
        }

        PredictedTypeEntity entity = PREDICTED_TYPE_MAPPER.inverse().get(dto);

        if (entity == null) {
            LOGGER.error("Unknown predicted type [" + dto.name() + "]");
            throw new IllegalArgumentException("Unknown predicted type [" + dto.name() + "]");
        }

        return entity;
    }
}
