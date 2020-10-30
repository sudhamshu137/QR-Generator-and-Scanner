package com.example.qrgeneratorandscanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button click, btnCopy, btnBrowse;
    TextView res;
    String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Activity activity = this;

        btnBrowse = findViewById(R.id.btnBrowse);
        btnCopy = findViewById(R.id.btnCopy);
        res = findViewById(R.id.cardResult);

        res.setText("");

        click = findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt(" ");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resultString = res.getText().toString();

                if(resultString.isEmpty()){
                    Toast.makeText(MainActivity.this,"It's Empty!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Patterns.WEB_URL.matcher(resultString).matches()){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(resultString));
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"URL is not valid",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultString = res.getText().toString();

                if(resultString.isEmpty()){
                    Toast.makeText(MainActivity.this,"It's Empty!",Toast.LENGTH_SHORT).show();
                }
                else{
                    resultString = res.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", resultString);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "text copied to clipboard", Toast.LENGTH_LONG).show();
                }

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.QRgenerator:
                        startActivity(new Intent(MainActivity.this,QRgenerator.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(MainActivity.this,About.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (result != null) {
            if(result.getContents() != null){

                resultString = result.getContents();
                res.setText(resultString);

            }
            else {
                Toast.makeText(MainActivity.this,"cancelled",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}