package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.util.Log;
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
    //private final List<Sala> listaSalas=new ArrayList<>();
    private List<Sala> salasNaEmpresa;
    private List<String> listaDeStrings=new ArrayList<>();
    private List<Sala> listaFiltrada=new ArrayList<>();
    private final Dao dao=new Dao();

    public ListaOpcoesAdapter(Context context) {
        this.context = context;
    }

    public List<Sala> getListaFiltrada() {
        return listaFiltrada;
    }

    public ListaOpcoesAdapter() {
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

    private void refresco() {
        listaDeStrings.clear();
        salasNaEmpresa=new ArrayList<>();
        String[] a1 ={"id_organizacao"}, a2={String.valueOf(dao.logado.getEmpresaId())};
        try{
            JSONArray k = new JSONArray(dao.ServerInOutput(false, "sala/salas", a1, a2));
            if(k.length()>0){
                for (int i=0;i<k.length();i++){
                    JSONObject obj=k.getJSONObject(i);
                    if(obj.has("id")&&obj.has("nome")&&obj.has("localizacao")&&obj.has("ativo")){
                        int id=obj.getInt("id");
                        String nome = obj.getString("nome");
                        String andar=obj.getString("localizacao");
                        boolean ativo = obj.getBoolean("ativo");
                        boolean proj=obj.getBoolean("possuiMultimidia");
                        boolean ar=obj.getBoolean("possuiArcon");
                        //int pcs=obj.getInt("quantPCs");
                        if(ativo) {
                            Sala newSala = new Sala(nome,0,andar,proj,ar);
                            newSala.setId(id);
                            salasNaEmpresa.add(newSala);
                            listaDeStrings.add(newSala.getName());
                        }
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
        refresco();
        boolean auxBool=false;
        /*Empresa aux=new Empresa("Phantom","opera.com",'M',false);
        for (Empresa i:dao.empresas) {
            if(i.getId()==empresaID){
                aux=i;
            }
        }*/

        for (Sala i : salasNaEmpresa) {
            //if (i.getPcs() >= pcs) {
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
            //}
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
                try{
                for (Empresa i:listaEmpresas) {
                    listaStr.add(i.getNome());
                }}catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                break;
            case 2:
                try{
                for (Sala i:salasNaEmpresa) {
                    listaStr.add(i.getName());
                }}catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                break;
            case 3:
                try{
                for (Sala i:listaFiltrada) {
                    listaStr.add(i.getName());
                    Log.i("teste", "getAsString: "+listaStr.get(0));
                }}catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                break;
        }
        return listaStr;
    }
}
