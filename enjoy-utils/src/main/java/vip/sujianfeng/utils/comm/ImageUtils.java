package vip.sujianfeng.utils.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

/**
 * Created by sujianfeng on 2016/3/30.
 */
public class ImageUtils {

    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static BufferedImage loadFile(String imgFile) throws IOException {
        return ImageIO.read(new File(imgFile));
    }

    public static BufferedImage loadUrl(String url) throws IOException {
        return ImageIO.read(new URL(url));
    }

    public static boolean imgChangeWidth(String srcfileName, String destFileName, int width){
        try {
            File file = new File(srcfileName);
            BufferedImage oriImg = ImageIO.read(file);
            if (oriImg == null || oriImg.getWidth(null) <= width){
                return false;
            }
            int height = oriImg.getHeight(null) * width / oriImg.getWidth(null);// Reduce the height proportionally
            Image scaledImage = oriImg.getScaledInstance(width, height,Image.SCALE_DEFAULT);
            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            output.createGraphics().drawImage(scaledImage, 0, 0, null); // draw designs
            File targetFile = new File(destFileName);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            ImageIO.write(output, "jpg", targetFile);   // Save it to the target file
            return true;
        } catch (IOException e) {
            logger.error(e.toString(), e);
            return false;
        }
    }

    public static String GetImageStr(String imgFilePath){
        InputStream in = null;
        byte[] data = null;
        //Read image byte array
        try{
            in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            //Encoding Byte Array Base64
            return Base64.getEncoder().encodeToString(data);//Returns a Base64 encoded byte array string
        }catch (IOException e){
            logger.error(e.toString(), e);
        }finally {
            try {
                if(in != null)in.close();
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
        }
        return "";
    }

    public static boolean GenerateImage(String imgFilePath,String fileName,String imgStr){
        if (imgStr == null) // Image data is empty
            return false;
        OutputStream out = null;
        try{
            if(imgStr.indexOf("data:image/") != -1){
                int l = imgStr.indexOf("base64,");
                imgStr = imgStr.substring(l+"base64,".length());
            }
            //Base64 decoding
            byte[] b =  Base64.getDecoder().decode(imgStr);
            for(int i=0;i<b.length;++i){
                if(b[i]<0){//Adjusting abnormal data
                    b[i]+=256;
                }
            }
            //Generate jpeg images
            File filePath = new File(imgFilePath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            out = new FileOutputStream(new File(imgFilePath+"/"+fileName));
            out.write(b);
            out.flush();
            return true;
        }catch (Exception e){
            logger.error(e.toString(), e);
            return false;
        }finally {
            try {
                if(null != out)out.close();
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
            return true;
        }
    }

    public static void subImage2file(BufferedImage image, Rectangle rect, String fileName, String fileType) throws IOException {
        BufferedImage subimage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
        File rectangleFile = new File(fileName + "." + fileType);
        ImageIO.write(subimage, fileType, rectangleFile);
    }

    public static void subImage2file(String srcImageFile, Rectangle rect, String fileName, String fileType) throws IOException {
        subImage2file(ImageIO.read(new File(srcImageFile)), rect, fileName, fileType);
    }

    public static String reduceImg(String imgsrc, String filePath, String fileName, String fileType, int widthdist, int heightdist) {
        try {
            File srcfile = new File(imgsrc);
            if (!srcfile.exists()) {
                return "";
            }
            Image bi = ImageIO.read(srcfile);
            if((bi.getHeight(null)*bi.getWidth(null))>widthdist*heightdist) {
                int w = widthdist;
                int h = heightdist;
                int w1 = bi.getHeight(null);
                int h1 = bi.getWidth(null);
                //Proportional scaling
                if (w1 / w > h1 / h) {
                    heightdist = h1 * w / w1;
                } else {
                    widthdist = w1 * h / h1;
                }
            }
            File destPath = new File(filePath);
            if(!destPath.exists()){
                destPath.mkdirs();
            }
            String smallFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_small." + fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            //Image src = javax.imageio.ImageIO.read(srcfile);
            BufferedImage image = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
            image.getGraphics().drawImage(bi.getScaledInstance(widthdist, heightdist,  Image.SCALE_DEFAULT), 0, 0,  null);
            //image.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,  Image.SCALE_AREA_AVERAGING), 0, 0,  null);
            File imageFile = new File(fileName + "." + fileType);
            ImageIO.write(image, fileType, imageFile);
            return filePath + "/" + smallFileName;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args){
        String img=GetImageStr("F:\\upload\\Desert.jpg");
        GenerateImage("F:\\upload","img.jpg",img);

        imgChangeWidth("C:\\1.png", "C:\\1\\1_s.jpg", 260);
    }
}
