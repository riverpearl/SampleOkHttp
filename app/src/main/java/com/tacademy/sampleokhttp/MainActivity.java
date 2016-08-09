package com.tacademy.sampleokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tacademy.sampleokhttp.autodata.Product;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edit_keyword)
    TextView keywordView;
    @BindView(R.id.rv_product_list)
    RecyclerView listView;

    ProductAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pAdapter = new ProductAdapter();
        pAdapter.setOnProductAdapterItemClickListener(new ProductAdapter.OnProductAdapterItemClickListener() {
            @Override
            public void onProductAdapterItemClick(View view, Product product, int position) {
                Toast.makeText(MainActivity.this, product.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(pAdapter);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        listView.setLayoutManager(manager);
    }

    @OnClick(R.id.btn_search)
    public void onSearch(View view) {

    }
}
