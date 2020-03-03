package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.graphics.ColorUtils;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserAdapter extends BaseAdapter {

    private List<User> usuarios=new ArrayList<>();
    private Dao dao=new Dao();
    private Context context;
    private SharedPreferences prefs;

    public UserAdapter(Context context) {
        this.context = context;
        prefs=context.getApplicationContext().getSharedPreferences("Descartes",MODE_PRIVATE);
    }

    public void atualiza(){
        try {
            usuarios.clear();
            JSONArray k = new JSONArray(dao.ServerInOutput(false, "usuario/getByEmpresa", new String[]{"idEmp"}, new Integer[]{dao.logado.getEmpresaId()}));
            //if (k.length() > 0) {
            for (int i = 0; i < k.length(); i++) {
                JSONObject obj = k.getJSONObject(i);
                User usuario = new User(obj.getString("email"), obj.getString("senha"), obj.getString("nome"),obj.getJSONObject("idOrganizacao").getInt("id"),obj.getInt("id"),obj.getBoolean("ativo"));
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
        try{
            viewCriada.findViewById(R.id.imageViewLar).setBackgroundColor(Color.parseColor(prefs.getString("color", null)));
            if(ColorUtils.calculateLuminance(((ColorDrawable) viewCriada.findViewById(R.id.imageViewLar).getBackground()).getColor())>0.6)
                nomeOpcao.setTextColor(Color.parseColor("#0E0017"));
            else
                nomeOpcao.setTextColor(Color.parseColor("#efefef"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!usuarios.get(position).isAtivo())
            viewCriada.findViewById(R.id.imageViewLar).setBackgroundColor(Color.parseColor("#aaaaaa"));
        return viewCriada;
    }
}
