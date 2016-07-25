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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    String ip1=null;
    String port1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        final EditText ip=(EditText) this.findViewById(R.id.editText);
        final EditText port=(EditText) this.findViewById(R.id.editText2);
        Button connectButton = (Button) this.findViewById(R.id.button);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  ip1 = ip.getText().toString();
                  port1= port.getText().toString();
                if(!(port1.isEmpty()||ip1.isEmpty())) {
                    ConnectHistory ch = new ConnectHistory(MainScreen.this);
                    ch.insertData(ip1,port1);
                    new Connect().execute(ip1, port1);
                    Toast.makeText(MainScreen.this, "CONNECTING...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainScreen.this, "ENTER ALL FIELDS", Toast.LENGTH_SHORT).show();
                    if(port1.isEmpty())
                       port.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    if(ip1.isEmpty())
                        ip.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                }
            }
        }
        );

        ListView lv = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> ar= new ArrayAdapter<String>(this,R.layout.listlayout);
        ConnectHistory ch = new ConnectHistory(this);

        ArrayList<String> arl =  ch.getData();
        if(arl!=null){
           for(int i=arl.size()-1;i>-1;i--)
            ar.add(arl.get(i));
        }
        lv.setAdapter(ar);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String datapos = ar.getItem(position);
                String[] data = datapos.split(":");
                ip.setText(data[0]);
                port.setText(data[1]);
                ip1=data[0];
                port1=data[1];
                new Connect().execute(data[0],data[1]);
                Toast.makeText(MainScreen.this, "CONNECTING...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            ConnectHistory ch = new ConnectHistory(MainScreen.this);
            ch.clearTable();
            ListView lv = (ListView) findViewById(R.id.listView);
            ArrayList<String> arl =  ch.getData();

            lv.setAdapter(null);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
   private   class Connect extends AsyncTask<String,Void,Boolean>{

       ConnectionManager com;
       @Override
       protected Boolean doInBackground(String... params) {
          try {
              com = new ConnectionManager(params[0], Integer.parseInt(params[1]));

              return true;
          }catch(Exception e){

              return false;
          }
       }
       protected void onPostExecute(Boolean result){
          if(result) {
              Toast.makeText(MainScreen.this, "CONNECTED!", Toast.LENGTH_SHORT).show();

              Intent inten = new Intent(MainScreen.this,SendActivity.class);
              inten.putExtra("IP",ip1);
              inten.putExtra("PORT",port1);
              startActivity(inten);
              MainScreen.this.finish();
          }else
            Toast.makeText(MainScreen.this,"CONNECTON FAILED!",Toast.LENGTH_SHORT).show();
       }
   }
}

