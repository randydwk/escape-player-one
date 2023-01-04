package escapegame;
import java.util.Timer;
import java.util.TimerTask;

public class EscapeGame{
    public static void main(String[] args) throws Exception {
    	// Beginning
		Terminal.cmd_clear();
    	beginningText();

        String username;
        do {
        	username = Keyboard.readString("Please enter your name :\n");
        } while (username.toUpperCase().equals("LINK") || username.isEmpty() || username.indexOf(' ') != -1 || username.indexOf('/') != -1);
        Terminal.setUser(username);
        
        init_file_system();
        
        Terminal.printFile("/player/README");
        
        // Timer
 		TimerTask t = new TimerTask() {	
 			public void run()
 			{
 				try {
 					Terminal.setFocus(false);
 					gameOver();
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 				return;
 			}
 		};
 		Timer tim = new Timer();
 		long chrono = 45*60;
 		tim.schedule(t, chrono*1000);
 		Terminal.setChrono(chrono);
 		
        // Terminal
        boolean end = false;
        while (Terminal.hasFocus() && !Terminal.isEnd()){
        	Terminal.readCommand();
        }

        // End
        if (Terminal.isEnd()) {
        	tim.cancel();
        	endText();
        	Keyboard.readString("Press ENTER to exit.");
        }
    }

    public static void init_file_system(){
    	Folder tmp;
    	
        // Hiérarchie de fichiers de base
        Folder root = Terminal.getCurrentFolder();
        root.createFolder("game_data");
        tmp = root.getChildFolder("game_data");
	        tmp.createFolder("system");
			tmp = tmp.getChildFolder("system");
				tmp.createFichierTexte("compiler_CORRUPTED","system/compiler_CORRUPTED");
				tmp.createFichierTexte("LOSTFILE","system/LOSTFILE");
		tmp = root.getChildFolder("game_data");
        	tmp.createFichierTexte("trees_CORRUPTED","game_data/trees_CORRUPTED");
        	tmp.createFichierTexte("sword_CORRUPTED","game_data/sword_CORRUPTED");
        	tmp.createFichierTexte("shop_CORRUPTED","game_data/shop_CORRUPTED");
        	tmp.createFichierTexte("bomb_CORRUPTED","game_data/bomb_CORRUPTED");

        root.createFolder("menus");
        tmp = root.getChildFolder("menus");
        	tmp.createFichierTexte("main_menu_CORRUPTED","menus/main_menu_CORRUPTED");
        	tmp.createFichierTexte("options_menu_CORRUPTED","menus/options_menu_CORRUPTED");

        root.createFolder("players");
        tmp = root.getChildFolder("players");
            tmp.createFolder(Terminal.getUser());
            	tmp.getChildFolder(Terminal.getUser()).createFichierTexte("README","player/README");
            tmp.createFolder("link");
            tmp = tmp.getChildFolder("link");
            	tmp.createFichierTexte("link_CORRUPTED","link/link_CORRUPTED");
            	tmp.createFichierTexte("instruction0","link/instruction0");
            	
    	root.createFolder(".trash");
            
        // Positionner l'utilisateur dans le bon dossier
        Terminal.setCurrentFolder(root.getChildFolder("players").getChildFolder(Terminal.getUser()));
    }
    
    public static void beginningText() throws InterruptedException {
    	Terminal.prints(Terminal.C_WHITE+"Your game ran into a problem",1);
        Terminal.prints("ERROR <0x42 6f 6e 6a 6f 75 72 20 4d 6f 6e 73 69",0.3);
        Terminal.prints("65 75 72 20 54 69 6e 63 68 6f 6e 20 65 74 20 4d",0.3);
        Terminal.prints("61 64 61 6d 65 20 44 65 6c 69 6c 6c 65 20 21>",1.6);
        Terminal.prints("MISSING PLAYER",1);
        Terminal.prints("LOOKING FOR PLAYER ...",3);
        Terminal.prints("PLAYER FOUND SUCCESFULLY",1);
        Terminal.prints("Hello, player.",1);
    }
    
    public static void endText() throws Exception {
    	Terminal.prints("The game is repaired.",2);
    	Terminal.prints("Restarting in 3...",1);
	    Terminal.prints("Restarting in 2...",1);
	    Terminal.prints("Restarting in 1...",1);
	    Terminal.cmd_clear();
	    Terminal.sleep(1);
	    Terminal.prints("Starting 'The Legend of Zelda'...",1);
	    Terminal.prints("Loading menus...",2);
	    Terminal.prints("Succesfully loaded menus!",0.3);
	    Terminal.prints("Loading player...",2);
	    Terminal.prints("Incorrect number of players (2) : found 'link' and '"+Terminal.getUser()+"'",3);
	    Terminal.prints("Recovering players data...",2);
	    Terminal.prints("Player 'link' is working. Loading 'link'...",3);
	    Terminal.prints("Succesfully loaded 'link'!", 2);
	    Terminal.prints("Freeing player '"+Terminal.getUser()+"'...",3);
	    Terminal.prints("Player '"+Terminal.getUser()+"' is free!",2);
	    Terminal.prints("Loading map data...",1.2);
	    Terminal.prints("Succesfully loaded map data!",1);
	    Terminal.prints("Starting Game!",0.7);
	    Terminal.cmd_clear();
	    Terminal.printFile("link/END");
    }

	public static void gameOver() throws Exception {
		Terminal.prints("\nTIME IS UP, player.", 2);
		Terminal.prints("Restarting in 3...",1);
	    Terminal.prints("Restarting in 2...",1);
	    Terminal.prints("Restarting in 1...",1);
	    Terminal.cmd_clear();
	    Terminal.sleep(1);
		Terminal.prints("Starting 'The Legend of Zelda'...",1);
	    Terminal.prints("#loa#di*ng  menus...",2);
	    Terminal.prints("Menus load : FAI*L#ED",0.3);
		Terminal.prints("Loading ?play?er...",2);
		Terminal.prints("TH#E G@M£ I$ C~ORRUPTED ", 1);
		Terminal.prints("INCORRECT number of players (2) : found 'link' and '"+Terminal.getUser()+"'",3);
	    Terminal.prints("R€covering pl@y€r data$...",2);
	    Terminal.prints("Player '" + Terminal.getUser() + "' is w0rking ???!" + "Loading "+ Terminal.getUser() + "...",3);
	    Terminal.prints("Succ€sfully lo@ded " + Terminal.getUser() + " !%§",2);
	    Terminal.prints("Starting Game!",0.7);
	    Terminal.cmd_clear();
	    Terminal.printFile("link/GAME_OVER");
	}
}
