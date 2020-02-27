package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {

    private List<User> usuarios=new ArrayList<>();
    private Dao dao=new Dao();
    private Context context;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void atualiza(){
        try {
            usuarios.clear();
            JSONArray k = new JSONArray(dao.ServerInOutput(false, "usuario/getByEmpresa", new String[]{"idEmp"}, new Integer[]{dao.logado.getEmpresaId()}));
            //if (k.length() > 0) {
            for (int i = 0; i < k.length(); i++) {
                JSONObject obj = k.getJSONObject(i);
                User usuario = new User(obj.getString("email"), obj.getString("senha"), obj.getString("nome"));
                usuarios.add(usuario);
            }
            //}
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public User getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return usuarios.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_opcao, parent, false);
        TextView nomeOpcao = viewCriada.findViewById(R.id.empresaNome);
        TextView description = viewCriada.findViewById(R.id.empresaLogradouro);
        nomeOpcao.setText(usuarios.get(position).getUser());
        description.setText(usuarios.get(position).getMail());
        return viewCriada;
    }
}
