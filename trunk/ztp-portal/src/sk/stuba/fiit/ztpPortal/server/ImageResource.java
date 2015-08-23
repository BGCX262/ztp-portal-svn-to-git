package sk.stuba.fiit.ztpPortal.server;

import java.awt.image.BufferedImage;

import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

/**
 * Image resource pre nacitavanie obrazkov z databazy
 * @author Peter Bradac
 *
 */

public class ImageResource extends DynamicImageResource {

	private static final long serialVersionUID = 1L;

	// obrazok vo forme byte[]
    private byte[] image;

    public ImageResource(byte[] image, String format) {
        this.image = image;
        setFormat(format);
    }

    public ImageResource(BufferedImage image) {
        this.image = toImageData(image);
    }

    @Override
    protected byte[] getImageData() {
        if (image != null) {
            return image;
        } else {
            return new byte[0];
        }

    }

    /**
     * Cachovanie obrazku - 1 den
     */
    @Override
    protected int getCacheDuration() {
       
        return 3600*24;
    }

} 