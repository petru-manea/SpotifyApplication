package spotifyapp.main.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import spotifyapp.main.config.GenericDAOImpl;
import spotifyapp.main.dao.ProcessedAudioDAO;
import spotifyapp.main.model.entity.PredictedTypeEntity;
import spotifyapp.main.model.entity.ProcessedAudioEntity;

import javax.persistence.TypedQuery;
import java.util.List;

@Component
public class ProcessedAudioDAOImpl extends GenericDAOImpl<ProcessedAudioEntity, Integer>
    implements ProcessedAudioDAO {

  public ProcessedAudioDAOImpl() {
    super(ProcessedAudioEntity.class);
  }

  @Override
  @Transactional
  public ProcessedAudioEntity save(ProcessedAudioEntity entity) {
    super.save(entity);
    flush();
    return entity;
  }

  @Override
  @Transactional
  public ProcessedAudioEntity update(ProcessedAudioEntity entity) {
    ProcessedAudioEntity updatedEntity = super.update(entity);
    flush();
    return updatedEntity;
  }

  @Override
  @Transactional
  public void delete(ProcessedAudioEntity entity) {
    super.delete(entity);
    flush();
  }

  @Override
  @Transactional
  public ProcessedAudioEntity findById(Integer id) {
    return super.findById(id);
  }

  @Override
  @Transactional
  public List<ProcessedAudioEntity> findAll() {
    return super.findAll();
  }

  @Override
  @Transactional
  public List<ProcessedAudioEntity> findByGenre(PredictedTypeEntity genre) {
    TypedQuery<ProcessedAudioEntity> query =
            getEntityManager().createNamedQuery(ProcessedAudioEntity.FIND_BY_GENRE, ProcessedAudioEntity.class);
    query.setParameter("genre", "%" + genre.name() + "%");
    return query.getResultList();
  }

  @Override
  @Transactional
  public void flush() {
    super.flush();
  }
}
