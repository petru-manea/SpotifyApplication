package spotifyapp.main.service.impl;

import com.google.common.io.Files;
import org.apache.commons.lang3.ArrayUtils;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import spotifyapp.main.service.ImageEncoderService;
import spotifyapp.main.util.FileUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Arrays;

@Component
public class ImageEncoderServiceImpl implements ImageEncoderService, AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(ImageEncoderServiceImpl.class.getName());

    private Graph graph = new Graph();

    public ImageEncoderServiceImpl() {
        try {
            byte[] inputStreamBytes =
                    FileUtils.getBytes(Files.asByteSource(new File("models/resnet-v2.pb")).openStream());
            graph.importGraphDef(inputStreamBytes);
        } catch (IOException e) {
            LOGGER.error("Could not initiate graph in ImageEncoder! " + e.getMessage());
        }
    }

    @Override
    public float[] encodeImage(BufferedImage image, int imgWidth, int imgHeight) {
        image = resizeImage(image, imgWidth, imgHeight);

        Tensor<Float> imageTensor = getImageTensor(image, imgWidth, imgHeight);

        try (Session sess = new Session(graph);
             Tensor<Float> result =
                     sess.runner()
                             .feed("input_1:0", imageTensor)
                             .fetch("output_node0:0")
                             .run()
                             .get(0)
                             .expect(Float.class)) {
            final long[] rshape = result.shape();
            if (result.numDimensions() != 2 || rshape[0] != 1) {
                throw new RuntimeException(
                        String.format(
                                "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
                                Arrays.toString(rshape)));
            }
            int nlabels = (int) rshape[1];
            return result.copyTo(new float[1][nlabels])[0];
        } catch (Exception ex) {
            LOGGER.error("Failed to predict image! " + ex.getMessage());
        }

        return ArrayUtils.EMPTY_FLOAT_ARRAY;
    }

    private Tensor<Float> getImageTensor(BufferedImage image, int imgWidth, int imgHeight) {

        final int channels = 1;

        int index = 0;
        FloatBuffer fb = FloatBuffer.allocate(imgWidth * imgHeight * channels);

        for (int row = 0; row < imgHeight; row++) {
            for (int column = 0; column < imgWidth; column++) {
                int pixel = image.getRGB(column, row);
                float red = (pixel >> 16) & 0xff;
                fb.put(index++, red);
            }
        }

        return Tensor.create(new long[] {1, imgHeight, imgWidth, channels}, fb);
    }

    private BufferedImage resizeImage(BufferedImage img, int imgWidth, int imgHeight) {
        if (img.getWidth() != imgWidth || img.getHeight() != imgHeight) {
            Image newImg = img.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            BufferedImage newBufferedImg =
                    new BufferedImage(
                            newImg.getWidth(null), newImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            newBufferedImg.getGraphics().drawImage(newImg, 0, 0, null);
            return newBufferedImg;
        }
        return img;
    }

    @Override
    public void close() {
        if (graph != null) {
            graph.close();
            graph = null;
        }
    }

}
