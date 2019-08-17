package AST;

import TYPES.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;

public class AST_ARRAYD extends AST_NODE_RULE {

    public String arrayName;
    public String arrayType;

    public AST_ARRAYD(String arrayName, String arrayType) {
        super();
        setNodeContent(String.format("array name: %s\narray type: %s", arrayName, arrayType));
        this.arrayName = arrayName;
        this.arrayType = arrayType;
    }

    public TYPE SemantMe() {

        TYPE t;
        if( SYMBOL_TABLE.getInstance().find(SYMBOL_TABLE.getInstance().SCOPE_BOUNDARY) != null){
            printErrorAndExit("ARRAY DEC: not in global scope");
        }

        t = SYMBOL_TABLE.getInstance().find(arrayType);
        if (t == null || !arrayType.equals(t.name) || !(t.isInt() || t.isString() || t.isClass() || t.isArray()))
        {
            printErrorAndExit("ARRAY DEC: array type "+arrayType+" problem");
        }
        if (SYMBOL_TABLE.getInstance().find(arrayName) != null)
        {
            printErrorAndExit("ARRAY DEC: array name: "+arrayName+" is already used");
        }
        SYMBOL_TABLE.getInstance().enter(arrayName, new TYPE_ARRAY(t,arrayName));
        return null;
    }


    @Override
    public String getName() {
        return "ARRAY DEC";
    }
}
