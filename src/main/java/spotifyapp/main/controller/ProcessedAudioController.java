package spotifyapp.main.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spotifyapp.main.config.ResponseWrapper;
import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.dto.ProcessedAudioDTO;
import spotifyapp.main.service.ProcessedAudioService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(path = "/api/spotify")
public class ProcessedAudioController {

  private static final Logger LOGGER = Logger.getLogger(ProcessedAudioController.class.getName());

  private final ProcessedAudioService processedAudioService;

  @Autowired
  public ProcessedAudioController(ProcessedAudioService processedAudioService) {
    this.processedAudioService = processedAudioService;
  }

  @GetMapping(path = "/song", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseWrapper<List<ProcessedAudioDTO>> getAllSongs(
      HttpServletRequest httpRequest) {

    String urlParams = "/song";

    ResponseWrapper<List<ProcessedAudioDTO>> responseWrapper = new ResponseWrapper<>();

    List<ProcessedAudioDTO> result = processedAudioService.getProcessedAudios();

    if (result == null) {
      LOGGER.error("Error: Songs could not be retrieved!");
      responseWrapper.setData(null);
      responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      responseWrapper.setUrlParams(urlParams);
      responseWrapper.setError("Error: Songs could not be retrieved!");
    } else {
      if (result.isEmpty()) {
        LOGGER.info("Error: No songs found!");
        responseWrapper.setData(result);
        responseWrapper.setStatus(HttpStatus.NOT_FOUND);
        responseWrapper.setUrlParams(urlParams);
        responseWrapper.setError("Error: No songs found!");
      } else {
        LOGGER.info("Result: " + result.toString());
        responseWrapper.setData(result);
        responseWrapper.setStatus(HttpStatus.OK);
        responseWrapper.setUrlParams(urlParams);
      }
    }
    return responseWrapper;
  }

  @GetMapping(path = "/song/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseWrapper<ProcessedAudioDTO> getSongById(
          @PathVariable("id") String id, HttpServletRequest httpRequest) {

    String urlParams = "/song?" + "id=" + id;

    ResponseWrapper<ProcessedAudioDTO> responseWrapper = new ResponseWrapper<>();

    ProcessedAudioDTO result = processedAudioService.getProcessedAudioById(id);

    if (result == null) {
      LOGGER.error("Error: Song not found!");
      responseWrapper.setData(null);
      responseWrapper.setStatus(HttpStatus.NOT_FOUND);
      responseWrapper.setUrlParams(urlParams);
      responseWrapper.setError("Error: Song not found!");
    } else {
      LOGGER.info("Result: " + result.toString());
      responseWrapper.setData(result);
      responseWrapper.setStatus(HttpStatus.OK);
      responseWrapper.setUrlParams(urlParams);
    }
    return responseWrapper;
  }

  @GetMapping(path = "/song/{genre}", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseWrapper<List<ProcessedAudioDTO>> getSongByGenre(
          @PathVariable("genre") String genre, HttpServletRequest httpRequest) {

    String urlParams = "/song?" + "genre=" + genre;

    ResponseWrapper<List<ProcessedAudioDTO>> responseWrapper = new ResponseWrapper<>();

    PredictedTypeDTO predictedTypeDTO = PredictedTypeDTO.get(genre);

    if(predictedTypeDTO == null){
      LOGGER.error("Error: No genre of type + " + genre + "!");
      responseWrapper.setData(null);
      responseWrapper.setStatus(HttpStatus.BAD_REQUEST);
      responseWrapper.setUrlParams(urlParams);
      responseWrapper.setError("Error: No genre of type + " + genre + "!");
    } else {
      List<ProcessedAudioDTO> result = processedAudioService.getProcessedAudiosByGenre(predictedTypeDTO);
      if (result == null) {
        LOGGER.error("Error: Songs could not be retrieved!");
        responseWrapper.setData(null);
        responseWrapper.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        responseWrapper.setUrlParams(urlParams);
        responseWrapper.setError("Error: Songs could not be retrieved!");
      } else {
        if (result.isEmpty()) {
          LOGGER.info("Error: No songs found!");
          responseWrapper.setData(result);
          responseWrapper.setStatus(HttpStatus.NOT_FOUND);
          responseWrapper.setUrlParams(urlParams);
          responseWrapper.setError("Error: No songs found!");
        } else {
          LOGGER.info("Result: " + result.toString());
          responseWrapper.setData(result);
          responseWrapper.setStatus(HttpStatus.OK);
          responseWrapper.setUrlParams(urlParams);
        }
      }
    }
    return responseWrapper;
  }
}
