package com.wise.roomcontrol.classes;

public class Reuniao {
    private String descricao,locador;
    private Sala lugar;
    private int[] data;
    private int[] hora1, hora2;
    //private int computadores;
    //private boolean ac, projetor;
    private int id;

    public Reuniao(String descricao, String locador, Sala lugar, int[] data, int[] hora1, int[] hora2) {
        this.descricao = descricao;
        this.locador = locador;
        this.lugar = lugar;
        this.data = data;
        this.hora1 = hora1;
        this.hora2 = hora2;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
        String horar="";
        if(hora[0]<10)
            horar+="0";
        horar+=hora[0]+":";
        if(hora[1]<10)
            horar+="0";
        horar+=hora[1];
        return horar;
    }
}
