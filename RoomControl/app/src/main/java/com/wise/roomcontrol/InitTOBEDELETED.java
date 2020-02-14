package com.wise.roomcontrol;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
        dao.logado=null;
        /*if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(1, "canal", NotificationManager.IMPORTANCE_DEFAULT );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, 1).setSmallIcon(R.drawable.roomtracker_logo).setContentTitle("Reunião")
                    .setContentText("Você tem uma reunião hoje").setPriority(NotificationCompat.PRIORITY_HIGH);
        }*/
        Empresa wise=new Empresa("Wise: Fábrica de Softwares","wises.com.br",'M',true);
        Empresa sraw=new Empresa("Sraw Rats: Fábrica de Hardwares","srawrats.io",'M',true);
        Sala cowork=new Sala("Sala de Coworking",16,"4",true,true);
        wise.addSala(cowork);
        Sala jedi=new Sala("Conselho Jedi",0,"3",false,true);
        wise.addSala(jedi);
        Sala sala=new Sala("Sala 171",0,"4",false,false);
        wise.addSala(sala);
        Sala endar=new Sala("Endar Spire",25,"1",true,true);
        wise.addSala(endar);
        Sala tantive=new Sala("Tantive IV",6,"7",true,false);
        wise.addSala(tantive);
        Sala sala42=new Sala("Sala 42",7,"2",false,true);
        wise.addSala(sala42);
        Sala vulco=new Sala("Vulcão de Mordor",16,"7",true,false);
        sraw.addSala(vulco);
        Sala ithil=new Sala("Minas Ithil",7,"1",true,true);
        sraw.addSala(ithil);
        Sala barad=new Sala("Barad-dûr",0,"1",true,false);
        sraw.addSala(barad);
        Sala gondor=new Sala("Gondor",20,"2",false,false);
        sraw.addSala(gondor);
        Sala shire=new Sala("Condado",50,"1",false,false);
        sraw.addSala(shire);
        User clov=new User("clovis@wises.com.br","maturity3","Clovis");
        startActivity(new Intent(this,LoginActivity.class));
    }
}
