package com.wise.roomcontrol.classes;

public class Sala {
    private String name;
    private int pcs, andar;
    private boolean projetor, ac;

    public Sala(String name, int pcs, int andar, boolean projetor, boolean ac) {
        this.name = name;
        this.pcs = pcs;
        this.andar = andar;
        this.projetor = projetor;
        this.ac = ac;
    }

    public String getName() {
        return name;
    }

    public int getPcs() {
        return pcs;
    }

    public int getAndar() {
        return andar;
    }

    public boolean temProjetor() {
        return projetor;
    }

    public boolean temAc() {
        return ac;
    }
}
