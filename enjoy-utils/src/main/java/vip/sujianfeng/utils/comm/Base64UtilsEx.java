package vip.sujianfeng.utils.comm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author SuJianFeng
 * @date 2019/10/29 9:57
 **/
public class Base64UtilsEx {

    public static String encodeStr(String content){
        return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] encode(byte[] bytes){
        return Base64.getEncoder().encode(bytes);
    }

    public static String encodeStr(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decodeStr(String bytesStr){
        byte[] decode = decode(bytesStr);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public static byte[] decode(String bytesStr){
        return Base64.getDecoder().decode(bytesStr);
    }

    /**
     * 图片转base64文本
     * @param bufferedImage
     * @param formatName  png,jpg,bmp 等
     * @return
     * @throws IOException
     */
    public static String bufferedImageToBase64(BufferedImage bufferedImage, String formatName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, formatName, baos);
        byte[] bytes = baos.toByteArray();
        return Base64UtilsEx.encodeStr(bytes);
    }

    public static BufferedImage base64ToBufferImage(String str) throws IOException {
        byte[] bytes = Base64UtilsEx.decode(str);
        ByteArrayInputStream i = new ByteArrayInputStream(bytes);
        return ImageIO.read(i);
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
	       /* if (hasAlpha) {
	         transparency = Transparency.BITMASK;
	         }*/

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
	        /*if (hasAlpha) {
	         type = BufferedImage.TYPE_INT_ARGB;
	         }*/
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static void main(String[] args) {
        String test = "abc,张三,aaa";
        String encode = encodeStr(test);
        String decode = decodeStr(encode);
        System.out.println("encode -> " + encode);
        System.out.println("decode -> " + decode);
    }

}
