package com.wise.roomcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.classes.User;

public class LoginActivity extends AppCompatActivity {

    private EditText campoLogin;
    private EditText campoSenha;
    final private Dao dao=new Dao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campoLogin=findViewById(R.id.email);
        campoSenha=findViewById(R.id.password);
        final Button botaologin = findViewById(R.id.login);
        botaologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campoLogin.getText().toString()!=null&&campoSenha.getText().toString()!=null){
                    String login = campoLogin.getText().toString();
                    String password = campoSenha.getText().toString();
                    Log.i("teste", "login: "+login+"    senha: "+password);
                    if(dao.validaLogin(login, password)) {
                        dao.logado=true;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else
                        Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
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
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
}
