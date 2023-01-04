package escapegame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder extends Fichier{
    private ArrayList<Fichier> children = new ArrayList<Fichier>();

    // Constructor
    public Folder(String name, Folder parent){
        super(name,parent);
    }

    public Folder(String name){
        super(name);
    }

    // Getters & Setters
    public ArrayList<Fichier> getChildren(){
        return this.children;
    }

    public Fichier getChild(String name) {
    	for (Fichier f : this.getChildren()) {
    		if (f.getName().equals(name)) {
    			return f;
    		}
    	}
    	return null;
    }

    public int getChildIndex(String name){
        for (int i = 0; i < this.getChildren().size(); i++){
            Fichier f = this.getChildren().get(i);
            if (f.getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    public Folder getChildFolder(String name) {
    	for (Fichier f : this.getChildren()) {
    		if (f instanceof Folder && f.getName().equals(name)) {
    			return (Folder) f;
    		}
    	}
    	return null;
    }

    public int getChildIndexFolder(String name){
        for (int i = 0; i < this.getChildren().size(); i++){
            Fichier f = this.getChildren().get(i);
            if (f instanceof Folder && f.getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    public FichierTexte getChildFichierTexte(String name) {
    	for (Fichier f : this.getChildren()) {
    		if (f instanceof FichierTexte && f.getName().equals(name)) {
    			return (FichierTexte) f;
    		}
    	}
    	return null;
    }

    public int getChildIndexFichierTexte(String name){
        for (int i = 0; i < this.getChildren().size(); i++){
            Fichier f = this.getChildren().get(i);
            if (f instanceof FichierTexte && f.getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    // Boolean
    public boolean isChildFolder(String name){
        return (getChildIndexFolder(name) != -1);
    }

    public boolean isChildFichierTexte(String name){
        return (getChildIndexFichierTexte(name) != -1);
    }
    
    public boolean isTexte() {
    	return false;
    }

    // Methods
	public boolean createFolder(String name){
        boolean created = false;
        if (!isChildFolder(name)){
            Folder f = new Folder(name,this);
            this.getChildren().add(f);
            created = true;
        }
        return created;
    }

    public void createFichierTexte(String name, String path){
        if (!this.isChildFichierTexte(name+".txt")){
            FichierTexte f = new FichierTexte(name,this,path);
            this.getChildren().add(f);
        } else {
        	this.getChildFichierTexte(name+".txt").setItsFile(path);
        }
    }
	
    public boolean deleteFichier(String name){
        boolean deleted = false;
        int i = getChildIndex(name);
        if (i != -1){
            this.getChildren().remove(i);
            deleted = true;
        }
        return deleted;
    }

    // toString
    public String getChildrenToString(){
        String res = "";
        for (int i = 0; i < this.getChildren().size(); i++){
            if (!this.getChildren().get(i).isTexte()) {res+=Terminal.C_BLUE;}
            res += this.getChildren().get(i).getName()+"  ";
            res+=Terminal.C_WHITE;
        }
        if (res.isEmpty()) res = "Folder is empty";
        return res;
    }

    public String toString(){
        String children = "";
        for (int i = 0; i < this.children.size(); i++){
            children += "'"+this.children.get(i).getName()+"'";
            if (i != this.children.size()-1) children += ", ";
        }
        if (children.isEmpty()) children = "nothing";
        return super.toString()+"\nContains "+children;
    }
}
