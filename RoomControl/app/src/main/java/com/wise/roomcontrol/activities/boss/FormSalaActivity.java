package com.wise.roomcontrol.activities.boss;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.adapters.ListaReunioesAdapter;
import com.wise.roomcontrol.classes.Empresa;

import org.json.JSONObject;

import java.util.List;

import static com.wise.roomcontrol.adapters.ListaOpcoesAdapter.empresaToJson;

public class FormSalaActivity extends AppCompatActivity {
    final private Dao dao=new Dao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_salas);
        final Button botone = findViewById(R.id.salvador);
        final EditText campoNome = findViewById(R.id.nomeSala);
        final EditText campoAndar = findViewById(R.id.andarSala);
        final EditText campoCapac = findViewById(R.id.capacSala);
        final EditText campoPcs = findViewById(R.id.pcsSala);
        final Switch ac = findViewById(R.id.switch2);
        final Switch proj = findViewById(R.id.switch1);
        botone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(campoNome.getText()!=null && !campoNome.getText().toString().isEmpty() && campoAndar.getText()!=null && !campoAndar.getText().toString().isEmpty()){
                    /*JSONObject obj = new JSONObject();
                    obj.put("nome",campoNome.getText().toString());
                    obj.put("localizacao",campoAndar.getText().toString());
                    if(campoCapac.getText()!=null && !campoCapac.getText().toString().isEmpty())
                        obj.put("quantidadePessoasSentadas",campoCapac.getText().toString());
                    if(campoPcs.getText()!=null && !campoPcs.getText().toString().isEmpty())
                        obj.put("quantPCs",campoPcs.getText().toString());
                    obj.put("possuiArcon",ac.isChecked());
                    obj.put("possuiMultimidia",proj.isChecked());*/
                    String capac="", pcs="";
                    if(campoCapac.getText()!=null && !campoCapac.getText().toString().isEmpty())
                        capac = campoCapac.getText().toString();
                    if(campoPcs.getText()!=null && !campoPcs.getText().toString().isEmpty())
                        pcs=campoPcs.getText().toString();
                    try {
                        ListaOpcoesAdapter adapter=new ListaOpcoesAdapter();
                        adapter.atualiza(new String[]{dao.logado.getMail().substring(dao.logado.getMail().indexOf("@")+1)});
                        List<Empresa> lista = adapter.getListaEmpresas();
                        Empresa org=new Empresa();
                        for (Empresa i:lista) {
                            if(i.getId()==dao.logado.getEmpresaId())
                                org=i;
                        }
                        int q1, q2;
                        try{
                            q1=Integer.parseInt(campoCapac.getText().toString());
                        }catch (Exception e){
                            q1=0;
                        }
                        try{
                            q2=Integer.parseInt(campoPcs.getText().toString());
                        }catch (Exception e){
                            q2=0;
                        }
                        dao.ServerInOutput(true,"sala/salvar",new String[]{"nome", "localizacao", "quantidadePessoasSentadas", "quantPCs", "possuiArcon", "possuiMultimidia", "idOrganizacao", "jsonSala"}
                                                            , new Object[]{campoNome.getText().toString(), campoAndar.getText().toString(), q1, q2, (Boolean) ac.isChecked(), (Boolean) proj.isChecked(), empresaToJson(org)});
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(FormSalaActivity.this, getString(R.string.server_conection_problem_try_again_later), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FormSalaActivity.this, getString(R.string.preencha_corretamente), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}