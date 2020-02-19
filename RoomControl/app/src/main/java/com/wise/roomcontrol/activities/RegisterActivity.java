package com.wise.roomcontrol.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.adapters.AuxAdapter;
import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.classes.Empresa;

//import org.json.JSONArray;
//import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText campoLogin;
    private EditText campoSenha;
    private EditText campoUser;
    int empSelected;
    private String loginDom="";
    final private Dao dao=new Dao();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()== NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!status){
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Não foi possível conectar-se a Internet. Certifique-se que a conexão wi-fi ou dados móveis esteja(m) ativada(os).").setTitle("Erro de conexão").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
        }
        final List<Empresa> lista = new ArrayList();

        final Spinner spi = findViewById(R.id.spinner);

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
                botao.setEnabled(false);
                spi.setEnabled(false);
                Log.i("teste", "clicado");
                if(!campoSenha.getText().toString().isEmpty()&&campoSenha.getText().toString().length()>=3) {
                    if(!campoLogin.getText().toString().isEmpty()&&!campoUser.getText().toString().isEmpty()) {
                        if (dao.validaCadastro(campoLogin.getText().toString(), campoSenha.getText().toString(), campoUser.getText().toString(), empSelected).equals("Usuário criado com sucesso")) {
                            Log.i("teste", "cadastro validado");
                            finish();
                        } else {
                            findViewById(R.id.loadingBar).setVisibility(View.GONE);
                            botao.setEnabled(true);
                            spi.setEnabled(true);
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, getString(R.string.preencha_corretamente),Toast.LENGTH_SHORT);
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, getString(R.string.senha_min_3_carac),Toast.LENGTH_SHORT);
                }
            }
        });
        botao.setEnabled(false);


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
                if(!hasFocus&&campoLogin.getText().toString().contains("@")&&!loginDom.equals(campoLogin.getText().toString().substring(campoLogin.getText().toString().indexOf('@')+1))) {
                    String loginAuxiliar = campoLogin.getText().toString();
                    //if(loginAuxiliar.contains("@")){
                        loginDom = loginAuxiliar.substring(loginAuxiliar.indexOf('@')+1);
                        if(loginDom.contains(".")&&loginDom.length()>loginDom.indexOf('.')+2){
                            Log.i("teste", "onFocusChange: entrou");
                            String[] domArray={loginDom};
                            adapterAux.atualiza(domArray);
                            //ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, adapterAux.getAsString(1));
                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //adapterAux.setTipo(adapterAux.EMPRESAS);
                            AuxAdapter adapter=new AuxAdapter(adapterAux.getListaEmpresas(),RegisterActivity.this);
                            spi.setAdapter(adapter);
                            spi.setVisibility(View.VISIBLE);
                        }
                    //}
                }
            }
        });


    }


}
