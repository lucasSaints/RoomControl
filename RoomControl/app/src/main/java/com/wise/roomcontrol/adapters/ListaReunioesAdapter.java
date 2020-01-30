package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.Reuniao;
import java.util.ArrayList;
import java.util.List;

public class ListaReunioesAdapter extends BaseAdapter {

    private Context context;
    static public final List<Reuniao> reunioes = new ArrayList<>();

    public ListaReunioesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.reuniaolista_item, parent, false);
        Reuniao reuniao = reunioes.get(position);
        TextView sala = viewCriada.findViewById(R.id.Sala); // Insere texto de nome da sala
        sala.setText(reuniao.getLugar().getName());         //    ''      ''       ''
        TextView floor = viewCriada.findViewById(R.id.andar);  // Insere texto de andar
        floor.setText("Andar "+reuniao.getLugar().getAndar()); //  ''     ''     ''
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

    public void atualiza(List<Reuniao> reuniaos){
        reunioes.clear();
        this.reunioes.addAll(reuniaos);
        notifyDataSetChanged();
    }
}
