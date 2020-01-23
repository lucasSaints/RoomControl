package com.wise.roomcontrol;

import android.util.Log;
import android.widget.Toast;

import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.User;

import java.util.ArrayList;
import java.util.List;

public class Dao {
    static public List<User> users = new ArrayList<>();
    static public List<Empresa> empresas = new ArrayList<>();

    public void validaLogin(String email, String senha){
        for (User i:users) {
            if(i.getMail()==email) {
                if (i.getSenha() == senha){
                    Log.i("teste", "validaLogin: deu certo");
                }
                    //Toast.makeText(LoginActivity.class,R.string.senha_incorreta,Toast.LENGTH_LONG);
            }
                //Toast.makeText(LoginActivity.class,R.string.invalid_email,Toast.LENGTH_LONG);
        }
    }
}
