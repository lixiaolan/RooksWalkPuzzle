package com.seventhharmonic.com.freebeeline.levelresources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.*;

import com.seventhharmonic.android.freebeeline.GlobalApplication;

import android.content.Context;
import android.sax.*;
import android.util.Log;
import android.util.Xml;

public class PuzzleParser{
	String TAG = "PuzzleParser";
	String PUZZLE = "puzzle";
	String HEIGHT = "height";
	String WIDTH = "width";
	String BOARD = "board";
	String PATH = "path";
	String HINT = "hint";
	String NUMBER = "number";
	String INDEX = "index";
	String DIRECTION = "direction";
	String ID = "id";
    
	public PuzzleParser(){
        
    }
    
    public Puzzle parse(String xml) {
    	final Puzzle puzz = new Puzzle();
    	final Hint hint = new Hint(); 
    	RootElement root = new RootElement(PUZZLE); 
    	Element hizzle = root.getChild(HINT);
    	
    	
    	root.setStartElementListener(new StartElementListener(){
    		public void start(Attributes a){
    			puzz.reset();
    			puzz.setId(Long.parseLong(a.getValue(ID)));
    			puzz.setBeforeFlower(a.getValue("before_flower"));
    			puzz.setAfterFlower(a.getValue("after_flower"));
    			puzz.setText(a.getValue("text"));
    		}
    	});
    	
    	root.getChild(WIDTH).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setWidth(Integer.parseInt(body));
    		}
    	});
    	
    	root.getChild(HEIGHT).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setHeight(Integer.parseInt(body));
    		}
    	});
    	
    	root.getChild(BOARD).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setBoard(body);
    		}
    	});
    	
    	root.getChild(PATH).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setPath(body);
    		}
    	});
    	
    	hizzle.getChild(NUMBER).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			hint.setNumber(Integer.parseInt(body));
    		}
    	});
    	
    	hizzle.getChild(INDEX).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			hint.setIndex(Integer.parseInt(body));
    		}
    	});
    	
    	hizzle.getChild(DIRECTION).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			hint.setDirection(body);
    		}
    	});
    	
    	root.getChild(HINT).setEndElementListener(new EndElementListener(){
    		public void end(){
    			puzz.addHint(hint.copy());
    		}
    		
    	});
    	
    	root.setEndElementListener( new EndElementListener() {
    		public void end(){
    			puzz.setChapter(null);
    		}
    	});
    	
    	
    	try{		
    		Xml.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")), Xml.Encoding.UTF_8, 
    				root.getContentHandler());
    	} catch(Exception e) {
    		 throw new RuntimeException(e);
    	}
    	
    	return puzz;
    }
        
    
}
