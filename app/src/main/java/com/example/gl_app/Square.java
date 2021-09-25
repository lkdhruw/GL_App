package com.example.gl_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;


public class Square {
    private FloatBuffer vertexBuffer;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 4;
    static final int COORDS_PER_TEXTURE = 2;
    public static final int BYTES_PER_FLOAT = 4;
    static final int STRIDE = (COORDS_PER_VERTEX + COORDS_PER_TEXTURE)*BYTES_PER_FLOAT;

    private int positionHandle;
    private int colorHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    static float squareCoords[] = {
            // X, Y, Z, Scale, S, T
            // S, T Coordinates of texture
            // Triangle 1
            -0.5f,  0.5f, 0.0f, 1.0f, 0.0f, 1.0f,  // top left
            -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f,   // bottom right
            // Triangle 2
            -0.5f,  0.5f, 0.0f, 1.0f, 0.0f, 1.0f,  // top left
            0.5f, -0.5f, 0.0f, 1.0f,  1.0f, 0.0f,  // bottom right
            0.5f,  0.5f, 0.0f, 1.0f, 1.0f, 1.0f    // top right
            /*
            // Triangle 1
            -0.5f,  0.5f, 0.0f, 1.0f,  // top left
            -0.5f, -0.5f, 0.0f, 1.0f,   // bottom left
            0.5f, -0.5f, 0.0f, 1.0f,   // bottom right
            // Triangle 2
            -0.5f,  0.5f, 0.0f, 1.0f,   // top left
            0.5f, -0.5f, 0.0f, 1.0f,   // bottom right
            0.5f,  0.5f, 0.0f, 1.0f//,    // top right
            */
            /*
            // Line 1
            -0.5f, 0.0f, 0.0f, 1.5f,
            0.5f, 0.0f, 0.0f, 1.5f,
            // Line 2
            0.0f, 0.5f, 0.0f, 2.0f,
            0.0f, -0.8f, 0.0f, 1.0f,
            // Point 1
            0.45f, 0.45f, 0.0f, 1.9f
             */
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.93671875f, 0.76953125f, 0.22265625f, 1.0f };
    float color2[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    float color3[] = { 1.0f, 0.0f, 0.0f, 1.0f };

    private final int mProgram;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  gl_PointSize = 10.0;" +
                    "}";
    private int vPMatrixHandle;

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final VertexArray vertexArray;

    public Square() {
        context = MainActivity.context;

        // ------------------------
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.simple_fragment_shader);
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderSource);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderSource);
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
        // ------------------------
        
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        vertexArray = new VertexArray(squareCoords);
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment

        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        //GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data-
        /*GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);*/

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        //GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 3, 3);
        /*
        GLES20.glUniform4fv(colorHandle, 1, color3, 0);
        GLES20.glLineWidth(3.0f);

        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
        GLES20.glDrawArrays(GLES20.GL_LINES, 8, 2);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 10, 1);

         */
        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(0, textureProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,textureProgram.getTextureCoordinatesAttributeLocation(),TEXTURE_COORDINATES_COMPONENT_COUNT,STRIDE);
    }
}
