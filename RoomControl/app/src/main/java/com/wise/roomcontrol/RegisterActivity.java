package com.wise.roomcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.User;

//import org.json.JSONArray;
//import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText campoLogin;
    private EditText campoSenha;
    private EditText campoUser;
    int empSelected;
    final private Dao dao=new Dao();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final ListaOpcoesAdapter adapterAux=new ListaOpcoesAdapter(RegisterActivity.this);
        campoLogin=findViewById(R.id.email);
        campoSenha=findViewById(R.id.password);
        campoUser=findViewById(R.id.username);
        /*ArrayAdapter<String> aux = new ArrayAdapter<>(this, R.layout.dialoglist);
        for (int i=0;i<dao.empresas.size();i++){
            aux.add(dao.empresas.get(i).getNome());
            Log.i("teste", "HA "+dao.empresas.get(i).getNome()+" = "+aux.getItem(i));
        }
        final ArrayAdapter<String> nomesEmpresas=aux;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Não foi possível detectar a qual empresa trabalha. Favor escolher entre uma listada");
        builder.setAdapter(nomesEmpresas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User usuario = new User(campoLogin.getText().toString(), campoSenha.getText().toString(), campoUser.getText().toString());
                usuario.setEmpresaById(dao.empresas.get(which).getId());
            }
        });*/
        final Button botao =findViewById(R.id.confirm);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingBar).setVisibility(View.VISIBLE);
                Log.i("teste", "clicado");
                if(dao.validaCadastro(campoLogin.getText().toString(),campoSenha.getText().toString(),campoUser.getText().toString(),empSelected).equals("Usuário criado com sucesso")){
                    Log.i("teste", "cadastro validado");
                    finish();
                }else{
                    findViewById(R.id.loadingBar).setVisibility(View.GONE);
                    /*AlertDialog alert = builder.create();
                    alert.show();*/
                }
            }
        });
        botao.setEnabled(false);

        final List<Empresa> lista = new ArrayList();

        final Spinner spi = findViewById(R.id.spinner);
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               empSelected=adapterAux.getListaEmpresas().get(position).getId();
               botao.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        campoLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    String loginAuxiliar = campoLogin.getText().toString();
                    if(loginAuxiliar.contains("@")){
                        String loginDom = loginAuxiliar.substring(loginAuxiliar.indexOf('@')+1);
                        if(loginDom.contains(".")&&loginDom.length()>loginDom.indexOf('.')+2){
                            Log.i("teste", "onFocusChange: entrou");
                            String[] domArray={loginDom};
                            adapterAux.atualiza(domArray);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, adapterAux.getAsString(1));
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spi.setAdapter(adapter);
                            spi.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });


    }


}
