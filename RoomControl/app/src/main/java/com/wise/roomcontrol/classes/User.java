package com.wise.roomcontrol.classes;

import com.wise.roomcontrol.Dao;

import java.util.List;

public class User {
    private String mail;
    private String senha;
    private String user;
    private int empresaId;
    private int id;
    private boolean ativo;
    static private int contadorId=1;
    final private Dao dao=new Dao();

    public int getEmpresaId() {
        return this.empresaId;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public User(String mail, String senha, String user) {
        this.mail = mail;
        this.senha = senha;
        this.user = user;
        this.id=contadorId;
        dao.users.add(this);
        contadorId++;
    }

    public User(String mail, String senha, String user, int empresaId, int id, boolean ativo) {
        this.mail = mail;
        this.senha = senha;
        this.user = user;
        this.empresaId = empresaId;
        this.id = id;
        this.ativo = ativo;
    }

    public String getMail() {
        return this.mail;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setEmpresaById(int empresa) {
        this.empresaId = empresa;
    }

    public String getUser() {
        return this.user;
    }

    public int getId() {
        return id;
    }
}
