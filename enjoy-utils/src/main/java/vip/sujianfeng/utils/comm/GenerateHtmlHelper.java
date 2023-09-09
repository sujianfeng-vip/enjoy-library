package vip.sujianfeng.utils.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by liaogw on 2015/12/2.
 */
public class GenerateHtmlHelper {
    private static Logger logger = LoggerFactory.getLogger(GenerateHtmlHelper.class);
    public static String generateHtml(String basePath, String modelName, String fileName, Map<String,String> content, String templetFilePath) {
        String tempStr = readFile(basePath+templetFilePath);
        File filePath = new File(basePath+"/staticHtml/"+modelName);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        Set<String> keys = content.keySet();
        for (String key: keys) {
            String value=content.get(key);
            tempStr=tempStr.replaceAll("#"+key+"#",null==value?"":value);
        }
        if(writeFile(filePath+"/"+fileName+".jsp",tempStr)){
            return fileName;
        }
        return "";
    }

    public static String readFile(String filePathAndName) {
        String fileContent = "";
        try {
            File f = new File(filePathAndName);
            if(f.isFile()&&f.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8");
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading file content operation");
            logger.error(e.toString(), e);
        }
        return fileContent;
    }
    public static boolean writeFile(String filePathAndName, String fileContent) {
        try {
            File f = new File(filePathAndName);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            BufferedWriter writer=new BufferedWriter(write);
            //PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePathAndName)));
            //PrintWriter writer = new PrintWriter(new FileWriter(filePathAndName));
            writer.write(fileContent);
            writer.flush();
            writer.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error writing file content operation");
            logger.error(e.toString(), e);
        }
        return false;
    }
}
