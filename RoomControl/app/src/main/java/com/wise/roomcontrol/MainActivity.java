package com.wise.roomcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.wise.roomcontrol.adapters.ListaReunioesAdapter;

public class MainActivity extends AppCompatActivity {
    private ListaReunioesAdapter adapter=new ListaReunioesAdapter(MainActivity.this);
    final private Dao dao=new Dao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);
        ListView lista = findViewById(R.id.listareunioes);
        adapter = new ListaReunioesAdapter(MainActivity.this);
        lista.setAdapter(adapter);
        final Button botao = findViewById(R.id.addButton);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dao.logado!=null)
                    startActivity(new Intent(MainActivity.this,FormReuniaoActivity.class));
                else {
                    builder.setTitle("Erro").setMessage("Você precisa estar logado para locar uma sala").show();
                }
            }
        });
    }
}
