package com.tacademy.sampleokhttp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tacademy.sampleokhttp.autodata.Product;

/**
 * Created by Tacademy on 2016-08-09.
 */
public class ProductListViewHolder extends RecyclerView.ViewHolder {
    Product product;
    TextView titleView, likeView, downloadView, descView;

    public interface OnProductItemClickListener {
        public void onProductItemClick(View view, Product product, int position);
    }

    OnProductItemClickListener pListener;
    public void setOnProductItemClickListener(OnProductItemClickListener listener) {
        pListener = listener;
    }

    public ProductListViewHolder(final View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pListener != null)
                    pListener.onProductItemClick(itemView, product, getAdapterPosition());
            }
        });

        titleView = (TextView)itemView.findViewById(R.id.text_title);
        likeView = (TextView)itemView.findViewById(R.id.text_like);
        downloadView = (TextView)itemView.findViewById(R.id.text_download);
        descView = (TextView)itemView.findViewById(R.id.text_desc);
    }

    public void setProduct(Product product) {
        this.product = product;

        titleView.setText(product.getName());
        likeView.setText(product.getScore() + "");
        downloadView.setText(product.getDownloadCount() +"");
        descView.setText(product.getDescription());
    }
}
