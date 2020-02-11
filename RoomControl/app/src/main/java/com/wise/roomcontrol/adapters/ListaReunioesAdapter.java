package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.Reuniao;
import com.wise.roomcontrol.classes.Sala;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaReunioesAdapter extends BaseAdapter {

    private Context context;
    static public final List<Reuniao> reunioes = new ArrayList<>();
    private Dao dao=new Dao();

    public ListaReunioesAdapter(Context context, List<Reuniao> reuniaos) {
        this.context = context;
        this.atualiza();
    }

    public ListaReunioesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return reunioes.size();
    }

    @Override
    public Object getItem(int position) {
        return reunioes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reunioes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.reuniaolista_item, parent, false);
        Reuniao reuniao = reunioes.get(position);
        TextView sala = viewCriada.findViewById(R.id.Sala); // Insere texto de nome da sala
        sala.setText(reuniao.getLugar().getName());         //    ''      ''       ''
        TextView floor = viewCriada.findViewById(R.id.andar);  // Insere texto de andar
        floor.setText("Andar: "+reuniao.getLugar().getAndar()); //  ''     ''     ''
        TextView locator = viewCriada.findViewById(R.id.locador);//Insere texto do username de quem alocou a sala
        locator.setText("~"+reuniao.getLocador());                   //  ''     ''     ''     ''     ''     ''
        TextView descri = viewCriada.findViewById(R.id.descrp);//Insere texto de descrição da reunião
        descri.setText(reuniao.getDescricao());                //   ''      ''      ''       ''
        TextView dia=viewCriada.findViewById(R.id.data);      //¬
        String auxString="";                                   //
        if(reuniao.getData(0)<10)                              //
            auxString="0";                                     //
        auxString+=reuniao.getData(0)+"/";                     //  Formata e insere texto de data
        if(reuniao.getData(1)<10)                              //
            auxString+="0";                                    //
        auxString+=reuniao.getData(1)+"/"+reuniao.getData(2);  //
        dia.setText(auxString);                               //¬
        TextView horario=viewCriada.findViewById(R.id.horario);                                           //  Formata e insere texto de horário
        horario.setText(reuniao.getHorario(reuniao.getHora1())+" - "+reuniao.getHorario(reuniao.getHora2())); //   ''      ''       ''      ''
        return viewCriada;
    }


    public void atualiza(){
        reunioes.clear();
        String[] a1 ={"id_sala"};
        String[] u1 ={"id"};
        ListaOpcoesAdapter opcoes=new ListaOpcoesAdapter();
        opcoes.filtraSalas(dao.logado.getEmpresaId(),0,false,false);
        try{
            for (Sala sala:opcoes.getListaFiltrada()) {
                Object[] a2 = {(Integer) sala.getId()};
                JSONArray k = new JSONArray(dao.ServerInOutput(false, "reserva/byIdSala", a1, a2));
                if(k.length()>0){
                    for (int i=0;i<k.length();i++){
                        JSONObject obj=k.getJSONObject(i);
                        if(obj.has("id")&&obj.has("descricao")&&obj.has("ativo")&&obj.has("idUsuario")){
                            int id=obj.getInt("id");
                            String descri = obj.getString("descricao");
                            boolean ativo = obj.getBoolean("ativo");
                            System.out.println("\n\n\n\n\n"+obj.getInt("idUsuario")+"\n\n\n\n\n");
                            Object[] u2 = {(Integer) obj.getInt("idUsuario")};
                            String datahora = obj.getString("dataHoraInicio");
                            String[] data={"","",""}, hora1={"",""}, hora2={"",""};
                            for (int j = 0; j < datahora.indexOf("-"); j++) {
                                data[2]+=datahora.charAt(j);
                            }
                            System.out.println("ano: "+data[2]);
                            for (int j = datahora.indexOf("-")+1; j < datahora.indexOf("-",data[2].length()+1); j++) {
                                data[1]+=datahora.charAt(j);
                            }
                            System.out.println("mes: "+data[1]);
                            for (int j = datahora.indexOf("-",data[2].length()+1)+1; j < datahora.indexOf("T"); j++) {
                                data[0]+=datahora.charAt(j);
                            }
                            System.out.println("dia: "+data[0]);
                            for (int j = datahora.indexOf("T")+1; j < datahora.indexOf(":"); j++) {
                                hora1[0]+=datahora.charAt(j);
                            }
                            for (int j = datahora.indexOf(":")+1; j < datahora.indexOf("Z")-3; j++) {
                                hora1[1]+=datahora.charAt(j);
                            }
                            System.out.println("hora de inicio: "+hora1[0]+":"+hora1[1]);
                            datahora = obj.getString("dataHoraFim");
                            for (int j = datahora.indexOf("T")+1; j < datahora.indexOf(":"); j++) {
                                hora2[0]+=datahora.charAt(j);
                            }
                            for (int j = datahora.indexOf(":")+1; j < datahora.indexOf("Z")-3; j++) {
                                hora2[1]+=datahora.charAt(j);
                            }
                            System.out.println("hora de termino: "+hora2[0]+":"+hora2[1]);
                            System.out.println(datahora);
                            int[] hour1={Integer.parseInt(hora1[0]),Integer.parseInt(hora1[1])}, hour2={Integer.parseInt(hora2[0]),Integer.parseInt(hora2[1])}, date={Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2])};
                            /*if(datahora.contains("UTC")){
                                hour1[0]-=3;
                                hour2[0]-=3;
                            }*/
                            JSONObject user = new JSONObject(dao.ServerInOutput(false, "usuario/getById", u1, u2));
                            if(ativo) {
                                Reuniao newReuniao = new Reuniao(descri,user.getString("nome"),sala,date,hour1,hour2);
                                newReuniao.setId(id);
                                reunioes.add(newReuniao);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }

}
