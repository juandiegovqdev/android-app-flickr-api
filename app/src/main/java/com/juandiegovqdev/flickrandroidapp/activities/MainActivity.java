package com.juandiegovqdev.flickrandroidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.juandiegovqdev.flickrandroidapp.R;
import com.juandiegovqdev.flickrandroidapp.clients.FlickrClient;
import com.juandiegovqdev.flickrandroidapp.models.FlickrModel;
import com.juandiegovqdev.flickrandroidapp.models.Item;
import com.juandiegovqdev.flickrandroidapp.services.FlickrServices;
import com.juandiegovqdev.flickrandroidapp.utils.DeviceData;
import com.google.gson.Gson;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context;
    EditText searchKey;
    String keyword;
    String oldKeyword;
    ProgressBar progressBar;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initializeUI();
        initializeDeviceData();
        initializeEditText();
    }

    public void initializeUI() {
        recyclerView = findViewById(R.id.recycler_view);
        searchKey = findViewById(R.id.keyword);
        progressBar = findViewById(R.id.progress_bar);
    }

    public void initializeDeviceData() {
        DeviceData.getInstance().setDisplay(getWindowManager().getDefaultDisplay());
    }

    public void initializeEditText() {
        searchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyword = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!keyword.isEmpty() && !keyword.endsWith(" ") && !keyword.equals(oldKeyword)) {
                    getImages();
                    oldKeyword = keyword;
                }
            }
        });

        searchKey.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void getImages() {
        final FlickrServices apiService = FlickrClient.getInstance().create(FlickrServices.class);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        disposable = apiService.requestForPosts(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(jsonString -> {
                    String json = jsonString.substring(15, jsonString.length() - 1);
                    FlickrModel flickrModel = new Gson().fromJson(json, FlickrModel.class);
                    return flickrModel.items;
                })
                .subscribe(items -> {
                    FastItemAdapter<Item> fastAdapter = new FastItemAdapter<>();
                    fastAdapter.add(items);
                    fastAdapter.withSelectable(true);
                    fastAdapter.withOnClickListener((v, adapter, item, position) -> {
                        try {
                            getSupportActionBar().hide();
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                        String url = item.media.m.replace("_m.jpg", "_b.jpg");
                        Intent imageDetailsIntent = new Intent(MainActivity.this, ImageDetailsActivity.class);
                        imageDetailsIntent.putExtra("author", item.author);
                        imageDetailsIntent.putExtra("description", item.description);
                        imageDetailsIntent.putExtra("title", item.title);
                        imageDetailsIntent.putExtra("link", item.link);
                        imageDetailsIntent.putExtra("date", item.published);
                        imageDetailsIntent.putExtra("url", url);
                        startActivity(imageDetailsIntent);
                        return false;
                    });
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(fastAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }, throwable -> {
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    System.out.println(throwable.getMessage());
                }, () -> {
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() { }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
