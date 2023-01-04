package escapegame;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Terminal{
    // Colors
    public static final String C_RESET = "\u001B[0m";
    public static final String C_GREEN = "\u001B[92m";
    public static final String C_BLUE = "\u001B[94m";
    public static final String C_WHITE = "\u001B[97m";
    public static final String C_RED_BG = "\u001B[41m";
    public static final String C_GREEN_BG = "\u001B[42m";
	// Timer
	private static Date timerStart = new Date();
	private static long chrono = 0;
	// Focus
	private static boolean focus = true;
	private static boolean end = false;
	// User and computer names
	private static String user = "player1";
	private final static String MACHINE = "game";
	private static final String PLAYER_FOLDER = "/players/";
	// Commands
	private static ArrayList<String> args = new ArrayList<String>();
	// File System
	private static final Folder root = new Folder("/");
	private static Folder currentFolder = root;
	// Man Pages
	private final static ArrayList<String> manPages = new ArrayList<String>(Arrays.asList("ascii","binary","cat","cd","echo","hex","java","ls","man","mkdir","rm","timer","whoami"));
	
	//---------//
	// Reading //
	//---------//
	
	public static void readCommand() throws Exception{
		// Get Path, edit with '/' if it's the root or '~' if it's the user directory 
		String path = replaceCurrentFolderPath(currentFolder.getPath());

		// Get Command
		String cmd = Keyboard.readString(C_GREEN+getUser()+"@"+getMachine()+C_WHITE+":"+C_BLUE+path+C_WHITE+"$ ");
		if (cmd.length() > 0 && cmd.charAt(0) == '§') {
			executeCommand(cmd.substring(1),true);
		} else {
			executeCommand(cmd,false);
		}
	}

	private static String replaceCurrentFolderPath(String path) {
		if (path.isEmpty()) path = "/";

		int l = PLAYER_FOLDER.length()+getUser().length()+1;
		if (path.equals(PLAYER_FOLDER+getUser())){
			path = "~";
		} else if (path.length() >= l && path.substring(0,l).equals(PLAYER_FOLDER+getUser()+"/")){
			path = "~/"+path.substring(l,path.length());
		}
		return path;
	}
	
	public static void executeCommand(String cmd, boolean asAdmin) throws Exception {
		stringSplit(cmd,args,' ');
		if (checkArg(0)){
			switch (args.get(0)){
				// Navigation
				case "ls":			cmd_ls();		break;
				case "cd":			cmd_cd();		break;
				// Folders
				case "mkdir":		if(asAdmin) cmd_mkdir(); 	else notAdmin(); break;
				case "rm":			if(asAdmin) cmd_rm(); 		else notAdmin(); break;
				// Files
				case "man":			cmd_man();		break;
				case "cat":			cmd_cat();		break;
				case "catout":		if(asAdmin) cmd_catout();   else invalid();  break;
				case "mkres":		if(asAdmin) cmd_mkres();    else invalid();  break;
				// Miscellaneous
				case "echo":		cmd_echo();		break;
				case "whoami":		cmd_whoami();	break;
				case "clear":		cmd_clear();	break;
				case "timer":		cmd_timer();	break;
				case "sleep":		if(asAdmin) cmd_sleep();	else notAdmin(); break;
				case "leavegame":	if(asAdmin) setEnd(true);   else invalid();  break;
				default: invalid(); break;
			}
		}
	}
	
	public static void invalid(){
	    print("Invalid Command, try 'man' for help");
	}
	
	public static void notAdmin() {
		print(getUser()+" is not allowed to run this command.");
	}

	//----------//
	// Commands //
	//----------//

	// Navigation
	public static void cmd_ls(){
		Collections.sort((List<Fichier>) currentFolder.getChildren());
		print(currentFolder.getChildrenToString());
	}

	public static void cmd_cd(){
		if (checkArg(1)){
			currentFolder = getFolder(args.get(1),"cd");
		} else {
			currentFolder = root.getChildFolder("players").getChildFolder(Terminal.getUser());
		}
	}
		
	// Folders
	public static void cmd_mkdir(){
		if (checkArg(1)){
			Folder tmp = currentFolder;
			if (checkArg(2)) tmp = getFolder(args.get(2),"mkdir");
			boolean ok = tmp.createFolder(args.get(1));
			if (!ok) print("mkdir: cannot create directory ‘"+args.get(1)+"’: File exists");
		} else {
			print("mkdir: missing operand");
		}
	}

	public static void cmd_rm() {
		if (checkArg(1)){
			String type = "";
			if (currentFolder.isChildFolder(args.get(1))){
				type = "folder";
			} else if (currentFolder.isChildFichierTexte(args.get(1))){
				type = "file";
			}
			
			if (!type.isEmpty()){
				boolean verif = false;
				String ans = "";
				if (!checkArg(2,"-r")) {
					verif = true;
					ans = Keyboard.readString("rm: remove "+type+" '"+args.get(1)+"'? ");
				}
				if (ans.equals("y") || !verif) currentFolder.deleteFichier(args.get(1));
			} else {
				print("No folder/file named "+args.get(1));
			}
		} else {
			print("rm: missing operand");
		}
	}

	// Files
	public static void cmd_man() throws Exception {
		if (checkArg(1)){
			if (manPages.contains(args.get(1))) printFile("man_pages/"+args.get(1));
			else print("No manual entry for "+args.get(1));
		} else {
			printFile("man_pages/man_what");
		}
		
	}

	public static void cmd_cat() throws Exception{
		// Copy Arguments
		ArrayList<String> cat_list = new ArrayList<String>();
		if (checkArg(1,"*")) {
			Collections.sort((List<Fichier>) currentFolder.getChildren());
	        for (int i = 0; i < currentFolder.getChildren().size(); i++){
	        	cat_list.add(currentFolder.getChildren().get(i).getName());
	        }
		} else {
			for (int s = 1; s < args.size(); s++) {
				cat_list.add(args.get(s));
			}
		}
		int i = 0;
		while (i < cat_list.size()) {
			String name = cat_list.get(i);
			if (currentFolder.isChildFichierTexte(name)){
				printFile(currentFolder.getChildFichierTexte(name).getItsFile().getPath());
			} else if (currentFolder.isChildFolder(name)){
				print("cat: "+name+": Is a directory");
			} else {
				print("cat: "+name+": No such file or directory");
			}
			i++;
		}
		if (i == 0) print("cat: missing operand");
	}
	
	public static void cmd_catout(){
		if (checkArg(1)) {
			try{
				printFile(args.get(1));
			} catch (Exception e) {
				print("catout: "+args.get(1)+": No such file or directory");
			}
		} else {
			print("catout: missing operand");
		}
	}
	
	public static void cmd_mkres() throws Exception {
		if (checkArg(3)) {
			getFolder(args.get(2),"mkres").createFichierTexte(args.get(3),args.get(1));
		} else {
			print("mkres: missing operand");
		}
	}
	
	// Miscellaneous
	public static void cmd_echo() {
		int i = 1;
		StringBuilder str_args = new StringBuilder();
		while (checkArg(i)) {
			str_args.append(args.get(i));
			str_args.append(' ');
			i++;
		}
		print(str_args.toString());
	}
	
	public static void cmd_whoami() {
		print(getUser());
	}
	
	public static void cmd_clear() {
		System.out.print("\033[H\033[2J");
	}
	
	public static void cmd_timer() {
		Date timer = new Date();
		long seconds = chrono-((timer.getTime()-timerStart.getTime())/1000);
		String min = ""+(int) (seconds/60);
		if ((seconds/60) < 10){
		    min = C_RED_BG+"0"+min;
		} else {
		    min = C_GREEN_BG+min;
		}
		String sec = ""+seconds%60;
		if ((seconds%60) < 10) sec = "0"+sec;
		print("Time left: "+min+"m "+sec+"s"+C_RESET);
	}
	
	public static void cmd_sleep() throws InterruptedException {
		if (checkArg(1)) {
			try {
				sleep(Double.parseDouble(args.get(1)));
			} catch (IllegalArgumentException iae) {
				print("Format is not correct : expected positive double, got \""+args.get(1)+"\".");
			}
		}
	}

	//-------------------//
	// Getters & Setters //
	//-------------------//
	
	public static void setChrono(long duration) {
		chrono = duration;
		timerStart = new Date();
	}
	
	public static Folder getCurrentFolder() {
		return currentFolder;
	}
	
	public static Folder getFolder(String path, String cmd) {
		Folder dest = currentFolder;
		ArrayList<String> path_el = new ArrayList<String>();
		if (path.charAt(0) == '/') {
			dest = root;
			stringSplit(path.substring(1,path.length()),path_el,'/');
		} else {
			stringSplit(path,path_el,'/');
		}
		
		boolean ok = true;
		int i = 0;
		while (ok && i < path_el.size()) {
			if (dest.isChildFolder(path_el.get(i))){
				dest = dest.getChildFolder(path_el.get(i));
			} else if (path_el.get(i).equals("..")){
				if (dest.getParent() != null) dest = dest.getParent();
			} else if (!path_el.get(i).equals(".")){
				print("bash: "+cmd+": "+args.get(1)+": No such file or directory");
				ok = false;
			}
			i++;
		}
		if (!ok) dest = currentFolder;
		return dest;
	}

	public static boolean hasFocus() {
		return focus;
	}
	
	public static boolean isEnd() {
		return end;
	}
	
	public static void setEnd(boolean e) {
		end = e;
	}
	
	public static void setFocus(boolean hasFocus) {
		focus = hasFocus;
	}
	
	public static String getUser() {
		return user;
	}

	public static String getMachine(){
		return MACHINE;
	}

	public static void setCurrentFolder(Folder newCurrentFolder) {
		Terminal.currentFolder = newCurrentFolder;
	}

	public static void setUser(String user){
		Terminal.user = user;
	}
	
	//-------//
	// Files //
	//-------//
	
	public static void printFile(String path) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("res/"+path));
		String line;
		
		while ((line = reader.readLine()) != null){
			if (!line.isEmpty() && line.charAt(0) == '$') {
				// Get Enigm
				ArrayList<String> enigm = new ArrayList<String>();
				stringSplit(line,enigm,'$');
				// Answer
				ArrayList<String> answers = new ArrayList<String>();
				stringSplit(enigm.get(1),answers,'|');
				if (!enigm.get(0).equals("execute")){
					boolean ok;
					do {
						ok = true;
						String ans = Keyboard.readString(enigm.get(0)+" ");
						if (answers.contains(ans)){
							cmd_clear();
							int j = 2;
							while (j < enigm.size()) {
								executeCommand(enigm.get(j),true);
								j++;
							}
						} else if (!ans.equals("exit")) {
							print("Wrong answer. Type 'exit' to try later.");
							ok = false;
						}
					} while (!ok && hasFocus());
				} else {
					int j = 1;
					while (j < enigm.size()) {
						executeCommand(enigm.get(j),true);
						j++;
					}
				}
			} else {
				print(line);
			}
		}
		reader.close();
	}

	//------------------------//
	// Command interpretation //
	//------------------------//
	
	private static void stringSplit(String src, ArrayList<String> dest, char sep){
		dest.clear();

		String tmp = "";
		for (int i = 0; i < src.length(); i++){
			if (src.charAt(i) == sep){
				if (!tmp.isEmpty()){
					dest.add(tmp);
					tmp = "";
				}
			} else {
				tmp += src.charAt(i);
			}
		}
		if (!tmp.isEmpty()) dest.add(tmp);
	}

	private static boolean checkArg(int index){
		return (index < args.size());
	}

	private static boolean checkArg(int index, String c){
		return (checkArg(index) && args.get(index).equals(c));
	}

	// Affichage
	public static void print(String str){
		System.out.println(str);
	}
	
	public static void sleep(double seconds) throws InterruptedException {
    	Thread.sleep((long) (seconds*1000));
    }
	
	public static void prints(String str, double seconds) throws InterruptedException {
		print(str);
		sleep(seconds);
	}
}
