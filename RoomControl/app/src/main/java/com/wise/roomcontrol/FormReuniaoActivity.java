package com.wise.roomcontrol;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.classes.Reuniao;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Date;

public class FormReuniaoActivity extends AppCompatActivity {

    private int[] data={1,1,2020};
    private Time horaIni, horaTer;
    private int[] hora1={12,00}, hora2={12,30};
    private int computadores=0;
    private boolean ac=false, projetor=false;
    private String descricao="";
    private ListaOpcoesAdapter adapterAux=new ListaOpcoesAdapter(FormReuniaoActivity.this);
    private Spinner drop;
    private Reuniao reuniao;
    final private Dao dao = new Dao();
    private int idsala;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reuniao);
        //possibilidades=new JSONObject();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText campoDescricao=findViewById(R.id.descripText);
        campoDescricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                descricao=campoDescricao.getText().toString();
            }
        });
        //locador=dao.logado.getUser();

        final CalendarView calendario=findViewById(R.id.calendarView);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView calendar, int ano, int mes, int dia){
                data[0]=dia;
                data[1]=mes;
                data[2]=ano-1900;
                atualizaOpcoes();
            }
        });

        final EditText horaInicio=findViewById(R.id.horaText);
        horaInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null) {
                    if (s.length() > 1) {
                        hora1[0] = Character.getNumericValue(s.charAt(0)) * 10 + Character.getNumericValue(s.charAt(1));
                        if (s.length() > 4) {
                            hora1[1] = Character.getNumericValue(s.charAt(3)) * 10 + Character.getNumericValue(s.charAt(4));
                            horaIni = new Time(hora1[0], hora1[1], 0);
                            atualizaOpcoes();
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && s.length()>=4){
                    if(s.toString().contains(":")){
                        hora1[0]=Integer.parseInt(s.toString().substring(0,s.toString().indexOf(":")-1));
                        hora1[1]=Integer.parseInt(s.toString().substring(s.toString().indexOf(":")+1,s.toString().length()-1));
                    }else if(s.toString().contains(".")){
                        hora1[0]=Integer.parseInt(s.toString().substring(0,s.toString().indexOf(".")-1));
                        hora1[1]=Integer.parseInt(s.toString().substring(s.toString().indexOf(".")+1,s.toString().length()-1));
                    }else{
                        hora1[0]=Integer.parseInt(s.toString().substring(0,1));
                        hora1[1]=Integer.parseInt(s.toString().substring(2,s.toString().length()-1));
                    }
                }
            }
        });

        final EditText horaFim=findViewById(R.id.horaText2);
        horaFim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>1){
                    hora2[0]= Character.getNumericValue(s.charAt(0))*10+ Character.getNumericValue(s.charAt(1));
                    if(s.length()>4) {
                        hora2[1]= Character.getNumericValue(s.charAt(3))*10+ Character.getNumericValue(s.charAt(4));
                        horaTer = new Time(hora2[0],hora2[1],0);
                        atualizaOpcoes();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && s.length()>=4){
                    if(s.toString().contains(":")){
                        hora2[0]=Integer.parseInt(s.toString().substring(0,s.toString().indexOf(":")-1));
                        hora2[1]=Integer.parseInt(s.toString().substring(s.toString().indexOf(":")+1,s.toString().length()-1));
                    }else if(s.toString().contains(".")){
                        hora2[0]=Integer.parseInt(s.toString().substring(0,s.toString().indexOf(".")-1));
                        hora2[1]=Integer.parseInt(s.toString().substring(s.toString().indexOf(".")+1,s.toString().length()-1));
                    }else{
                        hora2[0]=Integer.parseInt(s.toString().substring(0,1));
                        hora2[1]=Integer.parseInt(s.toString().substring(2,s.toString().length()-1));
                    }
                }}
        });

        final EditText pcs=findViewById(R.id.pcText);
        pcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && s.length()>0) {
                    computadores = Integer.parseInt(s.toString());
                    atualizaOpcoes();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        final Switch arcon=findViewById(R.id.switchAC);
        arcon.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                ac=isChecked;
                atualizaOpcoes();
            }
        });

        final Switch projet=findViewById(R.id.switchProjetor);
        projet.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                projetor=isChecked;
                atualizaOpcoes();
            }
        });


        drop=findViewById(R.id.dropdown);
        drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idsala= adapterAux.getItem(position).getId();
                reuniao = new Reuniao(descricao,dao.logado.getId(), adapterAux.getItem(position),data,hora1,hora2);
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        Button botaoSave=findViewById(R.id.saveButton);
        botaoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadBar).setVisibility(View.VISIBLE);
                if(descricao!=null&&reuniao!=null){
                    String resultad="";
                    //ListaReunioesAdapter.reunioes.add(reuniao);
                    try {
                        JSONObject json = new JSONObject();
                        String jsonEncoded = "kyugasdykgufsdgkutsdagfiu";
                        json.put("id_sala", idsala);
                        json.put("id_usuario", dao.logado.getId());
                        json.put("descricao", descricao);
                        json.put("data_hora_inicio", new Date(data[2],data[1],data[0],hora1[0],hora1[1]).getTime());
                        json.put("data_hora_fim", new Date(data[2],data[1],data[0],hora2[0],hora2[1]).getTime());
                        json.put("ativo", true);
                        jsonEncoded = Base64.encodeToString(json.toString().getBytes("UTF-8"), Base64.NO_WRAP);
                        Log.i("teste", "\n\n\nonClick: "+jsonEncoded+"\n\n\n\n");
                        String[] a1 = {"novaReserva"}, a2 = {jsonEncoded};

                        StringBuilder result = new StringBuilder();
                        URL url = new URL("http://172.30.248.111:8080/ReservaDeSala/rest/reserva/cadastrar");
                        System.out.println(url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("authorization", "secret");
                        conn.setRequestProperty("novaReserva", jsonEncoded);
                        conn.connect();

                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            System.out.println(line);
                            result.append(line);
                        }
                        rd.close();
                        Log.i("teste", "ServerInOutput: "+result.toString());
                        resultad=result.toString();
                        //String[] a1 = {"id_sala","id_usuario","descricao","data_hora_inicio","data_hora_fim","ativo","novaReserva"}, a2 = {idsala,dao.logado.getId(),descricao,new Date(data[2],data[1],data[0],hora1[0],hora1[1]),new Date(data[2],data[1],data[0],hora2[0],hora2[1]),true};
                        //dao.ServerInOutput(true, "reserva/cadastrar", a1, a2);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    findViewById(R.id.loadBar).setVisibility(View.INVISIBLE);
                    //dao.ServerInOutput(true,)
                    if(resultad.contains("Reserva realizada com sucesso"))
                        finish();
                    else{
                        builder.setTitle("Erro").setMessage(resultad).show();
                        //Toast.makeText(FormReuniaoActivity.this,resultad,Toast.LENGTH_LONG);

                    }
                }else{
                    Toast.makeText(FormReuniaoActivity.this,"Um ou mais campos não foi preenchido",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void atualizaOpcoes() {
        Log.i("teste", "FILTROS---  dia:"+data[0]+"/"+data[1]+"/"+data[2]+" -- hora de inicio: "+hora1[0]+":"+hora1[1]+" -- hora de termino: "+hora2[0]+":"+hora2[1]+"projetor: "+projetor);
        adapterAux.filtraSalas(dao.logado.getEmpresaId(),computadores,projetor,ac);
        try {
            adapter = new ArrayAdapter<>(FormReuniaoActivity.this, android.R.layout.simple_spinner_item, adapterAux.getAsString(3));
            drop.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
