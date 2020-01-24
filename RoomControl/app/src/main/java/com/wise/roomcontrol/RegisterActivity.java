package com.wise.roomcontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        final Button botao =findViewById(R.id.confirm);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.validaCadastro(campoLogin.getText().toString(),campoSenha.getText().toString(),campoUser.getText().toString());
            }
        });
    }
}
