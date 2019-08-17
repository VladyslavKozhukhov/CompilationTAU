package TYPES;

import AST.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TYPE_CLASS extends TYPE {
    public static TYPE_CLASS currentClassFather = null;
    public static String currentClassName = "";
    public static Map<String, TYPE_CLASS> classes = new HashMap<>();

    public int classVarsNum;

    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE_CLASS father;


    /**************************************************/
    /* Gather up all data members in one place        */
    /* Note that data members coming from the AST are */
    /* packed together with the class methods         */
    /**************************************************/
    public TYPE_LIST data_members;


    //For IRME//
    public int fieldCnt;

    public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_LIST data_members) {
        this.name = name;
        this.father = father;
        this.data_members = data_members;
        this.fieldCnt = coutfield();
    }

    private int coutfield() {
        TYPE_LIST fields = data_members;
        int cnt = 0;
        while (fields != null) {
            cnt++;
            fields = fields.tail;
        }
        return cnt;
    }

    public String getTypeName() {
        return "TYPE_CLASS";
    }

    public boolean isClass() {
        return true;
    }

    public boolean hasParent(TYPE_CLASS parentClass) {
        TYPE_CLASS f = this.father;
        while (f != null) {
            if (f.name.equals(parentClass.name)) {
                return true;
            }
            f = f.father;
        }
        return false;
    }

    public boolean hasParentWithName(String parentClassName) {
        TYPE_CLASS f = this.father;
        while (f != null) {
            if (f.name.equals(parentClassName)) {
                return true;
            }
            f = f.father;
        }
        return false;
    }


    /*
        STATIC METHODS
     */
    public static boolean inClass() {
        return !currentClassName.isEmpty();
    }

    public static void addClass(String name, TYPE_CLASS t) {
        classes.put(name, t);
    }

    private static TYPE_CLASS findClass(String name) {
        return classes.get(name);
    }

    public static TYPE_CLASS getCurrentClass() {
        return classes.get(currentClassName);
    }

    //checks if name is a datamember being accessed by the class
    public TYPE findInAllDataMembersAvailableInClass(String name) {
        TYPE_CLASS searchedClass = this;
        TYPE found;
        do {
            found = findInDataMembers(searchedClass.data_members, name);
            if (found != null) {
                return found;
            }
            searchedClass = searchedClass.father;
        } while (searchedClass != null);
        return null;
    }

    /**
     * STATIC METHODS
     */
    public static TYPE findInCurrentClassOrParentsDataMembers(String name) {
        if (!inClass()) {
            return null;
        }
        TYPE t = null;
        TYPE_CLASS ct = findClass(currentClassName); //check if the name is of current class type
        if (ct != null) {
            t = ct.findInAllDataMembersAvailableInClass(name);
        }
        return t;
    }

    public static TYPE findInParentsDataMembersOfCurrentClass(String name) {
        if (currentClassFather == null) {
            return null;
        } else {
            TYPE_CLASS father = currentClassFather;
            TYPE found;
            while (father != null) {
                //check father data members
                found = findInDataMembers(father.data_members, name);
                if (found != null) {
                    return found;
                }
                father = father.father;
            }
        }
        return null;
    }

    //class data members are only var decs or func decs
    public static TYPE findInDataMembers(TYPE_LIST data_members, String name) {
        for (TYPE_LIST type = data_members; type != null; type = type.tail) {   //iterate over list
            if (type.head.isVarDec()) {
                if (((TYPE_CLASS_VAR_DEC) type.head).name.equals(name)) {
                    return type.head;
                }
            }
            if (type.head.isFunction()) {
                if (((TYPE_FUNCTION) type.head).name.equals(name)) {
                    return type.head;
                }
            }
        }
        return null;
    }
}
