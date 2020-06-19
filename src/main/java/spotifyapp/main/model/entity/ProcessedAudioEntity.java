package spotifyapp.main.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import spotifyapp.main.config.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "PROCESSED_AUDIO")
@NamedQueries({
  @NamedQuery(
      name = ProcessedAudioEntity.FIND_BY_GENRE,
      query =
          "SELECT processedAudio FROM "
              + "ProcessedAudioEntity processedAudio "
              + " WHERE processedAudio.mainPredictedType LIKE :genre OR processedAudio.subPredictedType LIKE "
              + ":genre")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessedAudioEntity extends BaseEntity {

  public static final String FIND_BY_GENRE = "ProductEntity.findByName";

  private static final long serialVersionUID = 1L;

  @Enumerated(EnumType.STRING)
  @Column(name = "MAIN_TYPE_LABEL", nullable = false)
  private PredictedTypeEntity mainPredictedType;

  @Column(name = "MAIN_TYPE_VALUE", nullable = false)
  private Float mainPredictedValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "SUB_TYPE_LABEL", nullable = false)
  private PredictedTypeEntity subPredictedType;

  @Column(name = "SUB_TYPE_VALUE", nullable = false)
  private Float subPredictedValue;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @Column(name = "FILENAME", nullable = false)
  private String filename;

  @Column(name = "SUCCESS", nullable = false)
  private Boolean success;
}
