package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.Empresa;

import java.util.ArrayList;
import java.util.List;

public class AuxAdapter extends BaseAdapter {

    private List<Empresa> listaEmpresas=new ArrayList<>();
    private Context context;

    public AuxAdapter(List<Empresa> listaEmpresas, Context context) {
        this.listaEmpresas = listaEmpresas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.listaEmpresas.size();
    }

    @Override
    public Empresa getItem(int position) {
        return this.listaEmpresas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.listaEmpresas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_opcao, parent, false);
        TextView nomeOpcao = viewCriada.findViewById(R.id.empresaNome);
        TextView description = viewCriada.findViewById(R.id.empresaLogradouro);
        nomeOpcao.setText(listaEmpresas.get(position).getNome());
        description.setText(listaEmpresas.get(position).getEndereco());
        return viewCriada;
    }
}
