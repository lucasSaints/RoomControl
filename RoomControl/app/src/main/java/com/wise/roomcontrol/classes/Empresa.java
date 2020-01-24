package com.wise.roomcontrol.classes;

import com.wise.roomcontrol.Dao;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String nome, dominio;
    private int id;

    public int getId() {
        return id;
    }

    private List<Sala> salas = new ArrayList<>();
    static private int contadorId=1;
    final private Dao dao=new Dao();

    public Empresa(String nome, String dominio,boolean adicionar) {
        this.nome = nome;
        this.dominio = dominio;
        if(adicionar) {
            this.id = contadorId;
            updateEmpresa(this.nome, this.dominio);
            contadorId++;
        }
    }

    public void updateEmpresa(String nome, String dom) {
        Empresa aux=new Empresa(nome,dom,false);
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
            dao.empresas.add(this);
        }
    }

    public void addSala(Sala sala){
        salas.add(sala);
        updateEmpresa(this.nome,this.dominio);
    }

    public String getDominio() {
        return dominio;
    }

    public String getNome() {
        return nome;
    }
}
