package com.example.gl_app;
import android.content.Context;
import android.opengl.GLSurfaceView;

class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);

        renderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        // This setting prevents the GLSurfaceView frame from being redrawn
        // until you call requestRender(), which is more efficient for this sample app.
    }
}