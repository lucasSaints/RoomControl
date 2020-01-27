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
    static public boolean logado;

    public boolean validaLogin(String email, String senha){
        for (User i:users) {
            Log.i("teste", "email: "+i.getMail());
            if(i.getMail().equals(email)) {
                if (i.getSenha().equals(senha)){
                    Log.i("teste", "validaLogin: deu certo");
                    return true;
                }else
                    Log.i("teste", "validaLogin: senha incorreta");
                    //Toast.makeText(LoginActivity.class,R.string.senha_incorreta,Toast.LENGTH_LONG);
            }
                //Toast.makeText(LoginActivity.class,R.string.invalid_email,Toast.LENGTH_LONG);
        }
        Log.i("teste", "validaLogin: email não cadastrado");
        return false;
    }

    public boolean validaCadastro(String email, String senha, String user){
        String dominio="kkkkk";
        boolean deu=false;
        int aux = email.length();
        for (int i = 0; i < aux; i++) {
            if(email.charAt(i)=='@'&&aux>i+2) {
                dominio=email.substring(i+1);
                deu=true;
            }
        }
        if(deu) {
            if (dominio != "gmail.com" && dominio != "hotmail.com" && dominio != "yahoo.com.br" && dominio != "outlook.com") {
                for (Empresa i : empresas) {
                    if (dominio != i.getDominio()) {
                        User usuario = new User(email, senha, user);
                        usuario.setEmpresaById(i.getId());
                        return true;
                    }
                }
                return false;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }
}
