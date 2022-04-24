package com.example.cryptographer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtIn;
    EditText txtOut;
    EditText txtKey;
    SeekBar seekBar;
    Button btnEnCode;
    FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         txtIn = (EditText)findViewById(R.id.txtIn);
         txtOut = (EditText)findViewById(R.id.txtOut);
         txtKey = (EditText)findViewById(R.id.TextNumber);
         seekBar = (SeekBar) findViewById(R.id.sBar);
         btnEnCode = (Button) findViewById(R.id.btnEnCode);
         fab = (FloatingActionButton)findViewById(R.id.fabIcon);


         Intent receiveIntent = getIntent();
         String receiveText = receiveIntent.getStringExtra(Intent.EXTRA_TEXT);
         if(receiveIntent != null)
             txtIn.setText(receiveText);

         seekBar.setProgress(18);

         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent shareIntent = new Intent(Intent.ACTION_SEND);
                 shareIntent.setType("text/plain");
                 shareIntent.putExtra(Intent.EXTRA_TEXT, txtOut.getText().toString());

                 try{
                     startActivity(Intent.createChooser(shareIntent, "Share message..."));
                 }catch(android.content.ActivityNotFoundException ex){
                     Toast.makeText(MainActivity.this, "Error: Couldn't share.", Toast.LENGTH_SHORT).show();
                 }

                 Snackbar.make(view, "Share your encryption", Snackbar.LENGTH_LONG).setAction("Action", null).show();
             }
         });

         btnEnCode.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 int key = Integer.parseInt(txtKey.getText().toString());
                 String message = txtIn.getText().toString();
                 String output = encode(message, key);
                 txtOut.setText(output);
             }
         });


         seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 int key = seekBar.getProgress() - 13;
                 String message = txtIn.getText().toString();
                 String output = encode(message, key);
                 txtKey.setText("" + key);
             }

             @Override
             public void onStartTrackingTouch(SeekBar seekBar) { }

             @Override
             public void onStopTrackingTouch(SeekBar seekBar) { }
         });
    }

    public String encode(String message, int keyVal){
        String output = "";

        char key = (char)keyVal;				// Ключ пользователя для шифрования


        for(int i = 0; i <= message.length()-1; i++) {
            char input = message.charAt(i);

            if(input >= 'A' && input <= 'Z'){ // ПРОВЕРКА для заглавных букв
                input += key;

                if(input > 'Z')
                    input -= 26;

                if(input < 'A')
                    input += 26;
            }

            if(input >= 'a' && input <= 'z'){ // ПРОВЕРКА для строчных букв
                input += key;

                if(input > 'z')
                    input -= 26;

                if(input < 'a')
                    input += 26;

            }

            if(input >= '0' && input <= '9') {
                input += (keyVal % 10);

                if(input > '9')
                    input-=10;

                if(input < '0')
                    input+=10;
            }
            output+=input;
        }

        return output;
    }

}