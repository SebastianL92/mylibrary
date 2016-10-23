package com.lawdev.mylibrary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private final String outpanUrl = "https://api.outpan.com/v2/products/";
    private final String apiKey = "?apikey=918355e7993103dbf7dfde48b91dfcc7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scanBtn.setOnClickListener(this);
    }
    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + lookUpAuthor(scanContent));
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String lookUpAuthor(String content) {
        String author = downloadHTTP(outpanUrl + content + apiKey);
        return author;
    }

    private String lookUpTitle(String content) {
        return content;
    }

    private String lookUpYear(String content) {
        return content;
    }

    private String downloadHTTP(String url) {
        String resultLine = "";

        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(connection.getInputStream())));

            String line;

            while((line = bufferedReader.readLine()) != null) {
                resultLine = resultLine + "\n" + line;
            }
            bufferedReader.close();
        } catch (Exception e) {
            Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
        }
        return resultLine;
    }


}