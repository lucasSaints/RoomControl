package com.wise.roomcontrol;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

public class FormReuniaoActivity extends AppCompatActivity {

    private int[] data={1,1,2020};
    private int[] hora1={12,00}, hora2={12,30};
    private int computadores=0;
    private boolean ac=false, projetor=false;
    private String descricao, locador;
    //JSONObject possibilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reuniao);
        /*possibilidades=new JSONObject();
        Context ctx=this;
        InputStream is = ctx.getAssets().open("pacoteDeTeste.json");*/
        final CalendarView calendario=findViewById(R.id.calendarView);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView calendar, int ano, int mes, int dia){
                data[0]=dia;
                data[1]=mes+1;
                data[2]=ano;
                atualizaOpcoes();
            }
        });

        final EditText horaInicio=findViewById(R.id.horaText);
        horaInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>1){
                    hora1[0]= Character.getNumericValue(s.charAt(0))*10+ Character.getNumericValue(s.charAt(1));
                    if(s.length()>4) {
                        hora1[1]= Character.getNumericValue(s.charAt(3))*10+ Character.getNumericValue(s.charAt(4));
                        atualizaOpcoes();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
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
                        atualizaOpcoes();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText pcs=findViewById(R.id.pcText);
        pcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                computadores=Integer.parseInt(s.toString());
                atualizaOpcoes();
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
    }

    private void atualizaOpcoes() {
        Log.i("teste", "FILTROS---  dia:"+data[0]+"/"+data[1]+"/"+data[2]+" -- hora de inicio: "+hora1[0]+":"+hora1[1]+" -- hora de termino: "+hora2[0]+":"+hora2[1]+"projetor: "+projetor);

    }


}
