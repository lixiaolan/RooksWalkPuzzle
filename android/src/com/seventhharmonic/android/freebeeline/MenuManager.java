package com.seventhharmonic.android.freebeeline;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.seventhharmonic.android.freebeeline.TableOfContents.Contents;
import android.app.Activity;

class MenuManager {
	private GlobalState state;
	public GameMenu mGameMenu;
	public Model mModel;
	private Callback mCallback;
	private float[] geometry = new float[3];
	private float[] bottomPos1 = new float[3];
	private float[] bottomPos2 = new float[3];
	private float[] bottomRight = new float[3];
	private float scale1;
	private float scale2;
	EasyTracker mTracker;

	public MenuManager(GlobalState s, Model m) {
		state = s;
		mModel = m;
		updateState();
		mTracker = GlobalApplication.getGaTracker();

	}
	//Each case statement should have a declaration of
	//a new menu.

	public void callCallback(int val) {
		mCallback.callback(val);
	}


	// Note that we could use closure here to declare funtions (callbacks)
	// Which have access to information model!  Cool!
	public void updateState() {
		float[] pos1 = {-.75f, 0f, 0f};
		scale1 = .25f;
		scale2  = .2f;

		float tiltAngle = -1.0f*(float)Math.PI/2;

		switch (state.state) {
		case MAIN_MENU_OPENING:
			String[] textures1  = {};
			mGameMenu = new GameMenu(pos1, scale1, textures1, TextureManager.START);
			mCallback = new Callback_MAIN_MENU_OPENING();
			break;

		case MAIN_MENU_GEAR:
			String[] texturesGear = {TextureManager.OPTIONS, TextureManager.TUTORIAL,  TextureManager.STORY, TextureManager.STATS};
			mGameMenu = new GameMenu(pos1,scale1, texturesGear, TextureManager.BACK); 
			mCallback = new Callback_MAIN_MENU_GEAR();
			break;
		case MAIN_MENU_LIST:
			if(state.resumeGameExists){
				String[] textures2 = {TextureManager.RESUME, TextureManager.MENU, TextureManager.TABLE_OF_CONTENTS};
				mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
				mCallback = new Callback_MAIN_MENU_LIST_RESUME();
			} else {
				String[] textures2 = {TextureManager.MENU, TextureManager.TABLE_OF_CONTENTS};
				mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
				mCallback = new Callback_MAIN_MENU_LIST_NORESUME();
			}
			break;
		case MAIN_MENU_OPTIONS:
			String[] textures4 = new String[2];

			if(state.linesOn){
				textures4[0] = TextureManager.LINES_ON;
			} else {
				textures4[0] = TextureManager.LINES_OFF;
			}
			if(state.ruleCheck){
				textures4[1] = TextureManager.RULE_CHECK_ON;
			} else {
				textures4[1] = TextureManager.RULE_CHECK_OFF;
			}
			mGameMenu = new GameMenu(pos1, scale1, textures4, TextureManager.BACK); 
			mCallback = new Callback_MAIN_MENU_OPTIONS();
			break;
		case GAME_OPENING:
			String[] textures5 = {};
			mGameMenu = new GameMenu(bottomRight, scale2, textures5, TextureManager.BACK, tiltAngle); 
			mCallback = new Callback_GAME_OPENING();
			break;
		case GAME_MENU_END:
			String[] textures7 = {TextureManager.QUIT, TextureManager.SHARE};
			mGameMenu = new SelectTwoMenu(bottomPos2, scale2, textures7); 
			mCallback = new Callback_GAME_MENU_END();
			break;
		case TUTORIAL:
			String[] texturesTUTORIAL = {TextureManager.QUIT};
			//String[] textures8 = {TextureManager.NEXT, TextureManager.PREVIOUS};
			mGameMenu = new SelectOneMenu(bottomPos2, scale2, texturesTUTORIAL); 
			mCallback = new Callback_TUTORIAL();
			break;
			
		case TABLE_OF_CONTENTS:
			String[] texturesTOC = {TextureManager.BACK};
			mGameMenu = new SelectOneMenu(bottomRight, scale2, texturesTOC); 
			mCallback = new Callback_TABLE_OF_CONTENTS();
			break;
		case STATS:
			String[] textures9 = {TextureManager.QUIT};
			mGameMenu = new SelectOneMenu(bottomPos2, scale2, textures9); 
			mCallback = new Callback_STATS();
			break;
		case STORY:
			String[] textures10 = {TextureManager.QUIT};
			mGameMenu = new SelectOneMenu(bottomPos2, scale2, textures10); 
			mCallback = new Callback_STORY();
			break;
		}
	}

	public int touched(float[] pt) {
		return mGameMenu.touched(pt);
	}

	public void touchHandler(float[] pt) {
		int val = touched(pt);
		if(val != -1){
			mCallback.callback(val);
		}
	}

	public void draw(MyGLRenderer r){
		mGameMenu.draw(r);
	}

	public void setGeometry(float[] g) {
		geometry = g;

		bottomPos1[0] = 0.0f;
		bottomPos1[1] = -geometry[1]+scale1;
		bottomPos1[2] = 0.0f;

		bottomPos2[0] = 0.0f;
		bottomPos2[1] = -geometry[1]+scale2;
		bottomPos2[2] = 0.0f;
	
		bottomRight[0] = -1*(geometry[0]-scale2);
		bottomRight[1] = -geometry[1]+scale2;
		bottomRight[2] = 0.0f;
	}

	class Callback_MAIN_MENU_OPENING extends Callback {
		@Override
		public void callback(int val) {
			state.state = GameState.MAIN_MENU_LIST;
			updateState();
		}	
	}

	class Callback_MAIN_MENU_GEAR extends Callback {
		@Override
		public void callback(int val) {
			switch(val) {
			case 1: state.state = GameState.MAIN_MENU_OPTIONS;
			updateState();
			break;
			case 2: 
				mModel.createTutorial();
				mModel.setState(GameState.TUTORIAL);
				state.state = GameState.TUTORIAL;
				mTracker.send(MapBuilder
					    .createAppView()
					    .set(Fields.SCREEN_NAME,"tutorial_screen")
					    .build()
					);
				updateState();
				break;
			case 3:
				mModel.createStory();
				mModel.setState(GameState.STORY);
				mTracker.send(MapBuilder
					    .createAppView()
					    .set(Fields.SCREEN_NAME,"story_screen")
					    .build()
					);
				updateState();
				break;
			case 4:
				//mModel.updateStats();
				mModel.createTextures = true;
				state.state = GameState.STATS;
				mModel.setState(GameState.STATS);
				mTracker.send(MapBuilder
					    .createAppView()
					    .set(Fields.SCREEN_NAME,"stats_screen")
					    .build()
					);
				updateState();
				break;
			case 0: state.state = GameState.MAIN_MENU_OPENING;
			updateState();
			break;

			}

		}
	}

	class Callback_MAIN_MENU_LIST_RESUME extends Callback {
		public void callback(int val) {
			switch(val) {
			case 1: 
				state.saveCurrGame = true;
				mModel.resumeGame();
				mModel.setState(GameState.GAME_OPENING);
				mTracker.send(MapBuilder
						.createEvent("ui_action",     
								"button_press",  
								"resume_button",   
								null)            
								.build()
						);
				updateState();
				break;
			case 2: state.state = GameState.MAIN_MENU_GEAR;
			updateState();
			break;
			case 3:
				mModel.setState(GameState.TABLE_OF_CONTENTS);
				updateState();
				break;
			case 0: state.state = GameState.MAIN_MENU_OPENING;
			updateState();
			break;
			}
		}
	}

	class Callback_MAIN_MENU_LIST_NORESUME extends Callback {
		public void callback(int val) {
			switch(val) {
			/*if(state.firstRun){
			mModel.createTutorial();
			mModel.setState(GameState.TUTORIAL);
			state.state = GameState.TUTORIAL;
			state.firstRun = false;
			} else{
				state.state = GameState.MAIN_MENU_NEW;
			}
			updateState();
			break;*/ 
			case 1: state.state = GameState.MAIN_MENU_GEAR;
			updateState();
			break;
			case 2:
				mModel.setState(GameState.TABLE_OF_CONTENTS);
				updateState();
				break;

			case 0: state.state = GameState.MAIN_MENU_OPENING;
			updateState();
			break;
			}
		}
	}


	class Callback_MAIN_MENU_OPTIONS extends Callback {

		@Override
		public void callback(int val) {
			switch(val) {

			case 1: state.linesOn = !state.linesOn;
			if (state.linesOn)
				mGameMenu.setTexture(0,TextureManager.LINES_ON);
			else
				mGameMenu.setTexture(0,TextureManager.LINES_OFF);
			mModel.toggleLines(state.linesOn);
			break;
			case 2:
				state.ruleCheck = !state.ruleCheck;
				if (state.ruleCheck)
					mGameMenu.setTexture(1,TextureManager.RULE_CHECK_ON);
				else
					mGameMenu.setTexture(1,TextureManager.RULE_CHECK_OFF);
				mModel.toggleRules(state.ruleCheck);
				break;
			case 0: state.state = GameState.MAIN_MENU_LIST;
			updateState();
			break;
			}
		}

	}

	class Callback_GAME_OPENING extends Callback {

		@Override
		public void callback(int val) {
			mModel.setState(GameState.TABLE_OF_CONTENTS);
			updateState();
		}

	}


	class Callback_GAME_MENU_END extends Callback{

		@Override
		public void callback(int val) {
			switch(val) {
			case 1: 
			mModel.setState(GameState.TABLE_OF_CONTENTS);
			updateState();
			break;
			/*case 2: 
				ShareHelper sh = new ShareHelper((Activity)mModel.context, "1","2","3","4","5");
				sh.share();
				break;
			*/
			}

		}

	}

	class Callback_TUTORIAL extends Callback {

		@Override
		public void callback(int val) {
			switch(val) {
			case 1:
				state.state = GameState.MAIN_MENU_OPENING;
				mModel.setState(GameState.MAIN_MENU_OPENING);
				updateState();
				break;
			}
		}
	}

	class Callback_TABLE_OF_CONTENTS extends Callback {

		@Override
		public void callback(int val) {
			switch(val) {
			case 1:
				System.out.println("In Menu");
				System.out.println(mModel.toc.mContents);
				if(mModel.toc.mContents.equals(TableOfContents.Contents.LEVELPACKDISPLAY)){
					/*Comment: TOC already has a reference to Model 
					 * - it could just do these state changes itself
					 * That would be less awkward then this shit.
					*/
					mModel.setState(GameState.MAIN_MENU_OPENING);
					updateState();
				}
				else {	
					mModel.toc.setState();
				}
				break;
			}
		}
	}

	
	
	
	class Callback_STATS extends Callback { 	
		@Override
		public void callback(int val) {

			switch(val) {
			case 1:
				state.state = GameState.MAIN_MENU_OPENING;
				mModel.setState(GameState.MAIN_MENU_OPENING);
				updateState();
				break;
			}
		}
	}

	class Callback_STORY extends Callback { 	
		@Override
		public void callback(int val) {

			switch(val) {
			case 1:
				state.state = GameState.MAIN_MENU_OPENING;
				mModel.setState(GameState.MAIN_MENU_OPENING);
				updateState();
				break;
			}
		}
	}

}
