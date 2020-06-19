package spotifyapp.main.dao;

import spotifyapp.main.config.GenericDAO;
import spotifyapp.main.model.entity.PredictedTypeEntity;
import spotifyapp.main.model.entity.ProcessedAudioEntity;

import java.util.List;

public interface ProcessedAudioDAO extends GenericDAO<ProcessedAudioEntity, Integer> {

    List<ProcessedAudioEntity> findByGenre(PredictedTypeEntity genre);
}
