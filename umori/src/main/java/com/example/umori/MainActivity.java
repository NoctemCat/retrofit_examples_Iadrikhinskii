package com.example.umori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    List<UPost> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mPosts = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        UmoriliAdapter adapter = new UmoriliAdapter(mPosts);
        mRecyclerView.setAdapter(adapter);

        UmoriliService umoriliService =UmoriliService.retrofit.create(UmoriliService.class);

        final Call<List<UPost>> call = umoriliService.getData("bash", 50);

        call.enqueue((new Callback<List<UPost>>() {
            @Override
            public void onResponse(Call<List<UPost>> call, Response<List<UPost>> response) {
                if(response.isSuccessful()){
                    mPosts.addAll(response.body());
                    mRecyclerView.getAdapter().notifyDataSetChanged();

                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    ResponseBody errorBody = response.errorBody();
                    try{
                        Toast.makeText(MainActivity.this, errorBody.string(), Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UPost>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something has gone wrong", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }));
    }
}