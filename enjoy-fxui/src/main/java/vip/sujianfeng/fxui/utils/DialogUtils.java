package vip.sujianfeng.fxui.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import vip.sujianfeng.fxui.comm.formDialog.FormComfirmController;
import vip.sujianfeng.fxui.comm.formDialog.FormInfoController;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author SuJianFeng
 * createTime  2019/9/6 21:33
 **/
public class DialogUtils {

    public static void error(String message){
        FxFormUtils.showAndWaitForm(FormInfoController.class, (it)-> {
            it.error(message);
        });
    }

    public static void info(String message){
        FxFormUtils.showAndWaitForm(FormInfoController.class, (it)-> {
            it.info(message);
        });
    }

    public static void confirm(String message, FormComfirmController.IConfirm confirm){
        confirm(null, message, confirm);
    }

    public static void confirm(String title, String message, FormComfirmController.IConfirm confirm){
        FxFormUtils.showAndWaitForm(FormComfirmController.class, (it)-> {
            it.init(title, message, confirm);
        });
    }

    public static boolean confirm(String message){
        return confirm(null, message);
    }

    public static boolean confirm(String title, String message){
        AtomicBoolean result = new AtomicBoolean(false);
        FxFormUtils.showAndWaitForm(FormComfirmController.class, (it)-> {
            it.init(title, message, result::set);
        });
        return result.get();
    }

    public interface FileChosen {
        void selectBack(File file);
    }

    public static void openFileChooser(Window owner, String filterText, String filter, FileChosen fileChosen) {
        fileChooser(owner, filterText, filter, fileChosen);
    }

    public static void fileChooser(Window owner, String filterText, String filter, FileChosen fileChosen) {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                String.format("%s (%s)", filterText, filter), filter);
        fileChooser.getExtensionFilters().add(extFilter);
        // Show open file dialog
        File file = fileChooser.showOpenDialog(owner);
        if (file != null) {
            fileChosen.selectBack(file);
        }
    }


    public static void saveFileChooser(Window owner, String filterText, String filter, FileChosen fileChosen) {
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                String.format("%s (%s)", filterText, filter), filter);
        fileChooser.getExtensionFilters().add(extFilter);
        // Show Save file dialog
        File file = fileChooser.showSaveDialog(owner);
        if (file != null) {
            fileChosen.selectBack(file);
        }
    }
}
