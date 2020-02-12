package com.wise.roomcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // menu.add("Remover");
    }

    //@Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        int itemId = item.getItemId();
        CharSequence tituloDoMenu = item.getTitle();
        if(itemId==R.id.removedor){
            new AlertDialog.Builder(this).setTitle("Cancelar reunião")
                    .setMessage("Esta ação é irreversível. Tem certeza que deseja continuar?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                            try {
                                dao.ServerInOutput(true,"reserva/cancelar",new String[]{"id"},new Long[]{adapter.getItemId(which)});
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("Não",null).show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.atualiza();
    }
}
