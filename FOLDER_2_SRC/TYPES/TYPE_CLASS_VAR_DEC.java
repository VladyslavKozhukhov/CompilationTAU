package TYPES;

public class TYPE_CLASS_VAR_DEC extends TYPE {
    public TYPE type;
    public String name;
//    public String typeName;

    public TYPE_CLASS_VAR_DEC(TYPE t, String name) {
        this.type = t;
        this.name = name;
//        typeName = type.getTypeName();
    }

    public String getTypeName() {
        return "TYPE_CLASS_VAR_DEC";
    }

    public String getName() {
        return name;
    }

    public boolean isVarDec(){ return true;}


//    public boolean isClass() {
//        return typeName.equals("TYPE_CLASS_VAR_DEC");
//    }
//
//    public boolean isArray() {
//        return typeName.equals("TYPE_ARRAY");
//    }
//
//    public boolean isString() {
//        return typeName.equals("TYPE_STRING");
//    }
//
//    public boolean isInt() {
//        return typeName.equals("TYPE_INT");
//    }
//
//    public boolean isNil() {
//        return typeName.equals("TYPE_NIL");
//    }

}


