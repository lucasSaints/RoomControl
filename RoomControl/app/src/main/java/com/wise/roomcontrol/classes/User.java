package com.wise.roomcontrol.classes;

import com.wise.roomcontrol.Dao;

import java.util.List;

public class User {
    private String mail;
    private String senha;
    private String user;
    private String empresa;
    private int id;
    static private int contadorId=1;
    final private Dao dao=new Dao();

    public User(String mail, String senha, String user) {
        this.mail = mail;
        this.senha = senha;
        this.user = user;
        this.id=contadorId;
        dao.users.add(this);
        contadorId++;
    }

    public String getMail() {
        return mail;
    }

    public String getSenha() {
        return senha;
    }
}
