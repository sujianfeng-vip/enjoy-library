package vip.sujianfeng.utils.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;

/**
 * 二维码工具类
 * author SuJianFeng
 * createTime  2023/2/1
 * Description
 **/
public class QrCodeUtils {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int margin = 0;
    private static final int LogoPart = 4;

    public static void main(String[] args) throws WriterException {
        String content = "https://www.baidu.com";
        String logoPath = "D:\\git-sujianfeng-aliyun\\ai-robot\\70-ui\\src\\main\\resources\\images\\logo-0.jpg";
        String format = "jpg";
        int width = 240;
        int height = 240;
        String path = "c:/Temp/" + new Date().getTime() + ".png";//Set the file name for the QR code
        try {
            BufferedImage bufferedImage = buildQrImage(content, format, width, height, 5, ImageIO.read(new File(logoPath)));
            ImageIO.write(bufferedImage, format, new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage buildQrImage(String content, String format, int width, int height, int reduceWhiteArea, BufferedImage logoImage) throws WriterException {
        BitMatrix bitMatrix = setBitMatrix(content, width, height);
        BufferedImage image = toBufferedImage(bitMatrix, reduceWhiteArea);
        // 如果设置了二维码里面的logo 加入LOGO水印
        if (logoImage != null) {
            image = addLogo(image, logoImage);
        }
        return image;
    }



    private static BitMatrix setBitMatrix(String content, int width, int height) throws WriterException {
        BitMatrix bitMatrix = null;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // Specify encoding method to avoid Chinese garbled characters
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Specify the error correction level. If there is a lot of content in the QR code, it is recommended to use H with a fault tolerance rate of 30%, which can avoid some issues that cannot be scanned out
        hints.put(EncodeHintType.MARGIN, margin); // The official method of specifying the size of the white area around the QR code is currently not effective, and the default setting is 0
        bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return bitMatrix;

    }

    private static void writeToFile(BitMatrix matrix, String format, OutputStream outStream, String logoPath, int reduceWhiteArea) throws IOException {
        BufferedImage image = toBufferedImage(matrix, reduceWhiteArea);
        // 如果设置了二维码里面的logo 加入LOGO水印
        if (!StringUtils.isEmpty(logoPath)) {
            image = addLogo(image, logoPath);
        }
        ImageIO.write(image, format, outStream);
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix, int reduceWhiteArea) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width - 2 * reduceWhiteArea, height - 2 * reduceWhiteArea, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = reduceWhiteArea; x < width - reduceWhiteArea; x++) {
            for (int y = reduceWhiteArea; y < height - reduceWhiteArea; y++) {
                image.setRGB(x - reduceWhiteArea, y - reduceWhiteArea, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static BufferedImage addLogo(BufferedImage image, String logoPath) throws IOException {
        BufferedImage logoImage = ImageIO.read(new File(logoPath));
        return addLogo(image, logoImage);
    }

    private static BufferedImage addLogo(BufferedImage image, BufferedImage logoImage) {
        Graphics2D g = image.createGraphics();
        // Calculate the size of the logo image, which can adapt to rectangular images and generate squares based on shorter edges
        int width = image.getWidth() < image.getHeight() ? image.getWidth() / LogoPart : image.getHeight() / LogoPart;
        int height = width;
        // Calculate the placement position of the logo image
        int x = (image.getWidth() - width) / 2;
        int y = (image.getHeight() - height) / 2;
        // Draw the middle logo on the QR code image
        g.drawImage(logoImage, x, y, width, height, null);
        // Draw logo border, optional
        g.setStroke(new BasicStroke(2)); // Brush thickness
        g.setColor(Color.WHITE); // Border Color
        g.drawRect(x, y, width, height); // Rectangular Border
        logoImage.flush();
        g.dispose();
        return image;
    }
}
