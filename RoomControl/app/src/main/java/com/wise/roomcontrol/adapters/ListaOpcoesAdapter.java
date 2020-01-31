package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.RegisterActivity;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.Reuniao;
import com.wise.roomcontrol.classes.Sala;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaOpcoesAdapter extends BaseAdapter {

    private Context context;
    static private final List<Empresa> listaEmpresas = new ArrayList();
    private final List<Sala> listaSalas=new ArrayList<>();
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

    public void atualiza(String[] dominioAsArray) {
        if(listaEmpresas!=null)
            listaEmpresas.clear();
        String[] a1 ={"dominio"};
        try{
            JSONArray k = new JSONArray(dao.ServerInOutput(false, "organizacao/organizacoesByDominio", a1, dominioAsArray));
            List<String> listaDeStrings = new ArrayList<>();
            if(k.length()>0){
                for (int i=0;i<k.length();i++){
                    JSONObject obj=k.getJSONObject(i);
                    if(obj.has("id")&&obj.has("nome")&&obj.has("tipoOrganizacao")){
                        int id=obj.getInt("id");
                        String nome = obj.getString("nome");
                        String tipoOrganizacao = obj.getString("tipoOrganizacao");
                        Empresa newEmp = new Empresa(nome,dominioAsArray[0],tipoOrganizacao.charAt(0),id);
                        listaEmpresas.add(newEmp);
                        listaDeStrings.add(newEmp.getNome());
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void filtraSalas(int empresaID, int pcs, boolean projet, boolean ac) {
        /*if(pcs==null)
            pcs=0;*/
        if(listaFiltrada!=null)
            listaFiltrada.clear();
        Empresa aux=new Empresa("Phantom","opera.com",'M',false);
        boolean auxBool=false;
        for (Empresa i:dao.empresas) {
            if(i.getId()==empresaID){
                aux=i;
            }
        }

        //

        //

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
        notifyDataSetChanged();
    }

    public static List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public List<String> getAsString(int indexOfListInClass){
        List<String> listaStr = new ArrayList<>();
        switch (indexOfListInClass){
            case 1:
                for (Empresa i:listaEmpresas) {
                    listaStr.add(i.getNome());
                }
                break;
            case 2:
                for (Sala i:listaSalas) {
                    listaStr.add(i.getName());
                }
                break;
            case 3:
                for (Sala i:listaFiltrada) {
                    listaStr.add(i.getName());
                }
                break;
        }
        return listaStr;
    }
}
