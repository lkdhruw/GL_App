package com.example.gl_app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView gLView;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        //setContentView(R.layout.activity_main);
        //Context context = getApplicationContext();
        gLView = new MyGLSurfaceView(context);
        setContentView(gLView);
    }

}