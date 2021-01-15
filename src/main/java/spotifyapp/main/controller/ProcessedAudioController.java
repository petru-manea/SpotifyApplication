package spotifyapp.main.controller;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import spotifyapp.main.config.ResponseWrapper;
import spotifyapp.main.model.dto.PredictedTypeDTO;
import spotifyapp.main.model.dto.ProcessedAudioDTO;
import spotifyapp.main.service.ProcessedAudioService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(path = "/api/spotify")
public class ProcessedAudioController {

  private static final Logger LOGGER = Logger.getLogger(ProcessedAudioController.class.getName());

  private final ProcessedAudioService processedAudioService;

  @Autowired
  public ProcessedAudioController(ProcessedAudioService processedAudioService) {
    this.processedAudioService = processedAudioService;
  }

  @GetMapping(path = "/song/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseWrapper<List<ProcessedAudioDTO>> getAllSongs(
      HttpServletRequest httpRequest) {

    String urlParams = "/song/all";

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

  @GetMapping(path = "/song/{id}/audio", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public @ResponseBody ResponseEntity<Resource> getSongAudioById(
      @PathVariable("id") String id, HttpServletRequest httpRequest) {

    String urlParams = "/song/" + id + "/audio";

    ProcessedAudioDTO processedAudioDTO = processedAudioService.getProcessedAudioById(id);
    byte[] audioFile = processedAudioService.getProcessedAudioFileById(id);

    if (processedAudioDTO == null) {
      LOGGER.error("Error: Song not found!");
      return ResponseEntity.notFound().build();
    }
    if (audioFile == null) {
      LOGGER.error("Error: Audio file not found!");
      return ResponseEntity.notFound().build();
    }
    ContentDisposition contentDisposition =
        ContentDisposition.builder("attachment").filename(processedAudioDTO.getFilename()).build();
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(processedAudioDTO.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
        .body(new ByteArrayResource(audioFile));
  }

  @GetMapping(path = "/song", produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseWrapper<List<ProcessedAudioDTO>> getSongByGenre(
      @RequestParam("genre") String genre, HttpServletRequest httpRequest) {

    String urlParams = "/song?" + "genre=" + genre;

    ResponseWrapper<List<ProcessedAudioDTO>> responseWrapper = new ResponseWrapper<>();

    PredictedTypeDTO predictedTypeDTO = PredictedTypeDTO.get(genre);

    if (predictedTypeDTO == null) {
      LOGGER.error("Error: No genre of type + " + genre + "!");
      responseWrapper.setData(null);
      responseWrapper.setStatus(HttpStatus.BAD_REQUEST);
      responseWrapper.setUrlParams(urlParams);
      responseWrapper.setError("Error: No genre of type + " + genre + "!");
    } else {
      List<ProcessedAudioDTO> result =
          processedAudioService.getProcessedAudiosByGenre(predictedTypeDTO);
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
