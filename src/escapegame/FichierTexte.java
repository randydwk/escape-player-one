package escapegame;
import java.io.*;
public class FichierTexte extends Fichier{
    private File itsFile;
    private final static String EXTENSION = "txt";

    // Constructor
    public FichierTexte(String name, Folder parent, String path){
        this(name,parent);
        this.itsFile = new File(path);
    }

    public FichierTexte(String name, Folder parent){
        super(name, parent);
    }

    // Getters & Setters
    public String getName(){
        return super.getName()+"."+EXTENSION;
    }

    public File getItsFile(){
        return this.itsFile;
    }
    
    public void setItsFile(String path) {
    	this.itsFile = new File(path);
    }
    
    // Boolean
    public boolean isTexte() {
    	return true;
    }
}