package vip.sujianfeng.utils.scan;


import vip.sujianfeng.utils.comm.StringUtilsEx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SuJianFeng
 * @date 2019/8/26 17:22
 **/
public class FileScanner implements Scan {

    private String[] scanPaths;

    public FileScanner(String... scanPaths){
        this.scanPaths = scanPaths;
    }

    private void searchFileList(List<String> list, File file, ScanFilter filter){
        System.out.println("scan file: " + file.getPath());
        if (filter == null || filter.test(file.getPath())){
            list.add(file.getPath());
        }
        if (file.getPath().toLowerCase().contains(".jar")){
            List<String> tmpList = JarFileHelper.getJarFileList(file.getPath(), filter);
            list.addAll(tmpList);
        }
        File[] fileList = file.listFiles();
        if (fileList != null){
            for (File f : fileList) {
                searchFileList(list, f, filter);
            }
        }
    }

    @Override
    public List<String> search(ScanFilter filter) {
        List<String> result = new ArrayList<>();
        for (String scanPath : scanPaths) {
            System.out.println("FileScanner -> scanPath: " + scanPath);
            if (StringUtilsEx.isNotEmpty(scanPath)){
                if (scanPath.toLowerCase().contains(".jar")){
                    List<String> tmpList = JarFileHelper.getJarFileList(scanPath, filter);
                    result.addAll(tmpList);
                }else{
                    if (scanPath.startsWith("file:/")){
                        scanPath = StringUtilsEx.rightStr(scanPath, "file:/");
                    }
                    if (scanPath.startsWith("file:")){
                        scanPath = StringUtilsEx.rightStr(scanPath, "file:");
                    }
                    searchFileList(result, new File(scanPath), filter);
                }
            }
        }
        return result;
    }

}