package com.yht.iptv.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by admin on 2017/9/27.
 */

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * @return 获取本软件的保存目录
     */
    public static String getAppPath() {
//        String path = Environment.getExternalStorageDirectory()
//                .getAbsolutePath()
//                + File.separator
//                + "iptv"
//                + File.separator;
        String path = Environment.getDataDirectory().getAbsolutePath()
                + File.separator
                + "iptv"
                + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * @return 获取本软件的保存目录
     */
    public static String getCrashPath() {
        String path = getAppPath()
                + File.separator + "crash" + File.separator;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * @return 获取images目录
     */
    public static String getDownLoadPath() {

        String path = getAppPath()
                + File.separator + "downloads" + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * @return 获取advert_30s目录
     */
    public static String getAdvertPath() {

        String path = getAppPath()
                +  "advert" + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * @return 获取advert_15s目录
     */
    public static String getAdvert15Path() {

        String path = getAppPath()
                 + "advert15" + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return path;
    }


    /**
     * @return 获取初始信息目录
     */
    public static String getInfoPath() {

        String path = getAppPath()
                + File.separator + "baseInfo" + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    /**
     * @return 获取初始信息目录
     */
    public static String getInfoPath(String dir) {

        String path = getAppPath()
                + File.separator + dir + File.separator;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public static void delfile(String path) {
        File f = new File(path);
        deleteFile(f);
    }

    // 删除目录文件
    private static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
        }
    }

    public static int fileNumber(String path){
        File file = new File(path);
        if(file.exists()){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                return files.length;
            }
        }
        return 0;
    }

    public static File[] fileVideo(String path){
        File file = new File(path);
        if(file.exists()){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                return files;
            }
        }
        return null;
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 读取文字信息
     *
     * @param filePath
     */
    public static String readTxtFile(String filePath) {
        String remarksText = null;
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    remarksText = lineTxt;
                }
                read.close();
                return remarksText;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void saveTextFiles(String remarks,String remarksName) {
        try {
            // 将文字内容写到txt文本中
            FileOutputStream fileW = new FileOutputStream(getInfoPath()
                    + remarksName);
            fileW.write((remarks).getBytes());
            fileW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
