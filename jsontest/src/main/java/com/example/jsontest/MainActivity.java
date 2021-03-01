package com.example.jsontest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView mDateTextView;
    TextView mTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDateTextView = (TextView)findViewById(R.id.textview_date);
        mTimeTextView = (TextView)findViewById(R.id.textview_time);
    }

    public void fetchDateTime(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://date.jsontest.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsontestAPI api = retrofit.create(JsontestAPI.class);
        Call<ServerTime> serverTimeCall = api.getServerDateTime();
        serverTimeCall.enqueue(new Callback<ServerTime>() {
            @Override
            public void onResponse(Call<ServerTime> call, Response<ServerTime> response) {
                ServerTime serverTime = response.body();
                mDateTextView.setText("Date:" + serverTime.getDate());
                mTimeTextView.setText("Time: " + serverTime.getTime());
            }

            @Override
            public void onFailure(Call<ServerTime> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}