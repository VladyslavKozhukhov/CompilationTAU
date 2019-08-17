package TYPES;

import java.util.HashMap;
import java.util.Map;

public class TYPE_FUNCTION extends TYPE {
    /***********************************/
    /* The return type of the function */
    /***********************************/
    public TYPE returnType;

    public static String currentFuncName = "";


    /*************************/
    /* types of input params */
    /*************************/
    public TYPE_LIST params;
    public String label;
    public String endLabel;
    public int paramCnt = 0;
    public String cls;//classdec name


    /****************/
    /* CTROR(S) ... */

    /****************/
    public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params, int paramCnt) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.paramCnt = paramCnt;
    }

    public TYPE_FUNCTION(TYPE returnType, String name, TYPE_LIST params) {
        this.name = name;
        this.returnType = returnType;
        this.params = params;
    }

    //check if a function is a declaration of another (parameters of input function can be extension of the parameters for this function)
    public boolean isDeclarationOf(TYPE_FUNCTION tf) {
        return this.name.equals(tf.name) && this.equalParameters(tf.params);
    }

    private boolean equalParameters(TYPE_LIST params) {
        for (TYPE_LIST p1 = this.params, p2 = params; p1 != null || p2 != null; p1 = p1.tail, p2 = p2.tail) {   //iterate over list
            if ((p1 == null || p2 == null)) {
                return false;
            } //not equal number of parameters
            if (p1.head.name.equals(p2.head.name) && p1.getType().equals(p2.getType())) {
                continue;
            }
            if ((p1.head.isClass() || p1.head.isArray()) && p2.head.isNil()) {
                continue;
            }
            if (p1.head.isClass() && p2.head.isClass()) {
                TYPE_CLASS t1 = (TYPE_CLASS) p1.head;
                TYPE_CLASS t2 = (TYPE_CLASS) p2.head;
                if (t2.hasParent(t1) || t1.name.equals(t2.name)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    public boolean equals(TYPE_FUNCTION f) {
        return this.name.equals(f.name)
                && this.returnType.name.equals(f.returnType.name)
                && this.equalParameters(f.params);
    }

    public boolean isFunction() {
        return true;
    }

    public String getTypeName() {
        return "TYPE_FUNCTION";
    }

    public static boolean inFunc() {
        return !currentFuncName.isEmpty();
    }

}
