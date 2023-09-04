package vip.sujianfeng.utils.comm;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * @Author SuJianFeng
 * @Date 2023/1/29
 * @Description
 **/
public class ClipboardUtils {

    /**
     * 将文本放入粘贴板
     */
    public static void copyText(String text) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//获取剪切板
        Transferable tText = new StringSelection(text);
        clip.setContents(tText, null); //设置剪切板内容
    }


    /**
     * 将图片放入粘贴版
     */
    public static void copyImage(Image image) {
        Transferable trans = new Transferable() {

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (isDataFlavorSupported(flavor)) return image;
                throw new UnsupportedFlavorException(flavor);
            }
        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    /**
     * 获得粘贴板文本
     */
    public static String getClipboardText() throws IOException, UnsupportedFlavorException {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);
        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                ret = ConvertUtils.cStr(clipTf.getTransferData(DataFlavor.stringFlavor));
            }
        }
        return ret;
    }

    /**
     * 获得粘贴板图片
     */
    public static Image getImageFromClipboard() throws IOException, UnsupportedFlavorException {
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else
            if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
                return (Image) cc.getTransferData(DataFlavor.imageFlavor);
        return null;
    }

    /**
     * 清除粘贴版内容
     */
    public static void clipClear() {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard(); //获取剪切板
        Transferable tText = new StringSelection("");
        clip.setContents(tText, null); //设置剪切板内容
    }
}
