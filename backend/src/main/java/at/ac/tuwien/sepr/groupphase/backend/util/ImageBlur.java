package at.ac.tuwien.sepr.groupphase.backend.util;

import org.springframework.core.io.InputStreamResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ImageBlur {

    private static final float[] MATRIX = {
        1 / 16f, 1 / 8f, 1 / 16f,
        1 / 8f, 1 / 4f, 1 / 8f,
        1 / 16f, 1 / 8f, 1 / 16f,
    };

    public static InputStreamResource blur(InputStreamResource input, String mimeType) throws IOException {
        var imageType = mimeType.toLowerCase().replace("image/", "");

        var is = input.getInputStream();
        var image = ImageIO.read(is);

        int height = image.getHeight();
        int width = image.getWidth();
        var blurredImage = new BufferedImage(width, height, image.getType());

        int[][] reds = new int[10][10];
        int[][] greens = new int[10][10];
        int[][] blues = new int[10][10];

        for (int x = 0; x < width - 10; x++) {
            for (int y = 0; y < height - 10; y++) {
                for (int dx = 0; dx < 10; dx++) {
                    for (int dy = 0; dy < 10; dy++) {
                        Color color = new Color(image.getRGB(x + dx, y + dy));
                        reds[dx][dy] = color.getRed();
                        greens[dx][dy] = color.getGreen();
                        blues[dx][dy] = color.getBlue();
                    }
                }

                Arrays.sort(reds);
                Arrays.sort(greens);
                Arrays.sort(blues);

                int medianRed = reds[5][5];
                int medianGreen = greens[5][5];
                int medianBlue = blues[5][5];

                Color medianColor = new Color(medianRed, medianGreen, medianBlue);
                blurredImage.setRGB(x + 5, y + 5, medianColor.getRGB());
            }
        }

        var os = new ByteArrayOutputStream();
        ImageIO.write(blurredImage, imageType, os);
        is = new ByteArrayInputStream(os.toByteArray());
        return new InputStreamResource(is);
    }
}
