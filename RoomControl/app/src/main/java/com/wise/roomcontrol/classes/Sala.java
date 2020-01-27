package com.wise.roomcontrol.classes;

public class Sala {
    private String name;
    private int pcs, andar;
    private boolean projetor, ac;
    private int id;
    static private int contador=1;

    public Sala(String name, int pcs, int andar, boolean projetor, boolean ac) {
        this.name = name;
        this.pcs = pcs;
        this.andar = andar;
        this.projetor = projetor;
        this.ac = ac;
        this.id=contador;
        contador++;
    }

    public String getName() {
        return this.name;
    }

    public int getPcs() {
        return this.pcs;
    }

    public int getAndar() {
        return this.andar;
    }

    public boolean temProjetor() {
        return this.projetor;
    }

    public int getId() {
        return this.id;
    }

    public boolean temAc() {
        return this.ac;
    }
}
