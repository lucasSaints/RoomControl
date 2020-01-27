package com.wise.roomcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.classes.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText campoLogin;
    private EditText campoSenha;
    private EditText campoUser;
    final private Dao dao=new Dao();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        campoLogin=findViewById(R.id.email);
        campoSenha=findViewById(R.id.password);
        campoUser=findViewById(R.id.username);
        ArrayAdapter<String> aux = new ArrayAdapter<String>(this, R.layout.dialoglist);
        for (int i=0;i<dao.empresas.size();i++){
            aux.add(dao.empresas.get(i).getNome());
            Log.i("teste", "HA "+dao.empresas.get(i).getNome()+" = "+aux.getItem(i));
        }
        final ArrayAdapter<String> nomesEmpresas=aux;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(nomesEmpresas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User usuario = new User(campoLogin.getText().toString(), campoSenha.getText().toString(), campoUser.getText().toString());
                usuario.setEmpresaById(dao.empresas.get(which).getId());
            }
        });
        final Button botao =findViewById(R.id.confirm);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("teste", "HA "+dao.empresas.get(0).getNome()+" = "+nomesEmpresas.getItem(0));
                Log.i("teste", "clicado");
                if(dao.validaCadastro(campoLogin.getText().toString(),campoSenha.getText().toString(),campoUser.getText().toString())){
                    Log.i("teste", "cadastro validado");
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"Não foi possível detectar a qual empresa trabalha. Favor escolher entre uma listada.",Toast.LENGTH_LONG);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
}
