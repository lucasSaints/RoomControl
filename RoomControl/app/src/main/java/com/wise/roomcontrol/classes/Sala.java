package com.wise.roomcontrol.classes;

public class Sala {
    private String name, andar, dataCriacao, dataAlteracao;
    private int pcs, quantPessoas;
    private boolean projetor, ac;
    private int id;
    //static private int contador=1;

    public Sala(String name, int pcs, int quantPessoas, String andar, boolean projetor, boolean ac) {
        this.name = name;
        this.pcs = pcs;
        this.andar = andar;
        this.projetor = projetor;
        this.ac = ac;
        this.quantPessoas=quantPessoas;
        //this.id=contador;
        //contador++;
    }

    public String getName() {
        return this.name;
    }

    public int getPcs() {
        return this.pcs;
    }

    public int getQuantPessoas() {
        return quantPessoas;
    }

    public String getAndar() {
        return this.andar;
    }

    public boolean temProjetor() {
        return this.projetor;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean temAc() {
        return this.ac;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public String getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataAlteracao(String dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
