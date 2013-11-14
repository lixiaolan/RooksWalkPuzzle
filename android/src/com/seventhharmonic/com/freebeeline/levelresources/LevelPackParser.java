package com.seventhharmonic.com.freebeeline.levelresources;

import org.xml.sax.*;

import com.seventhharmonic.android.freebeeline.GlobalApplication;

import android.sax.*;
import android.util.Log;
import android.util.Xml;

public class LevelPackParser extends BaseParser {
	String TAG = "LevelPackParser";
	String LEVELPACK = "levelpack";
	String CHAPTER = "chapter";
	String PUZZLE = "puzzle";
	String TITLE = "title";
	String HEIGHT = "height";
	String WIDTH = "width";
	String BOARD = "board";
	String PATH = "path";
	String HINT = "hint";
	String NUMBER = "number";
	String INDEX = "index";
	String DIRECTION = "direction";
	String ID = "id";
	String AFTERIMAGE = "afterImage";
	String IMAGE = "image";
    public LevelPackParser(){
        
    }
    
    public LevelPack parse(String file) {
    	final LevelPack lp = new LevelPack();
    	final Chapter ch = new Chapter();
    	final Puzzle puzz = new Puzzle();
    	final Hint hint = new Hint(); 
    	RootElement root = new RootElement(LEVELPACK); 
    	Element chapter = root.getChild(CHAPTER);
    	Element puzzle = chapter.getChild(PUZZLE);
    	Element hizzle = puzzle.getChild(HINT);
    	Element afterImage = chapter.getChild(AFTERIMAGE);
    	
    	root.getChild(TITLE).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
		    lp.setTitle(body);
    		}
    		
	    });
    	
    	root.getChild(CHAPTER).setStartElementListener(new StartElementListener(){
    		public void start(Attributes a) {
		    ch.reset();
		    Log.d(TAG, "Start Chapter");
		    ch.setTitle(a.getValue("title"));
		    Log.d(TAG,"found title");
		    Log.d(TAG, a.getValue("title"));
		    ch.beforeImageList.add(a.getValue("before_image"));
    		}
    	});
    	
    	
    	afterImage.getChild(IMAGE).setStartElementListener(new StartElementListener(){
    		public void start(Attributes a){
    			ch.afterImageList.add(a.getValue("image"));
    			Log.d(TAG, a.getValue("image"));
    		}
    	});
    	
    	chapter.getChild(HEIGHT).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
		    Log.d(TAG,"height "+body);
    			ch.setHeight(Integer.parseInt(body));
    		}
    	});
    	
    	chapter.getChild(WIDTH).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			Log.d(TAG,"width "+body);
    			ch.setWidth(Integer.parseInt(body));
    			
    		}
    	});
    	
    	chapter.getChild(PUZZLE).setStartElementListener(new StartElementListener(){
    		public void start(Attributes a){
    			puzz.reset();
    			puzz.setId(Long.parseLong(a.getValue(ID)));
    			puzz.setBeforeFlower(a.getValue("before_flower"));
    			puzz.setAfterFlower(a.getValue("after_flower"));
    			puzz.setText(a.getValue("text"));
    		}
    	});
    	
    	puzzle.getChild(WIDTH).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setWidth(Integer.parseInt(body));
    		}
    	});
    	
    	puzzle.getChild(HEIGHT).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setHeight(Integer.parseInt(body));
    		}
    	});
    	
    	puzzle.getChild(BOARD).setEndTextElementListener(new EndTextElementListener(){
    		public void end(String body){
    			puzz.setBoard(body);
    		}
    	});
    	
    	puzzle.getChild(PATH).setEndTextElementListener(new EndTextElementListener(){
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
    	
    	puzzle.getChild(HINT).setEndElementListener(new EndElementListener(){
    		public void end(){
    			puzz.addHint(hint.copy());
    		}
    		
    	});
    	
    	chapter.getChild(PUZZLE).setEndElementListener( new EndElementListener() {
    		public void end(){
    			puzz.setChapter(ch);
    			ch.addPuzzle(puzz.copy());
    		}
    	});
    	
    	root.getChild(CHAPTER).setEndElementListener(new EndElementListener(){
            public void end() {
                Log.d(TAG,"End Chapter");
                lp.addChapter(ch.copy());
            }
        });
    	
    	
    	
    	
    	try{
    		
    		Xml.parse(this.getInputStream(file), Xml.Encoding.UTF_8, 
    				root.getContentHandler());
    	} catch(Exception e) {
    		 throw new RuntimeException(e);
    	}
    	
    	return lp;
    }
}
