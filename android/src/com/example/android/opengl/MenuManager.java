package com.example.android.opengl;

class MenuManager {
	private GlobalState state;
	public GameMenu mGameMenu;
	public Model mModel;
	private Callback mCallback;

	public MenuManager(GlobalState s, Model m) {
		state = s;
		mModel = m;
		updateState();
	}
	//Each case statement should have a declaration of
	//a new menu.

	// Note that we could use closure here to declare funtions (callbacks)
	// Which have access to information model!  Cool!
	public void updateState() {
		float[] pos1 = {-.75f, 0f, 0f};
		float[] pos2 = {0f,-1f, 0f};
		float scale1 = .3f;
		float scale2  = .2f;
		
		switch (state.state) {
		case MAIN_MENU_OPENING:
			String[] textures1  = {};
			mGameMenu = new GameMenu(pos1, scale1, textures1, TextureManager.START); 
			mCallback = new Callback_MAIN_MENU_OPENING();
			break;
		case MAIN_MENU_LIST:
			String[] textures2 = {TextureManager.NEW, TextureManager.RESUME, TextureManager.OPTIONS, TextureManager.BACK};
			mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.CLEAR); 
			mCallback = new Callback_MAIN_MENU_LIST();
			break;
		case MAIN_MENU_NEW:
			String[] textures3 = {TextureManager.SHORT, TextureManager.MEDIUM, TextureManager.LONGER, TextureManager.LONGEST, TextureManager.BACK};
			mGameMenu = new GameMenu(pos1, scale1, textures3, TextureManager.NEW); 
			mCallback = new Callback_MAIN_MENU_NEW();
			break;
		case MAIN_MENU_OPTION:
			String[] textures4 = new String[1];
			if(state.hintsOn){
				textures4[0] = TextureManager.HINTS_ON;
			} else {
				textures4[0] = TextureManager.HINTS_OFF;
			}
			mGameMenu = new GameMenu(pos1, scale1, textures4, TextureManager.OPTIONS); 
			mCallback = new Callback_MAIN_MENU_OPTION();
			break;
		case GAME_OPENING:
			String[] textures5 = new String[0];
			mGameMenu = new GameMenu(pos2, scale2, textures5, TextureManager.CLEAR); 
			mCallback = new Callback_GAME_OPENING();
			break;
		case GAME_MENU_LIST:
			String[] textures6 = {TextureManager.CLEAR_BOARD, TextureManager.QUIT, TextureManager.BACK};
			mGameMenu = new GameMenu(pos2, scale2, textures6, TextureManager.CLEAR); 
			mCallback = new Callback_GAME_MENU_LIST();
			break;
		case GAME_MENU_END:
			String[] textures7 = {TextureManager.QUIT, TextureManager.SHARE};
			mGameMenu = new GameMenu(pos2, scale2, textures7, TextureManager.CLEAR); 
			mCallback = new Callback_GAME_MENU_END();
			break;
		}
	}

	public int touched(float[] pt) {
		return mGameMenu.touched(pt);
	}

	public void onTouched(int val) {
		mCallback.callback(val);
	}

	public void draw(MyGLRenderer r){
		mGameMenu.draw(r);
	}
	
	
	class Callback_MAIN_MENU_OPENING extends Callback {
		@Override
		public void callback(int val) {
			state.state = GameState.MAIN_MENU_LIST;
			updateState();
		}

	}

	class Callback_MAIN_MENU_LIST extends Callback {
		public void callback(int val) {
			switch(val) {
			
			case 1: state.state = GameState.MAIN_MENU_NEW;
					updateState();
					break;
			case 4: state.state = GameState.MAIN_MENU_OPENING;
					updateState();
					break;
			}
		}
	}

	class Callback_MAIN_MENU_NEW extends Callback{
		public void callback(int val) {
			switch(val) {
			case 1: mModel.createPuzzle(4,2);
			mModel.setState(GameState.GAME_OPENING);
			state.state = GameState.GAME_OPENING;
			updateState();
			break;
			case 2: mModel.createPuzzle(10,3);
			mModel.setState(GameState.GAME_OPENING);
			state.state = GameState.GAME_OPENING;
			updateState();
			break;
			case 3: mModel.createPuzzle(12,4);
			mModel.setState(GameState.GAME_OPENING);
			state.state = GameState.GAME_OPENING;
			updateState();
			break;
			case 4: mModel.createPuzzle(20,3);
			mModel.setState(GameState.GAME_OPENING);
			state.state = GameState.GAME_OPENING;
			updateState();
			break;
			case 5: state.state = GameState.MAIN_MENU_LIST;
			updateState();
			break;
			}
		}
	}

	class Callback_MAIN_MENU_OPTION extends Callback {

		@Override
		public void callback(int val) {
			// TODO Auto-generated method stub

		}

	}

	class Callback_GAME_OPENING extends Callback {

		@Override
		public void callback(int val) {
			// TODO Auto-generated method stub

		}

	}

	class Callback_GAME_MENU_LIST extends Callback {

		@Override
		public void callback(int val) {
			// TODO Auto-generated method stub

		}

	}

	class Callback_GAME_MENU_END extends Callback{

		@Override
		public void callback(int val) {
			// TODO Auto-generated method stub

		}

	}

}
