package spotifyapp.main.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spotifyapp.main.config.ResponseWrapper;
import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.dto.ProcessedAudioDTO;
import spotifyapp.main.model.dto.ProcessedAudioTypeDTO;
import spotifyapp.main.service.ClassifierService;
import spotifyapp.main.service.ProcessedAudioService;
import spotifyapp.main.util.FileUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(path = "/api/spotify")
public class ClassifierController {

    private static final Logger LOGGER = Logger.getLogger(ClassifierController.class.getName());

    private final ClassifierService classifierService;
    private final ProcessedAudioService processedAudioService;

    @Autowired
    public ClassifierController(
            ClassifierService classifierService, ProcessedAudioService processedAudioService) {
        this.classifierService = classifierService;
        this.processedAudioService = processedAudioService;
    }

    @PostMapping(path = "/classify",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody
    ResponseWrapper<ProcessedAudioDTO> classifySong(
            @RequestPart("processedAudioDto") ProcessedAudioDTO processedAudioDTO,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest httpRequest) {

        processedAudioDTO.setFile(file);
        String urlParams =
                "/classify?"
                        + "&description="
                        + processedAudioDTO.getDescription()
                        + "&filename="
                        + processedAudioDTO.getFilename()
                        + "&file="
                        + processedAudioDTO.getFile();

        ResponseWrapper<ProcessedAudioDTO> responseWrapper = new ResponseWrapper<>();

        ProcessedAudioDTO result = new ProcessedAudioDTO();
        result.setDescription(processedAudioDTO.getDescription());
        result.setFilename(processedAudioDTO.getFilename());
        result.setFile(file);
        result.setFileType(file.getContentType());

        try {
            byte[] bytes = processedAudioDTO.getFile().getBytes();
            LOGGER.info("audio bytes received: " + bytes.length);

            File audioFile = FileUtils.createTempFile(bytes);

            List<ProcessedAudioTypeDTO> predictedTypes = classifierService.convertAndPredict(audioFile);

            if (predictedTypes != null && !predictedTypes.isEmpty()) {
                LOGGER.info("Main Type: " + predictedTypes.get(0));
                result.setMainType(predictedTypes.get(0));
                if (predictedTypes.size() == 2) {
                    LOGGER.info("Sub Type: " + predictedTypes.get(1));
                    result.setSubType(predictedTypes.get(1));
                }
            } else {
                LOGGER.error("Failed to process the uploaded image!");
                result.setMainType(new ProcessedAudioTypeDTO(PredictedTypeDTO.UNKNOWN, Float.MIN_VALUE));
            }
            FileUtils.deleteFile(audioFile);
            result.setSuccess(true);

            ProcessedAudioDTO savedProcessedAudio = processedAudioService.saveProcessedAudio(result);

            responseWrapper.setData(savedProcessedAudio);
            responseWrapper.setStatus(HttpStatus.OK);
            responseWrapper.setUrlParams(urlParams);

        } catch (IOException ex) {
            LOGGER.error("Failed to process the uploaded image", ex);
            result.setMainType(new ProcessedAudioTypeDTO(PredictedTypeDTO.UNKNOWN, Float.MIN_VALUE));

            result.setSuccess(false);

            responseWrapper.setData(result);
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseWrapper.setUrlParams(urlParams);
            responseWrapper.setError("Error: " + ex.getMessage());
        }

        return responseWrapper;
    }

    @PostMapping(path = "/classifyTest", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseWrapper<ProcessedAudioDTO> classifySong(
            @RequestParam("description") String description,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest httpRequest) {

        String urlParams =
                "/classify?" + "&description=" + description + "&filename=" + filename + "&file=" + file;

        ResponseWrapper<ProcessedAudioDTO> responseWrapper = new ResponseWrapper<>();

        ProcessedAudioDTO result = new ProcessedAudioDTO();
        result.setDescription(description);
        result.setFilename(filename);
        result.setFileType(file.getContentType());
        result.setFile(file);

        try {
            byte[] bytes = file.getBytes();
            LOGGER.info("audio bytes received: " + bytes.length);

            File audioFile = FileUtils.createTempFile(bytes);

            List<ProcessedAudioTypeDTO> predictedTypes = classifierService.convertAndPredict(audioFile);

            if (predictedTypes != null && !predictedTypes.isEmpty()) {
                LOGGER.info("Main Type: " + predictedTypes.get(0));
                result.setMainType(predictedTypes.get(0));
                if (predictedTypes.size() == 2) {
                    LOGGER.info("Sub Type: " + predictedTypes.get(1));
                    result.setSubType(predictedTypes.get(1));
                }
            } else {
                LOGGER.error("Failed to process the uploaded image!");
                result.setMainType(new ProcessedAudioTypeDTO(PredictedTypeDTO.UNKNOWN, Float.MIN_VALUE));
            }

            FileUtils.deleteFile(audioFile);

            result.setSuccess(true);

            ProcessedAudioDTO savedProcessedAudio = processedAudioService.saveProcessedAudio(result);

            responseWrapper.setData(savedProcessedAudio);
            responseWrapper.setStatus(HttpStatus.OK);
            responseWrapper.setUrlParams(urlParams);

        } catch (IOException ex) {
            LOGGER.error("Failed to process the uploaded image", ex);

            result.setMainType(new ProcessedAudioTypeDTO(PredictedTypeDTO.UNKNOWN, Float.MIN_VALUE));

            result.setSuccess(false);

            responseWrapper.setData(result);
            responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseWrapper.setUrlParams(urlParams);
            responseWrapper.setError("Error: " + ex.getMessage());
        }

        return responseWrapper;
    }
}
