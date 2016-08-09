package com.tacademy.sampleokhttp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tacademy.sampleokhttp.autodata.Product;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tacademy on 2016-08-09.
 */
public class ProductListView extends FrameLayout {
    Product product;

    @BindView(R.id.image_thumbnail)
    ImageView thumbView;

    @BindView(R.id.text_title)
    TextView titleView;

    @BindView(R.id.text_like)
    TextView likeView;

    @BindView(R.id.text_download)
    TextView downloadView;

    @BindView(R.id.text_desc)
    TextView descView;

    public ProductListView(Context context) {
        this(context, null);
    }

    public ProductListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_product_list, this);
        ButterKnife.bind(this);
    }
}
