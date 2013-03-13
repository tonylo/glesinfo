package com.example.glesinfo;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class GlesInfoActivity extends Activity {

	final static int kMaxConfs = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gles_info);
	    TextView text = (TextView) findViewById(R.id.maintext);
	    text.append("EGL:\n");
	    
	    EGL10 egl = (EGL10) EGLContext.getEGL();
	    EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
	    int[] version = new int[2];
	    egl.eglInitialize(dpy, version);
	    
	    EGLConfig[] conf_egl = new EGLConfig[kMaxConfs];
	    int[] conf_ids = new int[1];
	    egl.eglGetConfigs(dpy, conf_egl, kMaxConfs, conf_ids);

	    int[] value = new int[1];
	    text.append("Number of configs: " + conf_ids[0] + "\n");
	    
	    for (int i = 0; i < conf_ids[0]; i++) {
	    	Boolean rv;
	    	text.append("" + i + ": ");
	    	rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_RED_SIZE, value);
	    	text.append("" + (rv ? value[0] : "?"));
	    	text.append("/");
	    	rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_GREEN_SIZE, value);
	    	text.append("" + (rv ? value[0] : "?"));
	    	text.append("/");
	    	rv =egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_BLUE_SIZE, value);
	    	text.append("" + (rv ? value[0] : "?"));
	    	text.append(" ");
	    	
	    	text.append("alpha:");
	    	rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_ALPHA_SIZE, value);
	    	text.append("" + (rv ? value[0] : "?") + " ");
	    	text.append("depth:");
	    	rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_DEPTH_SIZE, value);
	    	text.append("" + (rv ? value[0] : "?") + " ");
	    	text.append("afmt:");
	    	rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_ALPHA_FORMAT, value);
	    	text.append("" + (rv ? value[0] : "?") + " ");
	    	text.append("amask:");
	    	rv = egl.eglGetConfigAttrib(dpy, conf_egl[i], EGL10.EGL_ALPHA_MASK_SIZE, value);
	    	text.append("" + (rv ? value[0] : "?") + " ");

	    	text.append("\n");

	    }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gles_info, menu);
		return true;
	}

}
