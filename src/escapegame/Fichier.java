package escapegame;

public abstract class Fichier implements Comparable<Fichier>{
    private String name;
    private Folder parent;

    // Constructor
    public Fichier(String name, Folder parent){
        this(name);
        this.parent = parent;
    }

    public Fichier(String name){
        this.name = name;
    }

    // Getters & Setters
    public String getName(){
        return this.name;
    }

    public Folder getParent(){
        return this.parent;
    }

    public String getPath(){
        if (this.getParent() != null) {
            return this.getParent().getPath()+"/"+this.getName();
        } else {
            return "";
        }
    }

    public abstract boolean isTexte();
    
    public String toString(){
        return "File '"+this.name+"'\nPath "+this.getPath();
    }

	@Override
	public int compareTo(Fichier otherFichier) {
		int compare = this.name.compareTo(otherFichier.name);
		if (this.isTexte() && !otherFichier.isTexte()) {compare = 1;}
		return compare;
	}
}
