package TYPES;

public class TYPE_PARAM extends TYPE {
    public TYPE type;
    public String name;

    public TYPE_PARAM(TYPE t, String name) {
        this.type = t;
        this.name = name;
    }

    public String getTypeName() {
        return "TYPE_PARAM";
    }

    public String getName() {
        return name;
    }

    public boolean isParam(){ return true;}

}
