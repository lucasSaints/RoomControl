package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.Reuniao;
import com.wise.roomcontrol.classes.Sala;

import java.util.ArrayList;
import java.util.List;

public class ListaOpcoesAdapter extends BaseAdapter {

    private Context context;
    //private final List<Sala> listaOpcoes=new ArrayList<>();
    private List<Sala> listaFiltrada=null;
    private final Dao dao=new Dao();

    public ListaOpcoesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Sala getItem(int position) {
        return listaFiltrada.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaFiltrada.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void atualiza(int empresaID,int pcs, boolean projet, boolean ac) {
        if(listaFiltrada!=null)
            listaFiltrada.clear();
        Empresa aux=new Empresa("Phantom","opera.com",'M',false);
        boolean auxBool=false;
        for (Empresa i:dao.empresas) {
            if(i.getId()==empresaID){
                aux=i;
            }
        }
            for (Sala i : aux.salas) {
                if (i.getPcs() >= pcs) {
                    if (projet == true && i.temProjetor())
                        auxBool = true;
                    if (projet == false || auxBool == true) {
                        auxBool = false;
                        if (ac == true && i.temAc())
                            auxBool = true;
                        if (ac == false || auxBool == true) {
                            listaFiltrada.add(i);
                        }
                    }
                }
            }
    }
}
