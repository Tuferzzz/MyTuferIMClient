package com.tufer.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.tufer.common.R;
import com.tufer.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Tufer
 * @version 1.0.0
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setup(RequestManager manager, Author author) {
        if (author == null)
            return;
        setup(manager, author.getPortrait());
    }


    public void setup(RequestManager manager, String url) {
        setup(manager, R.drawable.default_portrait, url);
    }


    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null)
            url = "";
        manager.load(url)
                .placeholder(resourceId)
                .centerCrop()
                .dontAnimate()
                .into(this);

    }

}
