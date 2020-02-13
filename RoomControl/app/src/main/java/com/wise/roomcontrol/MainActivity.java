package com.wise.roomcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.wise.roomcontrol.adapters.ListaReunioesAdapter;

import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {
    private ListaReunioesAdapter adapter=new ListaReunioesAdapter(MainActivity.this);
    final private Dao dao=new Dao();
    private int toques=0;
    private static long msec=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);
        ListView lista = findViewById(R.id.listareunioes);
        adapter = new ListaReunioesAdapter(MainActivity.this);
        adapter.atualiza();
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
        registerForContextMenu(lista);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // menu.add("Remover");
    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
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
