package com.wise.roomcontrol.activities.boss;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;

public class MinhasSalas extends AppCompatActivity {

    final private Dao dao=new Dao();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getApplicationContext().getSharedPreferences("Descartes",MODE_PRIVATE);
        editor=prefs.edit();
        setContentView(R.layout.activity_salas);

        try{
            //prefs.getString("color", null)
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
