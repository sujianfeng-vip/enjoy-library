package vip.sujianfeng.fxui.ctrls;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author SuJianFeng
 * @date 2019/9/6 21:09
 **/
public class ImageButton extends Button {

    public ImageButton(String resUrl, double width, double height, String text){
        Image srcImage = new Image(this.getClass().getResource(resUrl).toString());
        ImageView imageView = new ImageView(srcImage);
        BeautifyImageView.beautify(imageView, width, height, 5);
        this.setGraphic(imageView);
        this.setPrefWidth(60);
        this.setPrefHeight(60);
        this.setPadding(Insets.EMPTY);
        //设置鼠标样式
        this.setCursor(Cursor.HAND);
    }
}
