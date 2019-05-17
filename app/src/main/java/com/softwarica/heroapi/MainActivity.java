package com.softwarica.heroapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name, desc;
    private Button btnadd;
    private ListView listView;
    private Retrofit retrofit;
    private Interface anInterface;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getConnection();
        LoadData();
    }

    private void getConnection() {
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").addConverterFactory(GsonConverterFactory.create()).build();
        anInterface = retrofit.create(Interface.class);
    }

    private void init() {
        name = findViewById(R.id.etName);
        desc = findViewById(R.id.etDesc);
        btnadd = findViewById(R.id.btnadd);
        btnadd.setOnClickListener(this);
        listView = findViewById(R.id.listView);
    }

    private void setAdapter(){
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
    }
    private void LoadData(){
        Call<List<Model>> data = anInterface.getAllData();
        data.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Could not get Data From the Server", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                List<Model> data = response.body();
                for(Model m : data){
                    list.add(String.format("Name : %s \n Desc : %s",m.getName(),m.getDescription()));
                }

                setAdapter();
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PutData(String name, String desc){
        Call<Void> send = anInterface.setData(new Model("1",name,desc));
        send.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Data successfully inserted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to send data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
    if(v.getId() == R.id.btnadd){
        if(TextUtils.isEmpty(name.getText())){
            name.setError("Please enter name");
            return;
        }
        if(TextUtils.isEmpty(desc.getText())){
            desc.setError("Please enter description");
            return;
        }
        String Name = name.getText().toString();
        String Desc = desc.getText().toString();

        PutData(Name,Desc);
        list.clear();
        LoadData();
    }
    }
}
