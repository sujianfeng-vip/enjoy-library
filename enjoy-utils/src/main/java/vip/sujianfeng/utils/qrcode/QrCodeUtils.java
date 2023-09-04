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
 * @Author SuJianFeng
 * @Date 2023/2/1
 * @Description
 **/
public class QrCodeUtils {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int margin = 0;
    private static final int LogoPart = 4;

    public static void main(String[] args) throws WriterException {
        //二维码内容
        String content = "https://www.baidu.com";
        String logoPath = "D:\\git-sujianfeng-aliyun\\ai-robot\\70-ui\\src\\main\\resources\\images\\logo-0.jpg"; // 二维码中间的logo信息 非必须
        String format = "jpg";
        int width = 240; // 二维码宽度
        int height = 240;// 二维码高度
        String path = "c:/Temp/百度二维码" + new Date().getTime() + ".png";//设置二维码的文件名
        try {
            BufferedImage bufferedImage = buildQrImage(content, format, width, height, 5, ImageIO.read(new File(logoPath)));
            ImageIO.write(bufferedImage, format, new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片
     * @param content 二维码内容 - 例如： https://www.baidu.com
     * @param format 格式 - 例如： jgp
     * @param width 宽带
     * @param height 高度
     * @param reduceWhiteArea  目前针对容错等级为H,该参数为二维码空白区域的大小,根据实际情况设置，如果二维码内容长度不固定的话 需要自己根据实际情况计算reduceWhiteArea的大小
     * @param logoImage 二维码中间logo图标，可为空
     * @return
     */
    public static BufferedImage buildQrImage(String content, String format, int width, int height, int reduceWhiteArea, BufferedImage logoImage) throws WriterException {
        BitMatrix bitMatrix = setBitMatrix(content, width, height);
        BufferedImage image = toBufferedImage(bitMatrix, reduceWhiteArea);
        // 如果设置了二维码里面的logo 加入LOGO水印
        if (logoImage != null) {
            image = addLogo(image, logoImage);
        }
        return image;
    }



    /**
     * 设置生成二维码矩阵信息
     * @param content 二维码图片内容
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @throws WriterException
     */
    private static BitMatrix setBitMatrix(String content, int width, int height) throws WriterException {
        BitMatrix bitMatrix = null;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 指定编码方式,避免中文乱码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 指定纠错等级 如果二维码里面的内容比较多的话推荐使用H 容错率30%， 这样可以避免一些扫描不出来的问题
        hints.put(EncodeHintType.MARGIN, margin); // 指定二维码四周白色区域大小 官方的这个方法目前没有没有作用默认设置为0
        bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return bitMatrix;

    }

    /**
     * @param matrix
     * @param format
     * @param outStream
     * @param logoPath logo图片
     * @param reduceWhiteArea 二维码空白区域设置
     * @throws IOException
     */
    private static void writeToFile(BitMatrix matrix, String format, OutputStream outStream, String logoPath, int reduceWhiteArea) throws IOException {
        BufferedImage image = toBufferedImage(matrix, reduceWhiteArea);
        // 如果设置了二维码里面的logo 加入LOGO水印
        if (!StringUtils.isEmpty(logoPath)) {
            image = addLogo(image, logoPath);
        }
        ImageIO.write(image, format, outStream);
    }


    /**
     *
     * @param matrix
     * @param reduceWhiteArea
     * @return
     */
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


    /**
     * 给二维码图片中绘制logo信息 非必须
     * @param image 二维码图片
     * @param logoPath logo图片路径
     */
    private static BufferedImage addLogo(BufferedImage image, String logoPath) throws IOException {
        BufferedImage logoImage = ImageIO.read(new File(logoPath));
        return addLogo(image, logoImage);
    }

    private static BufferedImage addLogo(BufferedImage image, BufferedImage logoImage) {
        Graphics2D g = image.createGraphics();
        // 计算logo图片大小,可适应长方形图片,根据较短边生成正方形
        int width = image.getWidth() < image.getHeight() ? image.getWidth() / LogoPart : image.getHeight() / LogoPart;
        int height = width;
        // 计算logo图片放置位置
        int x = (image.getWidth() - width) / 2;
        int y = (image.getHeight() - height) / 2;
        // 在二维码图片上绘制中间的logo
        g.drawImage(logoImage, x, y, width, height, null);
        // 绘制logo边框,可选
        g.setStroke(new BasicStroke(2)); // 画笔粗细
        g.setColor(Color.WHITE); // 边框颜色
        g.drawRect(x, y, width, height); // 矩形边框
        logoImage.flush();
        g.dispose();
        return image;
    }
}
