package vip.sujianfeng.utils.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.zip.*;

/**
 * author sujianfeng
 * create 2019-04-21 7:36
 * Compression tool class
 */
public class ZipUtils {
    private static Logger logger = LoggerFactory.getLogger(ZipUtils.class);
    private static final int BUFFER_SIZE = 2 * 1024;

    public static String gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip=null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }finally{
            if(gzip!=null){
                try {
                    gzip.close();
                } catch (IOException e) {
                    logger.error(e.toString(), e);
                }
            }
        }

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String gunzip(String compressedStr){
        if(compressedStr==null){
            return null;
        }

        ByteArrayOutputStream out= new ByteArrayOutputStream();
        ByteArrayInputStream in=null;
        GZIPInputStream ginzip=null;
        byte[] compressed=null;
        String decompressed = null;
        try {
            compressed = Base64.getDecoder().decode(compressedStr);
            in=new ByteArrayInputStream(compressed);
            ginzip=new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed=out.toString();
        } catch (IOException e) {
            logger.error(e.toString(), e);
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

    public static final String zip(String str) {
        if (str == null)
            return null;
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        String compressedStr = null;
        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
            zout.closeEntry();
            compressed = out.toByteArray();
            compressedStr = Base64.getEncoder().encodeToString(compressed);
        } catch (IOException e) {
            compressed = null;
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return compressedStr;
    }

    public static final String unzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed = null;
        try {
            byte[] compressed = Base64.getDecoder().decode(compressedStr);
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            decompressed = null;
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return decompressed;
    }

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws IOException {
        Throwable t = null;
        ZipOutputStream zos = new ZipOutputStream(out);
        try{
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        } catch (Throwable t1) {
            t = t1;
            throw t1;
        }finally {
            if (t != null) {
                try {
                    zos.close();
                } catch (Throwable t2) {
                    t.addSuppressed(t2);
                }
            } else {
                zos.close();
            }
        }
    }


    public static void toZip(List<File> srcFiles, OutputStream out) throws IOException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = new ZipOutputStream(out);
        for (File srcFile : srcFiles) {
            byte[] buf = new byte[BUFFER_SIZE];
            zos.putNextEntry(new ZipEntry(srcFile.getName()));
            int len;
            FileInputStream in = new FileInputStream(srcFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        }
    }

    /**
     *
     * Recursive compression method
     *
     * @param sourceFile
     *            source file
     *
     * @param zos
     *            Zip output stream
     *
     * @param name
     *            Compressed name
     *
     * @param KeepDirStructure
     *            Do you want to keep the original directory structure? True: Keep the directory structure;
     *
     *            false:All files run to the root directory of the compressed package (note: not retaining the directory structure may result in files with the same name and compression failure)
     *
     * @throws Exception
     *
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // Add a zip entity to the zip output stream, with the name in the constructor being the name of the file containing the zip entity
            zos.putNextEntry(new ZipEntry(name));
            // Copy the file to the zip output stream
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // When retaining the original file structure, empty folders need to be processed
                if (KeepDirStructure) {
                    // Handling empty folders
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // No files, no copy required for files
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // Determine if it is necessary to preserve the original file structure
                    if (KeepDirStructure) {
                        // Note: File. getName() needs to be preceded by the name of the parent folder and a slash,
                        // Otherwise, the original file structure cannot be preserved in the final compressed package, that is, all files will run to the root directory of the compressed package
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    public static void dir2zip(String dir, String zipFile) throws IOException {
        FileOutputStream fos1 = new FileOutputStream(new File(zipFile));
        ZipUtils.toZip(dir, fos1, true);
    }

    public static void main(String[] args) throws Exception {
        /** Test compression method 1 */
        dir2zip("C:/Temp/msClientJar/test1", "C:/Temp/msClientJar/mytest02.zip");
        /** Test Compression Method 2 */
		/*List<File> fileList = new ArrayList<>();
		fileList.add(new File("E:/emailTemplate.html"));
		fileList.add(new File("E:/logobottom.jpg"));
		FileOutputStream fos2 = new FileOutputStream(new File("C:/Temp/msClientJar/mytest02.zip"));
        ZipUtils.toZip(fileList, fos2);*/
    }

}
