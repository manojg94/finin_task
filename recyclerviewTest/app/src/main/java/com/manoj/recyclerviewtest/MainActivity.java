package com.manoj.recyclerviewtest;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.manoj.recyclerviewtest.Adapter.PaginationListener;
import com.manoj.recyclerviewtest.Adapter.recyclerAdapter;
import com.manoj.recyclerviewtest.Adapter.recyclerViewAdapter;
import com.manoj.recyclerviewtest.api.api;
import com.manoj.recyclerviewtest.api.pojo.Data;
import com.manoj.recyclerviewtest.api.pojo.SpecificaData;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.manoj.recyclerviewtest.Adapter.PaginationListener.PAGE_SIZE;
import static com.manoj.recyclerviewtest.Adapter.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements recyclerAdapter {
    public List<SpecificaData> recyclerModels = new ArrayList<>();
    public recyclerViewAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5*1000;
    private ProgressDialog progressbar;
    private static MainActivity mInstance;
    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycler_view);
        layout = findViewById(R.id.layout);

        adapter = new recyclerViewAdapter(recyclerModels, MainActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        movielistList(currentPage);
        recyclerView.addOnScrollListener(new PaginationListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (PAGE_SIZE != 0) {
                    isLoading = true;
                    currentPage++;
                    movielistList(currentPage);
                    Toast.makeText(MainActivity.this, "Loading page " + currentPage, Toast.LENGTH_SHORT).show();
                } else {
                    isLoading = false;
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                String message="";
                if (isConnected(MainActivity.this)) {
                    message="We have internet connection. Good to go.";
                    Log.d("adsfvsdfv", "We have internet connection. Good to go.");
                    movielistList(currentPage);
                    Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();

                } else {
                    message="We have lost internet connection";
                    Log.d("adsfvsdfv", "We have lost internet connection");
                    Snackbar.make(layout, message, Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.colorAccent)).show();


                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, intentFilter);
    }
    private void movielistList(int pagenumber) {
        Snackbar.make(layout, "Loading", Snackbar.LENGTH_LONG).show();
        Retrofit retro = new Retrofit.Builder()
                .baseUrl(api.baseurl)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        api retrfit = retro.create(api.class);
        Call<Data> call;
        call = retrfit.getdata(
                pagenumber, 3);


        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, retrofit2.Response<Data> response) {

                if (response.isSuccessful()) {
                    PAGE_SIZE = response.body().getPerPage();
                    if (response.body().getData().size() == 0) {
                        PAGE_SIZE = 0;
                        Toast.makeText(MainActivity.this, "No Data to display", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        Toast.makeText(MainActivity.this, "SuccessFully ...", Toast.LENGTH_SHORT).show();
                        Log.d("results", response.body().getTotalPages().toString());
                        recyclerModels.add(response.body().getData().get(i));
                    }
                    isLoading = false;
                    adapter.notifyDataSetChanged();
                } else {
                    isLoading = false;
                    Toast.makeText(MainActivity.this, "Failed ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("errorin:", t.getMessage());
                isLoading = false;
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onitemclick(View view, int pos) {
        Toast.makeText(this, recyclerModels.get(pos).getFirstName(), Toast.LENGTH_SHORT).show();

    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}
