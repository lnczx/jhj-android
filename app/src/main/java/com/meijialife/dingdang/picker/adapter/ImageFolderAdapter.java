package com.meijialife.dingdang.picker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.picker.bean.Folder;
import com.meijialife.dingdang.utils.image.loader.ImageLoader;
import com.meijialife.dingdang.utils.image.loader.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 文件夹Adapter
 * Created by andye

 */
public class ImageFolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<Folder> mFolders = new ArrayList<>();

    private int lastSelectedPosition = 0;//上次所选位置

    public ImageFolderAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置数据集
     *
     * @param folders 文件夹集合
     */
    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
        } else {
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size() + 1;
    }

    @Override
    public Folder getItem(int i) {
        if (i == 0) {
            return null;
        }
        return mFolders.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.mis_list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (i == 0) {
                holder.mTvName.setText(R.string.mis_folder_all);
                holder.mTvPath.setText("/sdcard");
                holder.mTvSize.setText(String.format("%d%s",
                        getTotalImageSize(), mContext.getResources().getString(R.string.mis_photo_unit)));
                if (mFolders.size() > 0) {
                    Folder f = mFolders.get(0);
                    if (f != null) {
                        ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder()
                                .path(f.mCover.path).widthAndHeight(R.dimen.mis_folder_cover_size)
                                .placeHolder(R.drawable.mis_default_error)
                                .mImageView(holder.mCover).build());
                    } else {
                        ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder()
                                .widthAndHeight(R.dimen.mis_folder_cover_size)
                                .placeHolder(R.drawable.mis_default_error)
                                .mImageView(holder.mCover).build());
                    }
                }
            } else {
                holder.bindData(getItem(i));
            }
            if (lastSelectedPosition == i) {
                holder.mIndicator.setVisibility(View.VISIBLE);
            } else {
                holder.mIndicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (Folder f : mFolders) {
                result += f.mImages.size();
            }
        }
        return result;
    }

    public int getSelectIndex() {
        return lastSelectedPosition;
    }

    public void setSelectIndex(int i) {
        if (lastSelectedPosition == i) return;

        lastSelectedPosition = i;
        notifyDataSetChanged();
    }

    class ViewHolder {
        SimpleDraweeView mCover;
        TextView mTvName;
        TextView mTvPath;
        TextView mTvSize;
        ImageView mIndicator;

        ViewHolder(View view) {
            mCover = (SimpleDraweeView) view.findViewById(R.id.cover);
            mTvName = (TextView) view.findViewById(R.id.name);
            mTvPath = (TextView) view.findViewById(R.id.path);
            mTvSize = (TextView) view.findViewById(R.id.size);
            mIndicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(Folder data) {
            if (data == null) {
                return;
            }
            mTvName.setText(data.name);
            mTvPath.setText(data.path);
            if (data.mImages != null) {
                mTvSize.setText(String.format("%d%s", data.mImages.size(), mContext.getResources().getString(R.string.mis_photo_unit)));
            } else {
                mTvSize.setText("*" + mContext.getResources().getString(R.string.mis_photo_unit));
            }

            if (data.mCover != null) {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder()
                        .path(data.mCover.path).widthAndHeight(R.dimen.mis_folder_cover_size)
                        .placeHolder(R.drawable.mis_default_error)
                        .mImageView(mCover).build());
            } else {
                ImageLoaderUtil.getInstance().loadImage(mContext, new ImageLoader.Builder()
                        .widthAndHeight(R.dimen.mis_folder_cover_size)
                        .placeHolder(R.drawable.mis_default_error)
                        .mImageView(mCover).build());
            }
        }
    }

}