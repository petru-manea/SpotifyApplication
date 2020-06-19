package spotifyapp.main.service;

import java.awt.image.BufferedImage;

public interface ImageEncoderService {

    float[] encodeImage(BufferedImage image, int imgWidth, int imgHeight);

}
