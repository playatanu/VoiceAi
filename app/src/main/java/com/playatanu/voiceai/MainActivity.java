package com.playatanu.voiceai;



import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {


    TextToSpeech TTP;
    ImageView voiceimg;
    CameraManager camManager;
    String getCameraID;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(getApplicationContext(), MyServices.class));
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        voiceimg = findViewById(R.id.btnSpeak);

        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {

            getCameraID = camManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        TTP = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if (i != TextToSpeech.ERROR) {

                    TTP.setLanguage(Locale.UK);
                }
            }
        });


        voiceimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());


                if (intent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(intent, 10);
                } else {
                   // Toast.makeText(this,"Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }


            }
        });

        TTP.speak("Hi! Search Anythings", TextToSpeech.QUEUE_FLUSH, null);






    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);

                    String time = "time now";
                    String hi = "hi";
                    String hello = "hello";
                    String yt = "open YouTube";
                    String go = "open Google";
                    String play = "play music";
                    String who = "who are you";
                    String search = "search";
                    String flashon = "lights on";
                    String flashoff = "lights off";

                    if (hi.equals(text)) {
                        TTP.speak("Hello", TextToSpeech.QUEUE_FLUSH, null);
                    } else if (hello.equals(text)) {
                        TTP.speak("hi", TextToSpeech.QUEUE_FLUSH, null);
                    } else if (who.equals(text)) {

                        TTP.speak("I'm your voice A.I.", TextToSpeech.QUEUE_FLUSH, null);

                    } else if (time.equals(text)) {

                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String currentDateTimeString = sdf.format(d);

                        TTP.speak(currentDateTimeString, TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(this, currentDateTimeString, Toast.LENGTH_SHORT).show();

                    } else if (yt.equals(text)) {

                        Toast.makeText(this, "Opning Youtube", Toast.LENGTH_LONG).show();
                        TTP.speak("Opening Youtube", TextToSpeech.QUEUE_FLUSH, null);
                        Uri uri = Uri.parse("http://www.youtube.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else if (go.equals(text)) {
                        Toast.makeText(this, "Opning Google", Toast.LENGTH_LONG).show();
                        TTP.speak("Opening Google", TextToSpeech.QUEUE_FLUSH, null);
                        Uri uri = Uri.parse("http://www.google.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else if (play.equals(text)) {
                        Toast.makeText(this, "Play Music", Toast.LENGTH_LONG).show();
                        TTP.speak("Play Musing on Youtube", TextToSpeech.QUEUE_FLUSH, null);
                        Uri uri = Uri.parse("https://youtu.be/ZHKQgmpabD8");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }




                       else if (flashon.equals(text)) {

                            TTP.speak("Flashlight On", TextToSpeech.QUEUE_FLUSH, null);


                            try {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    camManager.setTorchMode(getCameraID, true);
                                }


                                Toast.makeText(MainActivity.this, "Flashlight is turned ON", Toast.LENGTH_SHORT).show();
                            } catch (CameraAccessException e) {

                                e.printStackTrace();
                            }


                        } else if (flashoff.equals(text)) {


                            TTP.speak("Flashlight Off", TextToSpeech.QUEUE_FLUSH, null);


                            try {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    camManager.setTorchMode(getCameraID, false);
                                }


                                Toast.makeText(MainActivity.this, "Flashlight is turned OFF", Toast.LENGTH_SHORT).show();
                            } catch (CameraAccessException e) {

                                e.printStackTrace();
                            }


                        } else {
                            TTP.speak("Sorry, I don't understand", TextToSpeech.QUEUE_FLUSH, null);
                        }


                    }

                }


        }
    }

