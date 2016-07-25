package com.iot.adithya.iotproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Button on= (Button) findViewById(R.id.button2);
        Button off= (Button) findViewById(R.id.button3);
        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);
        Button deb= (Button) findViewById(R.id.button4);
        final TextView debtv=(TextView) findViewById(R.id.editText3);

        debtv.setHint("DEBUG SEQUENCE");

        deb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!debtv.getText().toString().isEmpty())
                      new Send().execute(debtv.getText().toString());
                else
                    debtv.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Send().execute("\nRN\n");
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Send().execute("\nRF\n");
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            String progress="";
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              if(progress<10)
                  this.progress="00"+progress;
              if(progress<100 && progress>9)
                  this.progress="0"+progress;
              if(progress>100)
                 this.progress=""+progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                new Send().execute("PWM"+progress);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class Send extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
          try {
              String ip = SendActivity.this.getIntent().getStringExtra("IP"), port = SendActivity.this.getIntent().getStringExtra("PORT");
              ConnectionManager com = new ConnectionManager(ip, Integer.parseInt(port));
              com.sendString(params[0]);
              com.closeConnection();
              return true;
          }catch (Exception e){

              return false;
          }

        }
        protected void onPostExecute(Boolean result){
            if(result) {
                Toast.makeText(SendActivity.this, "SENT!", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(SendActivity.this,"CONNECTON FAILED!",Toast.LENGTH_SHORT).show();
        }

    }




}
