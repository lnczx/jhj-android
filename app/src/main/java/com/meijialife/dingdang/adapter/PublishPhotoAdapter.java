package com.meijialife.dingdang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.OrderDetailActivity;
import com.meijialife.dingdang.activity.PhotoPreviewActivity;
import com.meijialife.dingdang.utils.image.loader.ImageLoader;
import com.meijialife.dingdang.utils.image.loader.ImageLoaderUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 发表动态,图片adapter
 * Created by jiangjianxiong on 16/6/15.
 */
public class PublishPhotoAdapter extends BaseAdapter {

    private static final int TYPE_ADD = 0;//+号
    private static final int TYPE_NORMAL = 1;//正常图片

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> mList = new ArrayList<>();

    private boolean isFull = Boolean.FALSE;

    public PublishPhotoAdapter(Context ctx, ArrayList<String> list) {
        this.inflater = LayoutInflater.from(ctx);
        this.mContext = ctx;
        this.mList = list;
        if (mList.size() == 9) {
            isFull = true;
        }
    }

    @Override
    public int getCount() {//判断长度(加号)
        if (mList != null && !mList.isEmpty()) {
            if (mList.size() != 9) {
                return mList.size() + 1;
            } else {
                isFull = true;
                return 9;
            }
        } else {
            return 1;
        }
    }

    @Override
    public String getItem(int position) {
        if (null != mList && !mList.isEmpty()) {
            if (mList.size() != 9) {
                if (position != getCount() - 1) {
                    return mList.get(position);
                } else {
                    return "add";
                }
            } else {
                return mList.get(position);
            }
        } else {
            return "add";
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {//判断类型(图片or加号)
        if (getCount() != 1) {
            if (!isFull) {
                if (position != getCount() - 1) {
                    return TYPE_NORMAL;
                } else {
                    return TYPE_ADD;
                }
            } else {
                return TYPE_NORMAL;
            }
        } else {
            return TYPE_ADD;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_NORMAL:
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.publish_photo_grid_item, parent, false);
                    holder.image = (SimpleDraweeView) convertView.findViewById(R.id.image);
                    holder.del = (ImageView) convertView.findViewById(R.id.del);

                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, PhotoPreviewActivity.class);
                            intent.putExtra("position", position);
                            intent.putExtra("isLocal", true);
                            intent.putStringArrayListExtra("data", mList);
                            ((OrderDetailActivity) mContext).startActivityForResult(intent, OrderDetailActivity.REQUEST_IMAGE_VIEW);
                        }
                    });

                    holder.del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((OrderDetailActivity) mContext).deleteSinglePhoto(position);
                        }
                    });

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                File imageFile = new File(getItem(position));
                if (imageFile.exists() && null != holder) {
                    ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder()
                            .path(getItem(position)).widthAndHeight(200).placeHolder(R.mipmap.default_image_bg)
                            .mImageView(holder.image).build());
                }

                return convertView;

            case TYPE_ADD:
                View view_add = inflater.inflate(R.layout.publish_photo_add_grid_item, parent, false);
                ImageView image = (ImageView) view_add.findViewById(R.id.image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((OrderDetailActivity) mContext).pickImage();
                    }
                });
                return view_add;
        }
        return convertView;
    }

    private class ViewHolder {
        SimpleDraweeView image;
        ImageView del;
    }

}