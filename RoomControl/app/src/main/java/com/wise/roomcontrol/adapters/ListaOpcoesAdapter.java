package com.wise.roomcontrol.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.Sala;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaOpcoesAdapter extends BaseAdapter {

    private Context context;
    private int tipo;
    public final static int EMPRESAS=1, SALAS=2,FILTRO=3;
    static private final List<Empresa> listaEmpresas = new ArrayList();
    //private final List<Sala> listaSalas=new ArrayList<>();
    public List<Sala> salasNaEmpresa;
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
        return listaFiltrada.size();
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
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_opcao, parent, false);
        TextView nomeOpcao = viewCriada.findViewById(R.id.empresaNome);
        TextView description = viewCriada.findViewById(R.id.empresaLogradouro);
        nomeOpcao.setText(listaFiltrada.get(position).getName());
        description.setText(listaFiltrada.get(position).getAndar());
        return viewCriada;
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

    public void refresco() {
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
                        int pcs=obj.getInt("quantPCs");
                        int qntPessoas = obj.getInt("quantidadePessoasSentadas");
                        int incId=obj.getJSONObject("idOrganizacao").getInt("id");
                        String dataCria = obj.getString("dataCriacao").replaceAll("-","/");         //.replaceAll(":00Z"," ")
                        dataCria = dataFormatter(dataCria);
                        if(ativo) {
                            Sala newSala = new Sala(nome,pcs,qntPessoas,andar,proj,ar);
                            newSala.setId(id);
                            newSala.setEmpresaId(incId);
                            newSala.setDataCriacao(dataCria);
                            try {
                                if (obj.get("dataAlteracao") != null && !obj.getString("dataAlteracao").isEmpty()) {
                                    String dataAlt = obj.getString("dataAlteracao").replaceAll("-", "/");
                                    dataAlt = dataFormatter(dataAlt);
                                    newSala.setDataAlteracao(dataAlt);
                                } else
                                    newSala.setDataAlteracao(dataCria);
                            }catch (Exception e){
                                newSala.setDataAlteracao(dataCria);
                            }
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

    public String dataFormatter(String data) {
        String[] dataAux = data.split("T");
        data=dataAux[0]+" "+dataAux[1]+"T"+dataAux[2];
        dataAux[0]=data.substring(0, data.indexOf(":",data.indexOf(":")+1) );
        dataAux[1]=data.substring(data.indexOf("["),data.indexOf("]")+1);
        data=dataAux[0]+" "+dataAux[1];
        return data;
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
            case EMPRESAS:
                try{
                for (Empresa i:listaEmpresas) {
                    listaStr.add(i.getNome());
                }}catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                break;
            case SALAS:
                try{
                for (Sala i:salasNaEmpresa) {
                    listaStr.add(i.getName());
                }}catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
                break;
            case FILTRO:
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

    public static JSONObject empresaToJson(Empresa emp){
        JSONObject obj=new JSONObject();
        try {
            obj.put("nome", emp.getNome());
            obj.put("dominio", emp.getDominio());
            obj.put("endereco", emp.getEndereco());
            obj.put("id", emp.getId());
            obj.put("ativo", true);
            obj.put("tipoOrganizacao", Character.toString(emp.getTipo()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return obj;
    }
}
