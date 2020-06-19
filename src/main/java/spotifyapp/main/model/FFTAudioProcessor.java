package spotifyapp.main.model;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.util.PitchConverter;
import be.tarsos.dsp.util.fft.FFT;
import org.jboss.logging.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FFTAudioProcessor implements AudioProcessor {

    private static final Logger LOGGER = Logger.getLogger(FFTAudioProcessor.class.getName());

    private static final int BUFFER_SIZE = 1024 * 4;
    private int width;
    private int height;
    private int position = 0;
    private float[] amplitudes = new float[BUFFER_SIZE];
    private FFT fft;
    private BufferedImage bufferedImage;
    private int count = 0;

    public FFTAudioProcessor(BufferedImage bufferedImage, int width, int height) {
        this.fft = new FFT(BUFFER_SIZE);
        this.bufferedImage = bufferedImage;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean process(AudioEvent audioEvent) {
        float[] audioFloatBuffer = audioEvent.getFloatBuffer();
        float[] transformBuffer = new float[BUFFER_SIZE * 2];
        System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);
        fft.forwardTransform(transformBuffer);
        fft.modulus(transformBuffer, amplitudes);
        drawFFT(amplitudes, bufferedImage);
        count++;
        return true;
    }

    @Override
    public void processingFinished() {
        LOGGER.info("Processing finished for FFTAudioProcessor. Count = " + count
                + " Position = " + position
                + " Width = " + width
                + " Height = " + height);
    }

    private void drawFFT(float[] amplitudes, BufferedImage bufferedImage) {
        drawFFT(amplitudes, bufferedImage, 0, false, false);
    }

    private void drawFFT(float[] amplitudes, BufferedImage bufferedImage, boolean showMarkers) {
        drawFFT(amplitudes, bufferedImage, 0, false, showMarkers);
    }

    private void drawFFT(
            float[] amplitudes, BufferedImage bufferedImage, double pitch, boolean showPitch) {
        drawFFT(amplitudes, bufferedImage, pitch, showPitch, false);
    }

    private void drawFFT(
            float[] amplitudes,
            BufferedImage bufferedImage,
            double pitch,
            boolean showPitch,
            boolean showMarkers) {
        if (position >= width) {
            return;
        }

        Graphics2D bufferedGraphics = bufferedImage.createGraphics();
        String currentPitch = "";
        double maxAmplitude = 0;
        float[] pixelAmplitudes = new float[height];

        for (int i = amplitudes.length / 800; i < amplitudes.length; i++) {
            int pixelY = frequencyToBin(i * 44100 / (amplitudes.length * 8));
            pixelAmplitudes[pixelY] += amplitudes[i];
            maxAmplitude = Math.max(pixelAmplitudes[pixelY], maxAmplitude);
        }

        for (int i = 0; i < pixelAmplitudes.length; i++) {
            Color color = Color.black;
            if (maxAmplitude != 0) {

                final int greyValue =
                        (int) (Math.log1p(pixelAmplitudes[i] / maxAmplitude) / Math.log1p(1.0000001) * 255);
                color = new Color(greyValue, greyValue, greyValue);
            }
            bufferedGraphics.setColor(color);
            bufferedGraphics.fillRect(position, i, 3, 1);
        }

        if (showPitch && pitch != -1) {
            int pitchIndex = frequencyToBin(pitch);
            bufferedGraphics.setColor(Color.RED);
            bufferedGraphics.fillRect(position, pitchIndex, 1, 1);
            currentPitch = "Current frequency: " + (int) pitch + "Hz";
        }

        if (showMarkers) {
            bufferedGraphics.clearRect(0, 0, 190, 30);
            bufferedGraphics.setColor(Color.WHITE);

            bufferedGraphics.drawString(currentPitch, 20, 20);

            for (int i = 100; i < 500; i += 100) {
                int bin = frequencyToBin(i);
                bufferedGraphics.drawLine(0, bin, 5, bin);
            }

            for (int i = 500; i <= 20000; i += 500) {
                int bin = frequencyToBin(i);
                bufferedGraphics.drawLine(0, bin, 5, bin);
            }

            for (int i = 100; i <= 20000; i *= 10) {
                int bin = frequencyToBin(i);
                bufferedGraphics.drawString(String.valueOf(i), 10, bin);
            }
        }

        position += 3;
        position = position % width;
    }

    private int frequencyToBin(final double frequency) {
        final double minFrequency = 50;
        final double maxFrequency = 11000;
        int bin = 0;
        if (frequency != 0 && frequency > minFrequency && frequency < maxFrequency) {
            double binEstimate = 0;
            final double minCent = PitchConverter.hertzToAbsoluteCent(minFrequency);
            final double maxCent = PitchConverter.hertzToAbsoluteCent(maxFrequency);
            final double absCent = PitchConverter.hertzToAbsoluteCent(frequency * 2);
            binEstimate = (absCent - minCent) / maxCent * height;
            if (binEstimate > 700) {
                LOGGER.info(binEstimate + "");
            }
            bin = height - 1 - (int) binEstimate;
        }
        return bin;
    }

}
