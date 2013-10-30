package com.seventhharmonic.com.freebeeline.levelresources;

import java.io.IOException;
import java.io.InputStream;

import com.seventhharmonic.android.freebeeline.GlobalApplication;

import android.content.Context;
public class BaseParser {
  
    protected BaseParser(){
        try {
            //TODO: Parse xml here
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getInputStream(String file) {
        try{	
        	Context c = GlobalApplication.getContext();
            return c.getAssets().open(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
