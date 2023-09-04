package vip.sujianfeng.fxui.ctrls;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * @author SuJianFeng
 * @date 2019/9/6 20:56
 **/
public class ImageLabelButton extends Label {

    private BeautifyImageView imageView;

    public ImageLabelButton(BeautifyImageView imageView, String text){
        this.imageView = imageView;
        this.setText(text);
        this.setGraphic(imageView);
        this.setContentDisplay(ContentDisplay.TOP);
        //设置鼠标样式
        this.setCursor(Cursor.HAND);
        this.imageView.setCursor(Cursor.HAND);
    }

    public ImageLabelButton(Image image, double width, double height, double shadowRadius, String text){
        this(new BeautifyImageView(image, width, height, shadowRadius), text);

    }

    public ImageLabelButton(String resUrl, double width, double height, double shadowRadius, String text){
        this(new BeautifyImageView(resUrl, width, height, shadowRadius), text);
    }

    public void setMouseClickEvent(EventHandler<MouseEvent> mouseEvent){
        this.imageView.setOnMouseClicked(mouseEvent);
        this.setOnMouseClicked(mouseEvent);
    }

    public BeautifyImageView getImageView() {
        return imageView;
    }

    public void setImageView(BeautifyImageView imageView) {
        this.imageView = imageView;
    }
}
