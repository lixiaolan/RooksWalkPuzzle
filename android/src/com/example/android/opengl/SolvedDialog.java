package com.example.android.opengl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SolvedDialog extends DialogFragment {

	Activity mContext;
	
	public SolvedDialog() {
		
		// Empty constructor required for DialogFragment
	}

	public void setContext(Activity context){
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.solved_fragment, container);
		getDialog().setTitle("Finished");
		return view;
	}
	
	public void share() {
		ShareHelper sh = new ShareHelper(mContext,"1","2","3","4","http://www.google.com");
		sh.share();

	}
}