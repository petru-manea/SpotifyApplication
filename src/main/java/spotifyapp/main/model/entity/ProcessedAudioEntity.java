package spotifyapp.main.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import spotifyapp.main.config.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
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
              + " WHERE processedAudio.mainPredictedType = :genre OR processedAudio.subPredictedType = "
              + ":genre")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessedAudioEntity extends BaseEntity {

  public static final String FIND_BY_GENRE = "ProcessedAudioEntity.findByGenre";

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

  @Lob
  @Column(name = "FILE", nullable = false)
  private byte[] file;

  @Column(name = "FILE_TYPE", nullable = false)
  private String fileType;

  @Column(name = "SUCCESS", nullable = false)
  private Boolean success;
}
