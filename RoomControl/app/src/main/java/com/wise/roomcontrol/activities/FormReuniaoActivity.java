package com.wise.roomcontrol.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.R;
import com.wise.roomcontrol.adapters.ListaOpcoesAdapter;
import com.wise.roomcontrol.classes.Reuniao;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
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
    private Switch seg;
    private Switch ter;
    private Switch qua;
    private Switch qui;
    private Switch sex;
    private Switch sab;
    private Switch dom;
    private TextView segText;
    private TextView terText;
    private TextView quaText;
    private TextView quiText;
    private TextView sexText;
    private TextView sabText;
    private TextView domText;
    private Button alertaWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reuniao);
        drop=findViewById(R.id.dropdown);
        atualizaOpcoes();
        //possibilidades=new JSONObject();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText campoDescricao=findViewById(R.id.descripText);
        final EditText horaInicio=findViewById(R.id.horaText);
        final EditText horaFim=findViewById(R.id.horaText2);
        final EditText pcs=findViewById(R.id.pcText);
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
        final CalendarView calendario=findViewById(R.id.calendarView);
        final Button alertaCalendario=findViewById(R.id.alertCalendar);
        final Switch projet=findViewById(R.id.switchProjetor);
        final Switch arcon=findViewById(R.id.switchAC);
        seg=findViewById(R.id.segSwitch);
        ter=findViewById(R.id.terSwitch);
        qua=findViewById(R.id.quaSwitch);
        qui=findViewById(R.id.quiSwitch);
        sex=findViewById(R.id.sexSwitch);
        sab=findViewById(R.id.sabSwitch);
        dom=findViewById(R.id.domSwitch);
        segText=findViewById(R.id.segText);
        terText=findViewById(R.id.terText);
        quaText=findViewById(R.id.quaText);
        quiText=findViewById(R.id.quiText);
        sexText=findViewById(R.id.sexText);
        sabText=findViewById(R.id.sabText);
        domText=findViewById(R.id.domText);
        alertaWeek = findViewById(R.id.alertDate);

        final Switch modo = findViewById(R.id.switchMode);

        modo.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                atualizaLayoutCalendario(calendario, alertaCalendario, isChecked);
                if(isChecked){
                    setWeekItemsVisib(View.VISIBLE);
                    setConstraint(R.id.alertDate);
                }else{
                    setWeekItemsVisib(View.GONE);
                    setConstraint(R.id.calendarView);
                }
            }
        });

        atualizaLayoutCalendario(calendario, alertaCalendario, modo.isChecked());

        if(Build.VERSION.SDK_INT<20){
            arcon.setTextOff(" ");
            arcon.setTextOn(" ");
            projet.setTextOff(" ");
            projet.setTextOn(" ");
            //calendario.setLayoutParams(new ConstraintLayout.LayoutParams(calendario.getWidth(),330));
            alertaCalendario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogoDeData();
                }
            });
        }else{
            calendario.setMinDate(Calendar.getInstance().getTimeInMillis());
            calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                @Override
                public void onSelectedDayChange(CalendarView calendar, int ano, int mes, int dia){
                    SaveDate(ano, mes, dia);
                }
            });
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if(displayMetrics.widthPixels<300){
            horaFim.setHint("Fim");
            horaInicio.setHint("Iníc.");
        }

        horaInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null) {
                    if (s.length() == 2 && !s.toString().contains(":")) {
                        horaInicio.setText(s+":");
                        horaInicio.setSelection(horaInicio.getText().length());
                    }
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
                if(!horaInicio.hasFocus()) {
                    if (s != null && s.length() >= 4) {
                        if (s.toString().contains(":")) {
                            hora1[0] = Integer.parseInt(s.toString().substring(0, s.toString().indexOf(":") - 1));
                            hora1[1] = Integer.parseInt(s.toString().substring(s.toString().indexOf(":") + 1, s.toString().length() - 1));
                        } else if (s.toString().contains(".")) {
                            hora1[0] = Integer.parseInt(s.toString().substring(0, s.toString().indexOf(".") - 1));
                            hora1[1] = Integer.parseInt(s.toString().substring(s.toString().indexOf(".") + 1, s.toString().length() - 1));
                        } else if (s.toString().contains(",")) {
                            hora1[0] = Integer.parseInt(s.toString().substring(0, s.toString().indexOf(",") - 1));
                            hora1[1] = Integer.parseInt(s.toString().substring(s.toString().indexOf(",") + 1, s.toString().length() - 1));
                        } else {
                            hora1[0] = Integer.parseInt(s.toString().substring(0, 1));
                            hora1[1] = Integer.parseInt(s.toString().substring(2, s.toString().length() - 1));
                        }
                    } else if (s != null && s.length() == 2) {
                        hora1[0] = Integer.parseInt(s.toString());
                        hora1[1] = 0;
                    }
                }
            }
        });

        horaFim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null) {
                    if (s.length() == 2 && !s.toString().contains(":")) {
                        horaFim.setText(s+":");
                       horaFim.setSelection(horaFim.getText().length());
                    }
                    if (s.length() > 1) {
                        hora2[0] = Character.getNumericValue(s.charAt(0)) * 10 + Character.getNumericValue(s.charAt(1));
                        if (s.length() > 4) {
                            hora2[1] = Character.getNumericValue(s.charAt(3)) * 10 + Character.getNumericValue(s.charAt(4));
                            horaTer = new Time(hora2[0], hora2[1], 0);
                            atualizaOpcoes();
                        }
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!horaFim.hasFocus()) {
                    if (s != null && s.length() >= 4) {
                        if (s.toString().contains(":")) {
                            hora2[0] = Integer.parseInt(s.toString().substring(0, s.toString().indexOf(":") - 1));
                            hora2[1] = Integer.parseInt(s.toString().substring(s.toString().indexOf(":") + 1, s.toString().length() - 1));
                        } else if (s.toString().contains(".")) {
                            hora2[0] = Integer.parseInt(s.toString().substring(0, s.toString().indexOf(".") - 1));
                            hora2[1] = Integer.parseInt(s.toString().substring(s.toString().indexOf(".") + 1, s.toString().length() - 1));
                        } else if (s.toString().contains(",")) {
                            hora2[0] = Integer.parseInt(s.toString().substring(0, s.toString().indexOf(",") - 1));
                            hora2[1] = Integer.parseInt(s.toString().substring(s.toString().indexOf(",") + 1, s.toString().length() - 1));
                        } else {
                            hora2[0] = Integer.parseInt(s.toString().substring(0, 1));
                            hora2[1] = Integer.parseInt(s.toString().substring(2, s.toString().length() - 1));
                        }
                    } else if (s != null && s.length() == 2) {
                        hora2[0] = Integer.parseInt(s.toString());
                        hora2[1] = 0;
                    }
                }
            }
        });

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

        arcon.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                ac=isChecked;
                atualizaOpcoes();
            }
        });

        projet.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                projetor=isChecked;
                atualizaOpcoes();
            }
        });

        drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idsala= adapterAux.getItem(position).getId();
                reuniao = new Reuniao(descricao,dao.logado.getId(), adapterAux.getItem(position),data,hora1,hora2);
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        alertaWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoDeData();
            }
        });

        Button botaoSave=findViewById(R.id.saveButton);
        botaoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadBar).setVisibility(View.VISIBLE);
                if(descricao!=null&&reuniao!=null) {
                    String resultad = "";
                    try {
                        JSONObject json = new JSONObject();
                        String jsonEncoded = "kyugasdykgufsdgkutsdagfiu";
                        json.put("id_sala", idsala);
                        json.put("id_usuario", dao.logado.getId());
                        json.put("descricao", descricao);
                        if (modo.isChecked()){
                            json.put("repeticoes", Reuniao.getRepeticoesAsStringAlt(new Boolean[]{dom.isChecked(), seg.isChecked(), ter.isChecked(), qua.isChecked(), qui.isChecked(), sex.isChecked(), sab.isChecked()}));
                        }
                        json.put("data_hora_inicio", new Date(data[2], data[1], data[0], hora1[0], hora1[1]).getTime());
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
                    if(resultad.contains("Reserva realizada com sucesso"))
                        finish();
                    else{
                        builder.setTitle("Erro").setMessage(resultad).show();
                        //Toast.makeText(FormReuniaoActivity.this,resultad,Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(FormReuniaoActivity.this,"Um ou mais campos não foi preenchido",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void DialogoDeData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormReuniaoActivity.this);
        final DatePicker pique = new DatePicker(FormReuniaoActivity.this);
        pique.setMinDate(Calendar.getInstance().getTimeInMillis()-1000);
        pique.setCalendarViewShown(false);
        pique.setSpinnersShown(true);
        builder.setTitle(getString(R.string.choosedate)).setView(pique).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveDate(pique.getYear(), pique.getMonth(), pique.getDayOfMonth());
            }
        }).show();
    }

    public void setConstraint(int constraintId) {
        ConstraintLayout cons = findViewById(R.id.consLay);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cons);
        constraintSet.connect(R.id.fundoCalend,ConstraintSet.BOTTOM,constraintId,ConstraintSet.BOTTOM,0);
        constraintSet.applyTo(cons);
    }

    public void setWeekItemsVisib(int visib) {
        seg.setVisibility(visib);
        segText.setVisibility(visib);
        ter.setVisibility(visib);
        terText.setVisibility(visib);
        qua.setVisibility(visib);
        quaText.setVisibility(visib);
        qui.setVisibility(visib);
        quiText.setVisibility(visib);
        sex.setVisibility(visib);
        sexText.setVisibility(visib);
        sab.setVisibility(visib);
        sabText.setVisibility(visib);
        dom.setVisibility(visib);
        domText.setVisibility(visib);
        alertaWeek.setVisibility(visib);
    }

    public void atualizaLayoutCalendario(CalendarView calendario, Button alertaCalendario, boolean repe) {
        if(repe){
            calendario.setVisibility(View.GONE);
            alertaCalendario.setVisibility(View.GONE);
        }else {
            if (Build.VERSION.SDK_INT < 20) {
                calendario.setVisibility(View.INVISIBLE);
                alertaCalendario.setVisibility(View.VISIBLE);
            }else{
                calendario.setVisibility(View.VISIBLE);
                alertaCalendario.setVisibility(View.GONE);
            }
        }
    }

    public void SaveDate(int ano, int mes, int dia) {
        data[0]=dia;
        data[1]=mes;
        data[2]=ano-1900;
        atualizaOpcoes();
    }

    private void atualizaOpcoes() {
        Log.i("teste", "FILTROS---  dia:"+data[0]+"/"+data[1]+"/"+data[2]+" -- hora de inicio: "+hora1[0]+":"+hora1[1]+" -- hora de termino: "+hora2[0]+":"+hora2[1]+"projetor: "+projetor);
        adapterAux.filtraSalas(dao.logado.getEmpresaId(),computadores,projetor,ac);
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(FormReuniaoActivity.this, android.R.layout.simple_spinner_item, adapterAux.getAsString(3));
            drop.setAdapter(adapter);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
