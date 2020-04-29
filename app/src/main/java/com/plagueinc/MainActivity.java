package com.plagueinc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.plagueinc.model.City;
import com.plagueinc.model.Population;
import com.plagueinc.model.Preferences;
import com.plagueinc.model.ScenceFrame;
import com.plagueinc.view.GameSurfaceView;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    ConstraintLayout container;
    Population population;
    City city;
    ScenceFrame scenceFrame;
    GameSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG,"onCreate Invoked");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.viewContainer);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        city = new City(width,height);
        population = new Population(city, Preferences.init_population);
        scenceFrame = new ScenceFrame(city,population);
        view = new GameSurfaceView(this);
        view.setScenceFrame(scenceFrame);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    scenceFrame.addInfectedPerson((int)event.getX(),(int)event.getY());
                    return true;
                }
                return false;
            }
        });
        container.addView(view);
    }
}
