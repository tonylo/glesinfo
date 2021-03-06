package com.example.glesinfo;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

public class GlesInfoActivity extends Activity {

    static String TAG = "GLESINFO";
    private GLSurfaceView mGLView;
    private TextView mTextView;
    boolean mNoGLView = false;

    class ClearRenderer implements GLSurfaceView.Renderer {
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // Do nothing special.
            dumpGLES();
            weAreDone();
        }

        public void dumpGLES() {

            output("GLES11:");
            glPrint("GLES11.GL_MAX_TEXTURE_SIZE", GLES11.GL_MAX_TEXTURE_SIZE);
            glPrint("GLES11.GL_MAX_ELEMENTS_INDICES", GLES11.GL_MAX_ELEMENTS_INDICES);
            glPrint("GLES11.GL_MAX_ELEMENTS_VERTICES", GLES11.GL_MAX_ELEMENTS_VERTICES);
            glPrint("GLES11.GL_MAX_LIGHTS", GLES11.GL_MAX_LIGHTS);
            glPrint("GLES11.GL_MAX_MODELVIEW_STACK_DEPTH", GLES11.GL_MAX_MODELVIEW_STACK_DEPTH);
            glPrint("GLES11.GL_MAX_PROJECTION_STACK_DEPTH", GLES11.GL_MAX_PROJECTION_STACK_DEPTH);
            glPrint("GLES11.GL_MAX_TEXTURE_STACK_DEPTH", GLES11.GL_MAX_TEXTURE_STACK_DEPTH);
            glPrint("GLES11.GL_MAX_TEXTURE_UNITS", GLES11.GL_MAX_TEXTURE_UNITS);
            glPrint("GLES11.GL_MAX_VIEWPORT_DIMS", GLES11.GL_MAX_VIEWPORT_DIMS, 2);
            output("GLES20:");
            glPrint("GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS", GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
            glPrint("GLES20.GL_MAX_CUBE_MAP_TEXTURE_SIZE", GLES20.GL_MAX_CUBE_MAP_TEXTURE_SIZE);
            glPrint("GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS", GLES20.GL_MAX_FRAGMENT_UNIFORM_VECTORS);
            glPrint("GLES20.GL_MAX_RENDERBUFFER_SIZE", GLES20.GL_MAX_RENDERBUFFER_SIZE);
            glPrint("GLES20.GL_MAX_TEXTURE_IMAGE_UNITS", GLES20.GL_MAX_TEXTURE_IMAGE_UNITS);
            glPrint("GLES20.GL_MAX_TEXTURE_SIZE", GLES20.GL_MAX_TEXTURE_SIZE);
            glPrint("GLES20.GL_MAX_VARYING_VECTORS", GLES20.GL_MAX_VARYING_VECTORS);
            glPrint("GLES20.GL_MAX_VERTEX_ATTRIBS", GLES20.GL_MAX_VERTEX_ATTRIBS);
            glPrint("GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS", GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS);
            glPrint("GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS", GLES20.GL_MAX_VERTEX_UNIFORM_VECTORS);
            glPrint("GLES20.GL_MAX_VIEWPORT_DIMS", GLES20.GL_MAX_VIEWPORT_DIMS, 2);
        }

        public void onSurfaceChanged(GL10 gl, int w, int h) {
        }

        public void onDrawFrame(GL10 gl) {
        }
    }

    final static int kMaxConfs = 100;

    protected void onStart() {
        super.onStart();
    }
    protected void onResume() {
        super.onResume();
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View mV = super.onCreateView(parent, name, context, attrs);

        /*
         * We enter this method a few times, but first time around the textview
         * won't be ready, we then we close the glview hitting here again, at
         * which point we want to dump the log
         */
        if (mNoGLView && mTextView != null) {
            writeLogToTextView();
        }
        return mV;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onPostResume() {
        super.onPostResume();
    }

    protected void weAreDone() {
        ViewGroup vg = (LinearLayout)findViewById(R.id.linearlayout1);
        mNoGLView = true;
        vg.removeView(mGLView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gles_info);
        mGLView = (GLSurfaceView) findViewById(R.id.glSurfaceViewID);
        mTextView = (TextView) findViewById(R.id.maintext);
        mGLView.setRenderer(new ClearRenderer());

        output("EGL:");

        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(dpy, version);

        EGLConfig[] conf_egl = new EGLConfig[kMaxConfs];
        int[] conf_ids = new int[1];
        egl.eglGetConfigs(dpy, conf_egl, kMaxConfs, conf_ids);

        int[] value = new int[1];
        output("Number of configs: " + conf_ids[0] + "\n");
        output("RGB sz alpha/depth/afmt/amask/samplesize/samplebuffers");

        for (int i = 0; i < conf_ids[0]; i++) {
            Boolean rv;
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_RED_SIZE, value);
            int red_size = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_GREEN_SIZE, value);
            int green_size = value[0];
            rv =egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_BLUE_SIZE, value);
            int blue_size = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_ALPHA_SIZE, value);
            int alpha_size = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_DEPTH_SIZE, value);
            int depth_size = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_ALPHA_FORMAT, value);
            int alpha_fmt = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_ALPHA_MASK_SIZE, value);
            int alpha_mask_size = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_SAMPLE_BUFFERS, value);
            int egl_sample_buffers = value[0];
            rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_SAMPLES, value);
            int egl_samples = value[0];
            String op = "" + red_size + green_size + blue_size + " " + alpha_size +  "/" + depth_size +  "/" + alpha_fmt +  "/" + alpha_mask_size + "/" + egl_sample_buffers + "/" + egl_samples;
            output(op);
        }
    }

    public void output(String text) {
        Log.i(TAG, text);
        appendLog(text);
    }

    static boolean mLogInit = false;
    File mLogFile = new File("/sdcard/glesinfo.txt");

    public void appendLog(String text) {
        if (mLogInit == false) {
            if (mLogFile.exists()) {
                mLogFile.delete();
            }
            try {
                mLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            mLogInit = true;
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(mLogFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLogToTextView() {
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(mLogFile));
            while ((sCurrentLine = br.readLine()) != null) {
                mTextView.append(sCurrentLine);
                mTextView.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void glPrint(String label, int value) {
        glPrint(label, value, 1);
    }

    public void glPrint(String label, int value, int len) {
        int[] max = new int[len];
        GLES20.glGetIntegerv(value, max, 0);
        String op = label + ":" + max[0] + (len > 1 ? ":" +  max[1] : "");
        output(op);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gles_info, menu);
        return true;
    }

}
