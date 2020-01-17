package com.example.u1tema4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Anuncio_personalizado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_personalizado);
    }

    public void llamarreceiver(View view) {
        sendBroadcast(new Intent("RESPONSE"));
    }



}
