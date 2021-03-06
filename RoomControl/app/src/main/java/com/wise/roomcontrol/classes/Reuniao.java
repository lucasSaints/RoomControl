package com.wise.roomcontrol.classes;

import android.util.Log;

import com.wise.roomcontrol.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reuniao implements Comparable<Reuniao> {
    private String descricao;
    private Sala lugar;
    private int[] data;
    private int[] hora1, hora2;
    private Boolean[] repeticoes;
    private int id, locadorId;

    public Reuniao(String descricao, int locador, Sala lugar, int[] data, int[] hora1, int[] hora2) {
        this.descricao = descricao;
        this.locadorId = locador;
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

    public int getLocadorId() {
        return locadorId;
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

    public Boolean[] getRepeticoes() {
        return repeticoes;
    }

    public static String getRepeticoesAsStringAlt(Boolean[] repeticos) {
        String repe="";
        for (Boolean dia:repeticos) {   // 0,0,0,0,0,0,0
            if(dia)
                repe=repe.concat("1");
            else
                repe=repe.concat("0");
            if(repe.length()<12)
                repe=repe.concat(",");
        }
        return repe;
    }

    public String getRepeticoesAsWeekDay(String[] weekDays) {
        String repe="";
        for(int i=0;i<7;i++){
            if(this.repeticoes[i]) {
                if(repe.length()>1)
                    repe=repe.concat(", ");
                repe=repe.concat(weekDays[i]);
            }
        }
        if(repe.length()<12){
            String aux="";
            for (int i = 0; i < 12-repe.length(); i++) {
                aux+=" ";
            }
            repe=aux+repe;
        }
        return repe;
    }

    public void setRepeticoes(String repeticoes) {
        Boolean[] repetics = new Boolean[7];
        String[] repeticoes2=repeticoes.split(",");
        for (int i=0;i<repeticoes2.length;i++) {
            repetics[i] = (repeticoes2[i].equals("1"));
        }
        this.repeticoes = repetics;
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

    public Date getDataHora(int[] hor){
        return new Date(this.data[2],this.data[1],this.data[0],hor[0],hor[1]);
    }

    public boolean dataInvalida(){
        for (int i = 0; i < data.length; i++) {
            if((Integer) data[i]==null)
                return true;
        }
        for (int i = 0; i < hora1.length; i++) {
            if((Integer) hora1[i]==null)
                return true;
        }
        for (int i = 0; i < hora2.length; i++) {
            if((Integer) hora2[i]==null)
                return true;
        }
        return false;
    }

    @Override
    public int compareTo(Reuniao o) {
        Log.i("Reuniao", descricao+" compareTo----------------------------");
        if(Reuniao.getRepeticoesAsStringAlt(this.repeticoes).isEmpty() || Reuniao.getRepeticoesAsStringAlt(this.repeticoes).equals("0,0,0,0,0,0,0")) {
            if (this.dataInvalida() || o.dataInvalida()) {
                Log.i("Reuniao", "data invalida");
                return 0;
            }
            Log.i("Reuniao", "compareTo "+o.getDescricao()+": "+this.getDataHora(this.getHora1()).compareTo(o.getDataHora(o.getHora1())));
            return this.getDataHora(this.getHora1()).compareTo(o.getDataHora(o.getHora1()));
        }else {
            Log.i("Reuniao", "tem repeteco");
            return 2;
        }
    }
}
