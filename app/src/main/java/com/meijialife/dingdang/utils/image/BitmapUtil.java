package com.meijialife.dingdang.utils.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.widget.ImageView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片处理类
 * Created by ye
 */
public class BitmapUtil {

    private final static long MAX_SIZE = 500 * 1024L;//最大允许大小
    private final static int STANDARD_WIDTH = 480;//最大允许大小
    private final static int STANDARD_HEIGHT = 800;//最大允许大小

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    private static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static boolean saveJpgFile(String source, String toPath) {
        File file = new File(source);
        File outFile = new File(toPath);
        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        long outFileSize;
        int inSampleSize = 0;
        try {
            out = new FileOutputStream(toPath);

            int degree = getBitmapDegree(source);

            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            newOpts.inJustDecodeBounds = false;
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;

            if (w > h && w > STANDARD_WIDTH) {
                inSampleSize = newOpts.outWidth / STANDARD_WIDTH;
            } else if (w < h && h > STANDARD_HEIGHT) {
                inSampleSize = newOpts.outHeight / STANDARD_HEIGHT;
            }
            if (inSampleSize <= 0) {
                inSampleSize = 1;
            }
            newOpts.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            outFileSize = outFile.length();
            out.close();
            if (degree != 0) {
                bitmap = rotateBitmapByDegree(bitmap, degree);
            }
            inSampleSize = 100;
            while (outFileSize > MAX_SIZE && inSampleSize != 0) {
                inSampleSize = inSampleSize - 10;
                out = new FileOutputStream(toPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, inSampleSize, out);
                outFileSize = outFile.length();
                closeOutPutStream(out);
            }
            return true;
        } catch (Exception e) {
        } finally {
            closeOutPutStream(out);
        }
        return false;
    }

    private static void closeOutPutStream(FileOutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放图片资源
     */
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 获取图片宽高
     */
    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        return new int[]{options.outWidth, options.outHeight};
    }

    //图片压缩
    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap;

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }
}