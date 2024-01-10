package com.example.cmpt276_2021_7_manganese;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

public class GlideEngine implements ImageEngine {

    /**
     * load picture
     *
     * @param context   context
     * @param url       resource url
     * @param imageView Picture bearing control
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * Load the network image adaptation scheme
     *
     * @param context       context
     * @param url           resource url
     * @param imageView     Picture bearing control
     * @param longImageView Long diagrams host controls
     * @param callback      Network picture load callback listener
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url,
                          @NonNull ImageView imageView, SubsamplingScaleImageView longImageView,
                          OnImageCompleteCallback callback) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onShowLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                        boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                resource.getHeight());
                        longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                        imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                        if (eqLongImage) {
                            // load long picture
                            longImageView.setQuickScaleEnabled(true);
                            longImageView.setZoomEnabled(true);
                            longImageView.setDoubleTapZoomDuration(100);
                            longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                            longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                            longImageView.setImage(ImageSource.cachedBitmap(resource),
                                    new ImageViewState(0, new PointF(0, 0), 0));
                        } else {
                            // normal picture
                            imageView.setImageBitmap(resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * load photo directory
     *
     * @param context   context
     * @param url       photo path
     * @param imageView load picture ImageView
     */
    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .override(180, 180)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .placeholder(R.drawable.picture_image_placeholder)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    /**
     * load picture list
     *
     * @param context   context
     * @param url       photo path
     * @param imageView load picture ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (!ImageLoaderUtils.assertValidRequest(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .override(200, 200)
                .centerCrop()
                .placeholder(R.drawable.picture_image_placeholder)
                .into(imageView);
    }


    private GlideEngine() {
    }

    private static GlideEngine instance;

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }
}
