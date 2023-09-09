package vip.sujianfeng.utils.comm;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * author SuJianFeng
 * createTime  2023/1/29
 **/
public class ClipboardUtils {

    public static void copyText(String text) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();//Get Clipboard
        Transferable tText = new StringSelection(text);
        clip.setContents(tText, null); //Set clipboard content
    }

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

    public static String getClipboardText() throws IOException, UnsupportedFlavorException {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Get content from the clipboard
        Transferable clipTf = sysClip.getContents(null);
        if (clipTf != null) {
            // Check if the content is of text type
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                ret = ConvertUtils.cStr(clipTf.getTransferData(DataFlavor.stringFlavor));
            }
        }
        return ret;
    }

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

    public static void clipClear() {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection("");
        clip.setContents(tText, null);
    }
}
