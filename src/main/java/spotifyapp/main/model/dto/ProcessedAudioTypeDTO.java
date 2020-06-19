package spotifyapp.main.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProcessedAudioTypeDTO implements Serializable {

    private PredictedTypeDTO predictedType;

    private Float predictionValue;

}
