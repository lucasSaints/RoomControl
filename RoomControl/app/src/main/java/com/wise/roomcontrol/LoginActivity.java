package com.wise.roomcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.classes.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText campoLogin;
    private EditText campoSenha;
    final private Dao dao=new Dao();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs=getApplicationContext().getSharedPreferences("Descartes",MODE_PRIVATE);
        editor=prefs.edit();
        setContentView(R.layout.activity_login);
        campoLogin=findViewById(R.id.email);
        campoSenha=findViewById(R.id.password);
        final CheckBox caixa=findViewById(R.id.checkBox);
        try{
            if(prefs.getBoolean("lembrarme",false)) {
                campoLogin.setText(prefs.getString("email", null));
                campoSenha.setText(prefs.getString("senha", null));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        final Button botaologin = findViewById(R.id.login);
        botaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.overlay).setVisibility(View.VISIBLE);
                findViewById(R.id.loadingProgressBar).setVisibility(View.VISIBLE);
                if(campoLogin.getText().toString()!=null&&campoSenha.getText().toString()!=null&&campoLogin.getText().toString()!=""&&campoSenha.getText().toString()!=""){
                    String login = campoLogin.getText().toString();
                    String password = campoSenha.getText().toString();
                    Log.i("teste", "login: "+login+"    senha: "+password);
                    if(dao.validaLogin(login, password)) {
                        try{
                            String[] a1={"email"};
                            String[] a2={campoLogin.getText().toString()};
                            JSONObject obj = new JSONObject(dao.ServerInOutput(false, "usuario/getByEmail", a1, a2));
                            int id=obj.getInt("id");
                            String nome = obj.getString("nome");
                            System.out.println(nome);
                            int emp = obj.getJSONObject("idOrganizacao").getInt("id");
                            System.out.println(emp);
                            dao.logado = new User(campoLogin.getText().toString(),campoSenha.getText().toString(),nome,emp,id);
                            editor.putString("email",campoLogin.getText().toString());
                            editor.putString("senha",campoSenha.getText().toString());
                            if(caixa.isChecked()){
                                editor.putBoolean("lembrarme",true);
                            }
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else
                        Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, R.string.login_failed+". Preencha corretamente os campos", Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.overlay).setVisibility(View.GONE);
                findViewById(R.id.loadingProgressBar).setVisibility(View.GONE);
            }
        });
        final Button botaocadas = findViewById(R.id.register);
        botaocadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        final TextView skipper = findViewById(R.id.skip);
        skipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.logado=null;
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dao.logado!=null){
            Toast.makeText(LoginActivity.this,"Sessão finalizada. Favor logar novamente",Toast.LENGTH_LONG);
            dao.logado=null;
        }
    }
}
