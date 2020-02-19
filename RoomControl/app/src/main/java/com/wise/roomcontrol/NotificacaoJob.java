package com.wise.roomcontrol;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.wise.roomcontrol.activities.LoginActivity;
import com.wise.roomcontrol.adapters.ListaReunioesAdapter;
import com.wise.roomcontrol.classes.Reuniao;

import java.util.Calendar;

public class NotificacaoJob extends JobService {
    boolean jaFoiHj=false;
    @Override
    public boolean onStartJob(JobParameters params) {
        if(Build.VERSION.SDK_INT>=26) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Calendar agr = Calendar.getInstance();
                    if (agr.HOUR_OF_DAY == 13 && !jaFoiHj) {
                        jaFoiHj = true;
                        ListaReunioesAdapter adapter = new ListaReunioesAdapter(NotificacaoJob.this);
                        adapter.setSoPlayer(true);
                        adapter.atualiza();
                        for (Reuniao i:adapter.reunioes) {
                            if(i.getData(2)==agr.YEAR&&i.getData(1)==agr.MONTH&&i.getData(0)==agr.DAY_OF_MONTH){
                                NotificationChannel channel = new NotificationChannel("1", "canal", NotificationManager.IMPORTANCE_DEFAULT);
                                channel.enableVibration(true);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.createNotificationChannel(channel);
                                Intent intent = new Intent(NotificacaoJob.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pendingIntent = PendingIntent.getActivity(NotificacaoJob.this, 0, intent, 0);
                                Notification notificacao = new NotificationCompat.Builder(NotificacaoJob.this, "1").setSmallIcon(R.drawable.roomtracker_logo).setContentTitle("Reunião")
                                        .setContentText("Você tem uma reunião hoje às "+i.getHorario(i.getHora1())).setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setContentIntent(pendingIntent).build();
                                startForeground(1, notificacao);
                            }
                        }
                    }
                }
            });
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}