package com.meijialife.dingdang.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import com.meijialife.dingdang.Constants;

import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * 文件操作工具类
 * Created by andye
 */

public class FileUtil {

    public static void initPath() {
        String folder = Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_NAME;
        File file = new File(folder);
        if (file.exists() && file.isFile() && !file.isDirectory()) {
            file.delete();
        }

        String folder_record = Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_RECORD;
        if (!FileUtil.isFolderExist(folder_record)) {
            FileUtil.makeDirs(folder_record);
        }
        String folder_temp = Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_TEMP;
        if (!FileUtil.isFolderExist(folder_temp)) {
            FileUtil.makeDirs(folder_temp);
        }
    }

    /**
     * @param directoryPath 路径
     * @return 是否有文件夹
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * @param filePath 路径
     * @return 是否创建成功
     */
    public static boolean makeDirs(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }

        File folder = new File(filePath);
        return (folder.exists() && folder.isDirectory())
                || folder.mkdirs();
    }

    /**
     * 保存图片至手机(copy)
     */
    public static void savePhoto(File file, File toFile) {
        try {
            FileUtil.copyFile(file, toFile);
            Toast.makeText(x.app(), "保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(x.app(), "保存图片失败啦,无法下载图片", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 文件复制(采用FileChannel方法)
     *
     * @param fileFrom 源文件
     * @param fileTo   目标文件
     * @throws Exception
     */
    private static long copyFile(File fileFrom, File fileTo) throws Exception {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(fileFrom);
        FileOutputStream out = new FileOutputStream(fileTo);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        ByteBuffer b;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return new Date().getTime() - time;
            }
            if ((inC.size() - inC.position()) < length) {
                length = (int) (inC.size() - inC.position());
            } else
                length = 2097152;
            b = ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }

    /**
     * 保存Bitmap到本地
     *
     * @param mBitmap
     * @param bitName
     * @return
     */
    public static String saveBitmap(Bitmap mBitmap, String bitName) {
        String filePath;
        String path = Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_TEMP;

        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File f = new File(path + "/" + bitName + ".jpg");
        filePath = f.getPath();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            filePath = "";
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            filePath = "";
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            filePath = "";
            e.printStackTrace();
        }

        return filePath;
    }
}