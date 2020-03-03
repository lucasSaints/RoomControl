package com.wise.roomcontrol.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.activities.boss.MeusFuncionariosActivity;
import com.wise.roomcontrol.activities.boss.MinhaEmpresaActivity;
import com.wise.roomcontrol.activities.boss.MinhasSalasActivity;
import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.adapters.ListaReunioesAdapter;
import com.wise.roomcontrol.classes.Sala;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.wise.roomcontrol.Dao.empLogada;
import static com.wise.roomcontrol.Dao.playTickSound;
import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {
    private ListaReunioesAdapter adapter=new ListaReunioesAdapter(MainActivity.this);
    private ListaOpcoesAdapter adapterSalas=new ListaOpcoesAdapter(MainActivity.this);
    final private Dao dao=new Dao();
    private int toques=0;
    private static long msec=0;
    public static boolean onlyLogado;
    public static List<Boolean> salasNoFiltro = new ArrayList<>();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);
        prefs=getApplicationContext().getSharedPreferences("Descartes",MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        onlyLogado=false;
        /*if(onlyLogado)
            setTitle("Minhas reuniões");
        else*/
            setTitle("Reuniões marcadas");
        adapter = new ListaReunioesAdapter(MainActivity.this);
        adapter.setSoPlayer(onlyLogado);
        adapter.atualiza();
        adapterSalas = new ListaOpcoesAdapter(MainActivity.this);
        adapterSalas.refresco();
        for (Sala i:adapterSalas.salasNaEmpresa) {
            salasNoFiltro.add(true);
        }
        ListView lista = findViewById(R.id.listareunioes);
        registerForContextMenu(lista);
        lista.setAdapter(adapter);
        /*lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });*/

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
        try {
            if (dao.ServerInOutput(false, "usuario/checkBoss", new String[]{"id"}, new Integer[]{dao.logado.getId()}).contains("true")) {
                Log.i("teste", "onCreate: eh tetra");
                NavigationView navigationView = findViewById(R.id.nav);
                navigationView.setVisibility(View.VISIBLE);
                navigationView.getMenu().findItem(R.id.mysalas).setTitle(getString(R.string.geren)+" "+getString(R.string.salas));
                navigationView.getMenu().findItem(R.id.myslaves).setTitle(getString(R.string.geren)+" "+getString(R.string.members));
                if(Build.VERSION.SDK_INT<17 || displayMetrics.widthPixels<300)
                    navigationView.getMenu().findItem(R.id.myslaves).setVisible(false);
                TextView emp = navigationView.getHeaderView(0).findViewById(R.id.incName);
                emp.setText(empLogada.getNome());
                TextView end = navigationView.getHeaderView(0).findViewById(R.id.incEndre);
                end.setText(empLogada.getEndereco());
                try{
                    navigationView.getHeaderView(0).findViewById(R.id.imageInc).setBackgroundColor(Color.parseColor(prefs.getString("color", null)));
                }catch (Exception e){
                    e.printStackTrace();
                }
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.myinc) {
                        startActivity(new Intent(MainActivity.this, MinhaEmpresaActivity.class));
                    } else if (menuItem.getItemId() == R.id.mysalas) {
                        startActivity(new Intent(MainActivity.this, MinhasSalasActivity.class));
                    } else if (menuItem.getItemId() == R.id.myslaves) {
                        startActivity(new Intent(MainActivity.this, MeusFuncionariosActivity.class));
                    }
                    return false;
                    }
                });
            }else{
                findViewById(R.id.slidin).setVisibility(View.GONE);
                findViewById(R.id.nav).setVisibility(View.GONE);
                findViewById(R.id.handleDrawer).setVisibility(View.GONE);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_main,menu);
        invalidateOptionsMenu();
        if(!onlyLogado) {
            menu.findItem(R.id.switchreservas).setTitle("Minhas reuniões");
            setTitle("Reuniões marcadas");
        }else {
            menu.findItem(R.id.switchreservas).setTitle("Reuniões marcadas");
            setTitle("Minhas reuniões");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        playTickSound(MainActivity.this);
        if(item.getItemId() == R.id.switchreservas) {
            onlyLogado=!onlyLogado;
            adapter.setSoPlayer(onlyLogado);
            adapter.atualiza();
        }else{
            AlertDialog.Builder bob = new AlertDialog.Builder(MainActivity.this);
            CharSequence[] charseq = new CharSequence[adapterSalas.getAsString(adapterSalas.SALAS).size()];
            for(int i = 0; i < charseq.length; i++){
                charseq[i] = adapterSalas.getAsString(adapterSalas.SALAS).get(i);
            }
            boolean[] booleane = new boolean[salasNoFiltro.size()];
            for(int i = 0; i < booleane.length; i++){
                booleane[i] = salasNoFiltro.get(i);
            }
            bob.setMultiChoiceItems(charseq, booleane, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    salasNoFiltro.set(which, isChecked);
                    adapter.atualiza();
                }
            }).setPositiveButton("Feito", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.atualiza();
                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(msec+1200>currentTimeMillis()) {
            toques++;
        }else{
            toques=0;
            msec=currentTimeMillis();
        }
        if(toques==0){
            Toast.makeText(MainActivity.this, "Toque novamente para deslogar",Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_main, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        playTickSound(MainActivity.this);
        int itemId = item.getItemId();
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(itemId==R.id.removedor){
            if(dao.logado.getId()==adapter.getItem(menuInfo.position).getLocadorId()) {
                new AlertDialog.Builder(this).setTitle("Cancelar reunião")
                        .setMessage("Esta ação é irreversível. Tem certeza que deseja continuar?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Log.i("teste", "onClick: getItemId(menuInfo.position) = "+adapter.getItemId(menuInfo.position));
                                    //dao.ServerInOutput(true, "reserva/cancelar", new String[]{"id"}, new Long[]{adapter.getItemId(menuInfo.position)});

                                    StringBuilder result = new StringBuilder();
                                    URL url = new URL(dao.urlWS+"reserva/cancelar");
                                    System.out.println(url);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("POST");
                                    conn.setDoOutput(true);
                                    conn.setRequestProperty("authorization", "secret");
                                    conn.setRequestProperty("id", String.valueOf((Object) adapter.getItemId(menuInfo.position)));
                                    conn.connect();
                                    dao.getResultString(conn);
                                    adapter.atualiza();
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("Não", null).show();
            }else{
                Toast.makeText(MainActivity.this,"Você não pode cancelar reuniões marcadas por outros usuários",Toast.LENGTH_LONG).show();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.atualiza();
    }
}
