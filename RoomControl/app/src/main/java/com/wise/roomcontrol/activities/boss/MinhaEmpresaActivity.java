package com.wise.roomcontrol.activities.boss;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.adapters.ListaReunioesAdapter;
import com.wise.roomcontrol.adapters.UserAdapter;

import java.sql.Savepoint;

public class MinhaEmpresaActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myempresa);
        prefs=getApplicationContext().getSharedPreferences("Descartes",MODE_PRIVATE);
        editor=prefs.edit();
        Dao dao = new Dao();
        ListaOpcoesAdapter adapter1 = new ListaOpcoesAdapter();
        ListaReunioesAdapter adapter2 = new ListaReunioesAdapter(MinhaEmpresaActivity.this);
        UserAdapter adapter3 = new UserAdapter(MinhaEmpresaActivity.this);
        TextView tit = findViewById(R.id.tit);
        tit.setText(dao.empLogada.getNome());
        TextView rua = findViewById(R.id.rua);
        rua.setText(dao.empLogada.getEndereco());
        TextView qSalas = findViewById(R.id.quantSalas);
        adapter1.refresco();
        qSalas.setText(adapter1.salasNaEmpresa.size()+" "+getString(R.string.salas));
        TextView qReuni = findViewById(R.id.quantReunioes);
        adapter2.atualiza();
        if(adapter2.getCount()==1)
            qReuni.setText(adapter2.getCount()+" "+getString(R.string.reuniaomarcada_singular));
        else
            qReuni.setText(adapter2.getCount()+" "+getString(R.string.reuniaomarcada_plural));
        TextView qMembs = findViewById(R.id.quantMembros);
        adapter3.atualiza();
        qMembs.setText(adapter3.getCount()+" "+getString(R.string.members));
        Button altcor = findViewById(R.id.altCor);
        try{
            tit.setBackgroundColor(Color.parseColor(prefs.getString("color", null)));
            altcor.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor(prefs.getString("color", null)),Color.parseColor("#424242"),0.72f));
        }catch (Exception e){
            e.printStackTrace();
        }
        TextView titulo = findViewById(R.id.tit);
        if(ColorUtils.calculateLuminance(((ColorDrawable) titulo.getBackground()).getColor())>0.6)
            titulo.setTextColor(Color.parseColor("#0E0017"));
        else
            titulo.setTextColor(Color.parseColor("#efefef"));
        altcor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editext = new EditText(MinhaEmpresaActivity.this);
                editext.setText("#FFFFFF");
                AlertDialog.Builder builder = new AlertDialog.Builder(MinhaEmpresaActivity.this);
                builder.setTitle("Seleção de cor").setMessage("Selecione a cor da empresa. Visível apenas no seu dispositivo.").setView(editext).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         /*boolean pode=true;
                       for(int i=0;i<editext.getText().toString().length();i++){
                            if(editext.getText().toString().charAt(i)!='#'){
                                try{
                                    Integer.parseInt(Character.toString(editext.getText().toString().charAt(i)));
                                }catch (Exception e){
                                    if(     editext.getText().toString().charAt(i)!='a'&&editext.getText().toString().charAt(i)!='A'&&
                                            editext.getText().toString().charAt(i)!='b'&&editext.getText().toString().charAt(i)!='B'&&
                                            editext.getText().toString().charAt(i)!='c'&&editext.getText().toString().charAt(i)!='C'&&
                                            editext.getText().toString().charAt(i)!='d'&&editext.getText().toString().charAt(i)!='D'&&
                                            editext.getText().toString().charAt(i)!='e'&&editext.getText().toString().charAt(i)!='E'&&
                                            editext.getText().toString().charAt(i)!='f'&&editext.getText().toString().charAt(i)!='F')
                                        pode=false;
                                }
                            }
                        }
                        if(editext.getText().toString().length()==7 && editext.getText().toString().charAt(0)=='#' && pode) {
                            editor.putString("color", editext.getText().toString());
                            SaveColor();
                        }else if(editext.getText().toString().length()==6 && !editext.getText().toString().contains("#") && pode){
                            editor.putString("color", "#" + editext.getText().toString());
                            SaveColor();
                        }else{
                            Toast.makeText(MinhaEmpresaActivity.this,"Valor inválido",Toast.LENGTH_SHORT).show();
                        }*/
                        try{
                            Color.parseColor(editext.getText().toString());
                            editor.putString("color", editext.getText().toString());
                            SaveColor();
                        }catch(Exception e){
                            try{
                                Color.parseColor("#" + editext.getText().toString());
                                editor.putString("color", editext.getText().toString());
                                SaveColor();
                            }catch (Exception ex){
                                Toast.makeText(MinhaEmpresaActivity.this,"Valor inválido",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    private void SaveColor() {
                        editor.commit();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                    }
                }).setNegativeButton(getString(R.string.cancel), null).show();
            }
        });
    }
}
