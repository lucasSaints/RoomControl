package com.wise.roomcontrol.classes;

import com.wise.roomcontrol.Dao;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String nome, dominio, endereco;
    private int id, ativo;
    private char tipo;

    public int getId() {
        return id;
    }

    public List<Sala> salas = new ArrayList<>();
    static private int contadorId=1;
    final private Dao dao=new Dao();

    public Empresa(String nome, String dominio, char tipo,boolean adicionar) {
        this.nome = nome;
        this.dominio = dominio;
        if(adicionar) {
            this.id = contadorId;
            updateEmpresa(this.nome, this.dominio, this.tipo);
            contadorId++;
        }
    }

    public Empresa(String nome, String dominio, char tipo, int id) {
        this.nome = nome;
        this.dominio = dominio;
        this.id = id;
    }

    public void updateEmpresa(String nome, String dom, char tip) {
        Empresa aux=new Empresa(nome,dom,tip,false);
        aux.nome = nome;
        aux.dominio = dom;
        aux.salas=this.salas;
        if(dao.empresas!=null){
            for (Empresa i:dao.empresas) {
                if(i.id==this.id) {
                    aux.id=this.id;
                    dao.empresas.remove(this);
                    dao.empresas.add(aux);
                }
            }
        }else{
            dao.empresas.add(aux);
        }
    }

    public Empresa() {
    }

    public void addSala(Sala sala){
        salas.add(sala);
        updateEmpresa(this.nome,this.dominio,this.tipo);
    }

    public String getDominio() {
        return dominio;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
