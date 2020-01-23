package com.wise.roomcontrol;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        Button botaologin = findViewById(R.id.login);
        botaologin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("teste", "onClickou ");
                String login=campoLogin.getText().toString();
                String password=campoSenha.getText().toString();
                dao.validaLogin(login,password);
            }
        });
    }
}
