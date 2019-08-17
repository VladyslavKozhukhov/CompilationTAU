package AST;

import Auxiliary.GlobalVariables;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

import java.util.ArrayList;
import java.util.Collections;

public class AST_NEW_EXP extends AST_NODE_RULE {

    public String name;
    public AST_EXP exp;
    public int caseNum;
    public String vTableLabel;

    TYPE t;

    public AST_NEW_EXP(String name) {
        super();
        setNodeContent(String.format("new %s", name));
        this.name = name;
        this.caseNum = 0;
    }

    public AST_NEW_EXP(String name, AST_EXP e) {
        super(e);
        setNodeContent(String.format("new %s[exp]", name));
        this.name = name;
        this.exp = e;
        this.caseNum = 1;
    }

    public TYPE SemantMe() {
        if (caseNum == 0) {
            if (name.equals(TYPE_CLASS.currentClassName)) {
                t = TYPE_CLASS.getCurrentClass();
                return t;
            }
            t = SYMBOL_TABLE.getInstance().find(name);
            if (t != null && t.isClass()) {
                return t;
            }
            printErrorAndExit("NEW EXP: no type such as " + name + " found");
        } else {
            t = SYMBOL_TABLE.getInstance().find(name); //this name must be a array
            if (t != null && t.name.equals(name) && exp.SemantMe().isInt()) {
                return new TYPE_ARRAY(t, null);
            }
            printErrorAndExit("NEW EXP: no type such as " + name + " found");
        }
        return null;
    }


    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (caseNum == 0) {
            TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
            TYPE_CLASS clssType = (TYPE_CLASS) t;
            AST_CLASSD classD = GlobalVariables.classNameAstClassD.get(clssType.name);
            AST_CLASSD father = GlobalVariables.classNameAstClassD.get(classD.extendClass);
            ArrayList<String> fieldNamesTMP;
            ArrayList<AST_EXP> expListTMP;
            expListTMP = classD.fieldExps;
            fieldNamesTMP = classD.fieldNames;
            while (father != null) {
                fieldNamesTMP.addAll(0, father.fieldNames);
                expListTMP.addAll(0, father.fieldExps);
                father = GlobalVariables.classNameAstClassD.get(father.extendClass);

            }
            int size = fieldNamesTMP.size();
            IR.getInstance().Add_IRcommand(new IRcommand_Allocate_Class(t1, size + 1)); //+1 for vtable
            IR.getInstance().Add_IRcommand(new IRcommand_Move(dst,t1));
            TEMP tmp3 = TEMP_FACTORY.getInstance().getFreshTEMP();
            vTableLabel = classD.vTable;
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Address(tmp3, vTableLabel));
            IR.getInstance().Add_IRcommand(new IRcommand_Store(t1, tmp3));
            for (int i = 0; i < size; i++) {
                TEMP t2;
                AST_EXP exp = expListTMP.get(i);
                if (exp != null) {
                    t2 = exp.IRme();
                } else {
                    t2 = TEMP_FACTORY.getInstance().getFreshTEMP();
                    IR.getInstance().Add_IRcommand(new IRcommand_Set_Zero(t2));
                }
                IR.getInstance().Add_IRcommand(new IRcommand_Add_Offset(t1, t1, 4));
                IR.getInstance().Add_IRcommand(new IRcommand_Store(t1, t2));
            }
            return dst;
        } else {
            TEMP arraySize = exp.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Allocate_Array(dst, arraySize));
            return dst;
        }
    }


    @Override
    public String getName() {
        return "NEW EXP";
    }
}
