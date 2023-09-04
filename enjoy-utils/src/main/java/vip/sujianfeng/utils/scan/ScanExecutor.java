package vip.sujianfeng.utils.scan;

import java.util.List;

/**
 * @author SuJianFeng
 * @date 2019/8/26 17:25
 **/
public class ScanExecutor implements Scan {

    private volatile static ScanExecutor instance;

    private String[] scanPaths;

    private ScanExecutor(String... scanPaths){
        this.scanPaths = scanPaths;
    }

    @Override
    public List<String> search(ScanFilter filter) {
        Scan fileSc = new FileScanner(this.scanPaths);
        List<String> fileSearch = fileSc.search(filter);
        Scan jarScanner = new JarScanner();
        List<String> jarSearch = jarScanner.search(filter);
        fileSearch.addAll(jarSearch);
        return fileSearch;
    }

    public static ScanExecutor getInstance(String... scanPaths){
        if(instance == null){
            synchronized (ScanExecutor.class){
                if(instance == null){
                    instance = new ScanExecutor(scanPaths);
                }
            }
        }
        return instance;
    }

}