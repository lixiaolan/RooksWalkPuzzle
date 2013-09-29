/*
 * http://stackoverflow.com/questions/13286358/sharing-to-facebook-twitter-via-share-intent-android
 */

package com.example.android.opengl;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;


public class EndDialog {
    private Context mContext;
    private Dialog mDialog;
    
    LayoutInflater mInflater;
    
    public EndDialog(Context context) {
    	this.mContext = context;
    }


@SuppressLint("NewApi")
//Build the actual dialog.
public void show() {
    	this.mInflater = LayoutInflater.from(mContext);
    //Declare out intent.
        final View endView = mInflater.inflate(R.layout.end_dialog, null);
        //AlertDialog.Builder builder;
        //ContextThemeWrapper ctw = new ContextThemeWrapper( mContext, R.style.DialogLight );
        //builder = new AlertDialog.Builder(mContext, R.style.DialogLight);
        //builder.setView(endView);
      
        
        mDialog = new Dialog(mContext, R.style.DialogLight);//builder.create();
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window w = mDialog.getWindow();
        w.setGravity(Gravity.BOTTOM);
        mDialog.setContentView(endView);
        mDialog.show();
}

}