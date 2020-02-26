package com.wise.roomcontrol.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.classes.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private List<User> usuarios;
    private Dao dao=new Dao();

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
        return null;
    }
}
