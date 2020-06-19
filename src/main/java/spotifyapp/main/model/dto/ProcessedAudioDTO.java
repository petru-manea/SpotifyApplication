package spotifyapp.main.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class ProcessedAudioDTO implements Serializable {

    private Integer id;

    private String description;

    private String filename;

    private MultipartFile file;

    private ProcessedAudioTypeDTO mainType;

    private ProcessedAudioTypeDTO subType;

    private Boolean success;
}
