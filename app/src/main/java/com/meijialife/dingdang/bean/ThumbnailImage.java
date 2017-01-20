package com.meijialife.dingdang.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 */
@Table(name = "thumbnail_images")
public class ThumbnailImage {

    /**
     * {
     * "width": 200,
     * "height": 200,
     * "thumbnailBaseUri": "http://i0.letvimg.com/lc31_webdiskthumbs/201610/09/19/13/4f29e04ddefd8bdc794719f9c80c51f650e3058b/thumb/1_bak.jpg"
     * }
     */
    @Column(name = "id", isId = true)
    private String id;

    @SerializedName("width")
    @Expose
    @Column(name = "width")
    private int width;

    @SerializedName("height")
    @Expose
    @Column(name = "height")
    private int height;

    @SerializedName("thumbnailBaseUri")
    @Expose
    @Column(name = "thumbnailBaseUri")
    private String thumbnailBaseUri;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThumbnailBaseUri() {
        return thumbnailBaseUri;
    }

    public void setThumbnailBaseUri(String thumbnailBaseUri) {
        this.thumbnailBaseUri = thumbnailBaseUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}