package com.meijialife.dingdang.utils;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.meijialife.dingdang.bean.UpdateInfo;

/**
 * 获取更新信息
 * 
 * @author piao
 * 
 */
public class UpdateInfoProvider {
    //解析xml文件
    public  static UpdateInfo getUpdateInfo(InputStream is) {
        XmlPullParser parser = Xml.newPullParser();
        UpdateInfo info = new UpdateInfo();
        // 初始化解析器
        try {
            parser.setInput(is, "utf-8");
            int type = parser.getEventType();

            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                case XmlPullParser.START_TAG:
                    if ("version".equals(parser.getName())) {
                        String version = parser.nextText();
                        info.setVersion(version);
                    } else if ("description".equals(parser.getName())) {
                        String description = parser.nextText();
                        info.setDescription(description);
                    } else if ("path".equals(parser.getName())) {
                        String path = parser.nextText();
                        info.setPath(path);
                    }
                    break;
                }
                type = parser.next();
            }
            return info;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}