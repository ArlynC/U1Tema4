package com.example.u1tema4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Servicio extends Activity {
    private final VibrateReceiver receiver = new VibrateReceiver();
    private IntentFilter intentFilter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //enciende el celular aunque este en pantalla de bloqueo
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_servicio);
        setContentView(R.layout.activity_servicio);

        intentFilter = new IntentFilter("RESPONSE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, intentFilter);

        Button arrancar = (Button) findViewById(R.id.boton_arrancar);
        arrancar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(Servicio.this, ServicioMusica.class));
                } else {
                    //inicializa el servicio
                    startService(new Intent(Servicio.this,
                            ServicioMusica.class));
                }
            }
        });
        Button detener = (Button) findViewById(R.id.boton_detener);
        detener.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stopService(new Intent(Servicio.this,
                        ServicioMusica.class));
            }
        });

        if ((ContextCompat.checkSelfPermission(Servicio.this,
                Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED)){
            arranque();
        } else {
            Handler handler = new Handler();
            handler.postDelayed(
                    new Runnable() {
                        public void run() {
                            if (ContextCompat.checkSelfPermission(Servicio.this,
                                    Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                permisolocalizacion();
                            } else {
                                solicitarPermiso(Manifest.permission.CALL_PHONE,
                                        "Sin el permiso" + " de estado de telefono", 0);
                            }
                        }
                    }, 2000L);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(3);
    }

    void permisolocalizacion(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            arranque();
        } else {
            solicitarPermiso(Manifest.permission.READ_CALL_LOG,
                    "Sin el permiso" + " de registro de llamadas", 1);
        }
    }


    public void solicitarPermiso(final String permiso, String justificacion, final int codigo) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Solicitud de permiso");
            dialogo1.setMessage(justificacion);
            dialogo1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    ActivityCompat.requestPermissions(Servicio.this, new String[]{permiso}, codigo);
                }
            });
            dialogo1.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permiso}, codigo);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permisolocalizacion();
            } else {
                solicitarPermiso(Manifest.permission.READ_PHONE_STATE,
                        "Sin el permiso" + " de telefono no podemos realizar llamadas.", 0);
            }
        }
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                arranque();
            } else {
                solicitarPermiso(Manifest.permission.READ_CALL_LOG,
                        "Sin el permiso" + " de ubicacion no podremos localizarte", 1);
            }
        }
    }
    void arranque() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
      /*  dialogo1.setTitle("FELICITACIONES");
        dialogo1.setMessage("Usted ya tiene los permisos necesarios para usar nuestra app");
        dialogo1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        dialogo1.show();*/
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void millamado(View view) {
        startActivity(new Intent(this,Anuncio_personalizado.class));
    }
    public void miintentservice(View view) {
        startActivity(new Intent(this,MiIntentService.class));
    }
}