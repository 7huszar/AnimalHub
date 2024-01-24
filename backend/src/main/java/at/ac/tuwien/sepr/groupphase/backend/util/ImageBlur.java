package at.ac.tuwien.sepr.groupphase.backend.util;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.file.FileDetailDto;
import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import org.springframework.core.io.InputStreamResource;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageBlur {

    public static FileDetailDto blur(FileDetailDto file) throws IOException {
        var imageType = file.getContentMimeType().toLowerCase().replace("image/", "");

        var is = file.getFile().getInputStream();
        var image = ImageIO.read(is);

        var imp = new ImagePlus("Image", image);

        double diagonal = Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight());
        double sigma = diagonal * 0.03;  // Set sigma to be 5% of the image's diagonal size

        var gb = new GaussianBlur();
        gb.blurGaussian(imp.getProcessor(), sigma);

        var blurredImage = imp.getBufferedImage();

        var os = new ByteArrayOutputStream();
        ImageIO.write(blurredImage, imageType, os);

        file.setContentLength(os.size());

        is = new ByteArrayInputStream(os.toByteArray());

        file.setFile(new InputStreamResource(is));

        return file;
    }
}
