package vip.sujianfeng.utils;

import vip.sujianfeng.utils.comm.GuidUtils;
import vip.sujianfeng.utils.comm.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * author SuJianFeng
 * createTime  2023/4/21
 * @Description
 **/
public class ImageUtilsTest {

    public static void main(String[] args) {
        try {
            String imgUrl = "D:\\git-sujianfeng-aliyun\\china-chess-robot\\doc\\images\\scene1.png";
            Rectangle rect = new Rectangle(583,13, 69,62);
            ImageUtils.subImage2file(ImageIO.read(new File(imgUrl)), rect, "C:/Temp/" + GuidUtils.buildGuid(), "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
