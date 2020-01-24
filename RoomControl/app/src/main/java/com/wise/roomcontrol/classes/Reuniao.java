package com.wise.roomcontrol.classes;

public class Reuniao {
    private String descricao,locador;
    private Sala lugar;
    private int[] data;
    private int[] hora1, hora2;
    private int computadores;
    private boolean ac, projetor;
    private int empresaId;

    public Reuniao(String descricao, String locador, Sala lugar, int[] data, int[] hora1, int[] hora2, int computadores, boolean ac, boolean projetor) {
        this.descricao = descricao;
        this.locador = locador;
        this.lugar = lugar;
        this.data = data;
        this.hora1 = hora1;
        this.hora2 = hora2;
        this.computadores = computadores;
        this.ac = ac;
        this.projetor = projetor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocador() {
        return locador;
    }

    public Sala getLugar() {
        return lugar;
    }

    public int getData(int index) {
        return data[index];
    }

    public int[] getHora1() {
        return hora1;
    }

    public int[] getHora2() {
        return hora2;
    }

    public String getHorario(int[] hora){
        return hora[0]+":"+hora[1];
    }

    public int getComputadores() {
        return computadores;
    }

    public boolean isAc() {
        return ac;
    }

    public boolean isProjetor() {
        return projetor;
    }

    public int getEmpresaId() {
        return empresaId;
    }
}
