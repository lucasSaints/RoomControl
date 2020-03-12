package com.wise.roomcontrol.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import com.wise.roomcontrol.Dao;
import com.wise.roomcontrol.classes.Empresa;
import com.wise.roomcontrol.classes.Sala;
import com.wise.roomcontrol.classes.User;

public class InitTOBEDELETED extends AppCompatActivity {
    final private Dao dao=new Dao();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //getResources().setBoolean
        dao.logado=null;
        dao.cadastrado=null;
        /*Empresa wise=new Empresa("Wise: Fábrica de Softwares","wises.com.br",'M',true);
        Empresa sraw=new Empresa("Sraw Rats: Fábrica de Hardwares","srawrats.io",'M',true);
        Sala cowork=new Sala("Sala de Coworking",16,2,"4",true,true);
        wise.addSala(cowork);
        Sala jedi=new Sala("Conselho Jedi",0,2,"3",false,true);
        wise.addSala(jedi);
        Sala sala=new Sala("Sala 171",0,2,"4",false,false);
        wise.addSala(sala);
        Sala endar=new Sala("Endar Spire",25,2,"1",true,true);
        wise.addSala(endar);
        Sala tantive=new Sala("Tantive IV",6,2,"7",true,false);
        wise.addSala(tantive);
        Sala sala42=new Sala("Sala 42",7,2,"2",false,true);
        wise.addSala(sala42);
        Sala vulco=new Sala("Vulcão de Mordor",16,2,"7",true,false);
        sraw.addSala(vulco);
        Sala ithil=new Sala("Minas Ithil",7,2,"1",true,true);
        sraw.addSala(ithil);
        Sala barad=new Sala("Barad-dûr",0,2,"1",true,false);
        sraw.addSala(barad);
        Sala gondor=new Sala("Gondor",20,2,"2",false,false);
        sraw.addSala(gondor);
        Sala shire=new Sala("Condado",50,2,"1",false,false);
        sraw.addSala(shire);
        User clov=new User("clovis@wises.com.br","maturity3","Clovis");*/
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
