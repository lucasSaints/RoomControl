package com.wise.roomcontrol.activities.boss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.classes.Sala;

import static com.wise.roomcontrol.Dao.playTickSound;

public class MinhasSalasActivity extends AppCompatActivity {

    final private Dao dao=new Dao();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ListaOpcoesAdapter adapter=new ListaOpcoesAdapter();
    private TextView nome;
    private TextView local;
    private TextView dataAlt;
    private TextView dataCria;
    private TextView pcs;
    private TextView capac;
    private CheckBox ar;
    private CheckBox proj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getApplicationContext().getSharedPreferences("Inc",MODE_PRIVATE);
        editor=prefs.edit();
        setContentView(R.layout.activity_salas);
        Toolbar tolbar = findViewById(R.id.tolbar);
        setSupportActionBar(tolbar);
        ImageView fundo = findViewById(R.id.fundoSalas);
        try{
            tolbar.setBackgroundColor(Color.parseColor(prefs.getString("color", null)));
            fundo.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor(prefs.getString("color", null)),Color.parseColor("#171717"),0.42f));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(ColorUtils.calculateLuminance(((ColorDrawable) tolbar.getBackground()).getColor())>0.6)
            tolbar.setTitleTextColor(Color.parseColor("#0E0017"));
        else
            tolbar.setTitleTextColor(Color.parseColor("#efefef"));
        setTitle("Minhas salas");
        nome = findViewById(R.id.selectedSalaName);
        local = findViewById(R.id.selectedSalaAndar);
        dataAlt = findViewById(R.id.selectedSalaDataAlteracao);
        dataCria = findViewById(R.id.selectedSalaDataCriacao);
        pcs = findViewById(R.id.selectedSalaPCs);
        capac = findViewById(R.id.selectedSalaPessoas);
        ar = findViewById(R.id.selectedSalaArcondicionado);
        proj = findViewById(R.id.selectedSalaProjetor);
        atualizaResumoSala(null);
        ListView lista = findViewById(R.id.listaSalas);
        adapter.filtraSalas(dao.logado.getEmpresaId(),0,false,false);
        ArrayAdapter<String> adapterView = new ArrayAdapter<>(MinhasSalasActivity.this, R.layout.item_sala, adapter.getAsString(2));
        lista.setAdapter(adapterView);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                atualizaResumoSala(adapter.getItem(position));
            }
        });

        Button botone = findViewById(R.id.addSalaButton);
        botone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MinhasSalasActivity.this,FormSalaActivity.class));
            }
        });
    }

    private void atualizaResumoSala(Sala sala){
        if(sala==null){
            nome.setText("");
            local.setText("");
            dataAlt.setText("Data de alteração: ");
            dataCria.setText("Data de criação: ");
            pcs.setText("PCs: ");
            capac.setText("Capacidade: ");
            ar.setChecked(false);
            proj.setChecked(false);
        }else{
            nome.setText(sala.getName());
            local.setText(sala.getAndar());
            dataAlt.setText("Data de alteração: "+sala.getDataAlteracao());
            dataCria.setText("Data de criação: "+sala.getDataCriacao());
            pcs.setText("PCs: "+sala.getPcs());
            capac.setText("Capacidade: "+sala.getQuantPessoas());
            ar.setChecked(sala.temAc());
            proj.setChecked(sala.temProjetor());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_salas,menu);
        invalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        playTickSound(MinhasSalasActivity.this);
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
