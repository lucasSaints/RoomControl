package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.activities.MainActivity;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.Reuniao;
import com.wise.roomcontrol.classes.Sala;
import com.wise.roomcontrol.classes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaReunioesAdapter extends BaseAdapter {

    private Context context;
    static public final List<Reuniao> reunioes = new ArrayList<>();
    private Dao dao=new Dao();
    private boolean soPlayer=false;
    public String[] weekDays;

    public ListaReunioesAdapter(Context context, List<Reuniao> reuniaos) {
        this.context = context;
        this.atualiza();
    }

    public ListaReunioesAdapter(Context context) {
        this.context = context;
        weekDays = new String[]{context.getString(R.string.sundayShort), context.getString(R.string.mondayShort), context.getString(R.string.tuesdayShort), context.getString(R.string.wednesdayShort), context.getString(R.string.thursdayShort), context.getString(R.string.fridayShort), context.getString(R.string.saturdayShort)};
        try {
            soPlayer = MainActivity.onlyLogado;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSoPlayer(Boolean soPlayer) {
        if(soPlayer==null)
            this.soPlayer=MainActivity.onlyLogado;
        else
            this.soPlayer = soPlayer;
    }

    @Override
    public int getCount() {
        return reunioes.size();
    }

    @Override
    public Reuniao getItem(int position) {
        return reunioes.get(position);
    }

    @Override
    public long getItemId(int position) {
        for (int i=0;i<reunioes.size();i++) {
            Log.i("teste", "getItemId: "+reunioes.get(i).getDescricao()+" - "+reunioes.get(i).getId());
        }
        Log.i("teste", "getItemId: o array tem tamanho "+reunioes.size());
        Log.i("teste", "getItemId: o objeto Reuniao na posicao "+position);
        Log.i("teste", "getItemId: tem id "+reunioes.get(position).getId());
        return reunioes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ordena();
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_reuniaolista, parent, false);
        Reuniao reuniao = reunioes.get(position);
        TextView sala = viewCriada.findViewById(R.id.Sala); // Insere texto de nome da sala
        sala.setText(reuniao.getLugar().getName());         //    ''      ''       ''
        TextView floor = viewCriada.findViewById(R.id.andar);  // Insere texto de andar
        floor.setText(context.getString(R.string.floor)+": "+reuniao.getLugar().getAndar()); //  ''     ''     ''
        User u = null;
        try {
            JSONObject j = new JSONObject(dao.ServerInOutput(false,"usuario/getById",new String[]{"id"},new Integer[]{reuniao.getLocadorId()}));
            TextView locator = viewCriada.findViewById(R.id.locador);//Insere texto do username de quem alocou a sala
            locator.setText("~"+j.getString("nome"));//  ''     ''     ''     ''     ''     ''
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView descri = viewCriada.findViewById(R.id.descrp);//Insere texto de descrição da reunião
        descri.setText(reuniao.getDescricao());                //   ''      ''      ''       ''
        TextView dia=viewCriada.findViewById(R.id.data);                //¬
        boolean auxBool=false;                                           //
        for (int i = 0; i < reuniao.getRepeticoes().length; i++) {       //
            Log.i("auxbool", reuniao.getDescricao()+" tem repeticao no dia "+weekDays[i]+": "+reuniao.getRepeticoes()[i]);
            if(reuniao.getRepeticoes()[i])                               //
                auxBool=true;                                            //
        }                                                                //
        Log.i("auxbool", "getView: "+auxBool);                           //
        if(auxBool){                                                     //
            dia.setText(reuniao.getRepeticoesAsWeekDay(weekDays));               //
        }else {                                                          //  Formata e insere texto de data
            String auxString = "";                                       //
            if (reuniao.getData(0) < 10)                                 //
                auxString = "0";                                         //
            auxString += reuniao.getData(0) + "/";                       //
            if (reuniao.getData(1) < 10)                                 //
                auxString += "0";                                        //
            auxString += reuniao.getData(1) + "/" + reuniao.getData(2);  //
            dia.setText(auxString);                                      //
        }                                                               //¬
        TextView horario=viewCriada.findViewById(R.id.horario);                                           //  Formata e insere texto de horário
        horario.setText(reuniao.getHorario(reuniao.getHora1())+" - "+reuniao.getHorario(reuniao.getHora2())); //   ''      ''       ''      ''
        return viewCriada;
    }

    private void ordena() {
        /*List<Reuniao> aux = new ArrayList<>();
        aux.addAll(reunioes);
        reunioes.clear();
        for (int i = 0; i < aux.size(); i++) {
            Reuniao proxima=new Reuniao("","",new Sala("",0,"",false,false),new int[]{4000,12,31},new int[]{22,00},new int[]{23,00});
            for (int j = 0; j < aux.size(); j++) {
                Log.i("", "<><><><><><><><><>   ordena: "+aux.get(j).getDataHora(aux.get(j).getHora1())+" comparado a "+proxima.getDataHora(proxima.getHora1()));
                /*if(aux.get(j).getDataHora(aux.get(j).getHora1()).compareTo(proxima.getDataHora(proxima.getHora1())) < 0){
                    proxima=aux.get(j);
                }*/
                /*if(aux.get(j).getData(2)==proxima.getData(2)){
                    if(aux.get(j).getData(1)==proxima.getData(1)){
                        if(aux.get(j).getData(0)==proxima.getData(0)){
                            if(aux.get(j).getHora1()[0]==proxima.getHora1()[0]){
                                if(aux.get(j).getHora1()[1]==proxima.getHora1()[1]){
                                    if(aux.get(j).getLugar().getId()==proxima.getLugar().getId()){
                                        System.out.println("RoomControl.Exception: BAKA NA! Isso deveria ser impossível!");
                                        if(new Random().nextInt(2)==1)
                                            proxima=aux.get(j);
                                    }else if(aux.get(j).getLugar().getId()<proxima.getLugar().getId())
                                        proxima=aux.get(j);
                                }else if(aux.get(j).getHora1()[1]<proxima.getHora1()[1])
                                    proxima=aux.get(j);
                            }else if(aux.get(j).getHora1()[0]<proxima.getHora1()[0])
                                proxima=aux.get(j);
                        }else if(aux.get(j).getData(0)<proxima.getData(0))
                            proxima=aux.get(j);
                    }else if(aux.get(j).getData(1)<proxima.getData(1))
                        proxima=aux.get(j);
                }else if(aux.get(j).getData(2)<proxima.getData(2))
                    proxima=aux.get(j);
            }
            reunioes.add(proxima);
            aux.remove(proxima);
        }*/
    }

    public void atualiza(){
        reunioes.clear();
        String[] a1 ={"id_sala"};
        String[] u1 ={"id"};
        if(!soPlayer) {
            ListaOpcoesAdapter opcoes = new ListaOpcoesAdapter();
            opcoes.filtraSalas(dao.logado.getEmpresaId(), 0, false, false);
            try {
                int auxiliar=0;
                for (Sala sala : opcoes.getListaFiltrada()) {
                    boolean aux;
                    try {
                        aux = MainActivity.salasNoFiltro.get(auxiliar);
                    }catch (Exception e){
                        aux=true;
                    }
                    if(aux) {
                        Object[] a2 = {(Integer) sala.getId()};
                        JSONArray k = new JSONArray(dao.ServerInOutput(false, "reserva/byIdSala", a1, a2));
                        if (k.length() > 0) {
                            for (int i = 0; i < k.length(); i++) {
                                JSONObject obj = k.getJSONObject(i);
                                jsonArrayToObject(sala, obj);
                            }
                        }
                    }
                    auxiliar++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try{
                JSONArray k = new JSONArray(dao.ServerInOutput(false, "reserva/byIdUsuario",new String[]{"id_usuario"},new Integer[]{dao.logado.getId()}));
                if(k.length()>0){
                    for (int i = 0; i < k.length(); i++) {
                        JSONObject obj = k.getJSONObject(i);
                        JSONObject objec = new JSONObject(dao.ServerInOutput(false,"sala/getById",new String[]{"id"},new Integer[]{obj.getInt("idSala")}));
                        System.out.println(objec);
                        Sala salinha = new Sala(objec.getString("nome"),objec.getInt("quantPCs"),objec.getInt("quantidadePessoasSentadas"),objec.getString("localizacao"),objec.getBoolean("possuiMultimidia"),objec.getBoolean("possuiArcon"));
                        jsonArrayToObject(salinha,obj);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    public void jsonArrayToObject(Sala sala, JSONObject obj) throws JSONException {
        if (obj.has("id") && obj.has("descricao") && obj.has("ativo") && obj.has("idUsuario")) {
            int id = obj.getInt("id");
            String descri = obj.getString("descricao");
            boolean ativo = obj.getBoolean("ativo");
            System.out.println("\n\n\n\n\n" + obj.getInt("idUsuario") + "\n\n\n\n\n");
            int idUser = obj.getInt("idUsuario");
            String datahora = obj.getString("dataHoraInicio");
            String[] data = {"", "", ""}, hora1 = {"", ""}, hora2 = {"", ""};
            for (int j = 0; j < datahora.indexOf("-"); j++) {
                data[2] += datahora.charAt(j);
            }
            System.out.println("ano: " + data[2]);
            for (int j = datahora.indexOf("-") + 1; j < datahora.indexOf("-", data[2].length() + 1); j++) {
                data[1] += datahora.charAt(j);
            }
            System.out.println("mes: " + data[1]);
            for (int j = datahora.indexOf("-", data[2].length() + 1) + 1; j < datahora.indexOf("T"); j++) {
                data[0] += datahora.charAt(j);
            }
            System.out.println("dia: " + data[0]);
            for (int j = datahora.indexOf("T") + 1; j < datahora.indexOf(":"); j++) {
                hora1[0] += datahora.charAt(j);
            }
            for (int j = datahora.indexOf(":") + 1; j < datahora.indexOf("Z") - 3; j++) {
                hora1[1] += datahora.charAt(j);
            }
            System.out.println("hora de inicio: " + hora1[0] + ":" + hora1[1]);
            datahora = obj.getString("dataHoraFim");
            for (int j = datahora.indexOf("T") + 1; j < datahora.indexOf(":"); j++) {
                hora2[0] += datahora.charAt(j);
            }
            for (int j = datahora.indexOf(":") + 1; j < datahora.indexOf("Z") - 3; j++) {
                hora2[1] += datahora.charAt(j);
            }
            System.out.println("hora de termino: " + hora2[0] + ":" + hora2[1]);
            System.out.println(datahora);
            int[] hour1 = {Integer.parseInt(hora1[0]), Integer.parseInt(hora1[1])}, hour2 = {Integer.parseInt(hora2[0]), Integer.parseInt(hora2[1])}, date = {Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])};
        /*if(datahora.contains("UTC")){
            hour1[0]-=3;
            hour2[0]-=3;
        }*/
            //JSONObject user = new JSONObject(dao.ServerInOutput(false, "usuario/getById", u1, u2));
            if (ativo) {
                Reuniao newReuniao = new Reuniao(descri, idUser, sala, date, hour1, hour2);
                newReuniao.setId(id);
                newReuniao.setRepeticoes(obj.getString("repeticoes"));
                reunioes.add(newReuniao);
            }
        }
    }
}
