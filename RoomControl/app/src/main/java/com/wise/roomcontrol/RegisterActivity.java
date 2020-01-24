package com.wise.roomcontrol;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        final Button botao =findViewById(R.id.confirm);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dao.validaCadastro(campoLogin.getText().toString(),campoSenha.getText().toString(),campoUser.getText().toString())){
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"Não foi possível detectar a qual empresa trabalha. Favor escolher entre uma listada.",Toast.LENGTH_LONG);
                    final Dialog dialog = new Dialog(RegisterActivity.this);
                    dialog.setContentView(R.layout.dialoglist);
                }
            }
        });
    }
}
