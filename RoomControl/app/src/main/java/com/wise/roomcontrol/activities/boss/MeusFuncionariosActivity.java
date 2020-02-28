package com.wise.roomcontrol.activities.boss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.activities.MainActivity;
import com.wise.roomcontrol.adapters.UserAdapter;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.wise.roomcontrol.Dao.playTickSound;

public class MeusFuncionariosActivity extends AppCompatActivity {

    private UserAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slaves);
        adapter = new UserAdapter(MeusFuncionariosActivity.this);
        adapter.atualiza();
        ListView lista = findViewById(R.id.lislaves);
        lista.setAdapter(adapter);
        registerForContextMenu(lista);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_main, menu);
        menu.findItem(R.id.removedor).setTitle("Gerenciar membro");
    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        playTickSound(MeusFuncionariosActivity.this);
        int itemId = item.getItemId();
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(itemId==R.id.removedor){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setPositiveButton(getString(R.string.confirmar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Dao dao = new Dao();
                                System.out.println(dao.ServerInOutput(true, "usuario/remover", new String[]{"id"}, new Integer[]{adapter.getItem(menuInfo.position).getId()}));
                                adapter.atualiza();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).setNegativeButton("Não", null);
            if(adapter.getItem(menuInfo.position).isAtivo())
                builder.setTitle("Remover membro").setMessage("Bloquear "+adapter.getItem(menuInfo.position).getUser()+"?");
            else
                builder.setTitle("Adicionar membro").setMessage("Desbloquear "+adapter.getItem(menuInfo.position).getUser()+"?");
            builder.show();
        }
        return super.onContextItemSelected(item);
    }

}
