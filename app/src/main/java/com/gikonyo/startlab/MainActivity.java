package com.gikonyo.startlab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button textButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

   public void onClick(View view) {
        Intent intent=new Intent(this,MessageService.class);
        //now we add text o the intent
        intent.putExtra(MessageService.EXTRA_MESSAGE,getString(R.string.button_response));
        //now start the service
        startService(intent);
    }
}