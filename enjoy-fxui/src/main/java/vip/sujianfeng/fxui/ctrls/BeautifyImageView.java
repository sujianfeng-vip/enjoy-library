package vip.sujianfeng.fxui.ctrls;

import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * author SuJianFeng
 * createTime  2019/9/6 20:58
 **/
public class BeautifyImageView extends ImageView {

    public static void beautify(ImageView imageView, double width, double height, double shadowRadius){
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(shadowRadius);
        clip.setArcHeight(shadowRadius);
        imageView.setClip(clip);
        // snapshot the rounded image.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);
        // remove the rounding clip so that our effect can show through.
        imageView.setClip(null);
        // apply a shadow effect.
        imageView.setEffect(new DropShadow(shadowRadius, Color.rgb(0,0,0, 0.5)));
        // store the rounded image in the imageView.
        imageView.setImage(image);
    }

    public BeautifyImageView(Image image, double width, double height, double shadowRadius){
        super();
        this.setImage(image);
        beautify(this, width, height, shadowRadius);
        //设置鼠标样式
        this.setCursor(Cursor.HAND);
    }

    public BeautifyImageView(String url, double width, double height, double shadowRadius){
        this(new Image(url), width, height, shadowRadius);
    }
}
