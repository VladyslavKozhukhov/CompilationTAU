package AST;

import Auxiliary.*;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

public class AST_VAR extends AST_NODE_RULE {
    public int caseNum;
    public String name;
    public AST_VAR var;
    public AST_EXP exp;

    TYPE t;//t is the type of the variable (int/string/class)
    TYPE v; // v.name t is the type of the name v is the type of the var
    TYPE_CLASS tc;
    int varIndex;
    VAR_KIND varKind;

    public AST_VAR(String name) {
        super();
        setNodeContent(String.format("name: %s", name));
        this.name = name;
        this.caseNum = 0;
    }

    public AST_VAR(AST_VAR v, String name) {
        super(v);
        setNodeContent(String.format("var.%s", name));
        this.var = v;
        this.name = name;
        this.caseNum = 1;
    }

    public AST_VAR(AST_VAR v, AST_EXP e) {
        super(v, e);
        setNodeContent("var[exp]");
        this.var = v;
        this.exp = e;
        this.caseNum = 2;
    }

    /* exp var */
    //simple
    public TYPE SemantMe() {
        switch (caseNum) {
            case 0:
                if (TYPE_CLASS.inClass()) {
                    t = SYMBOL_TABLE.getInstance().findUntilLevel(this.name, 1);
                    if (t == null) {
                        t = TYPE_CLASS.findInCurrentClassOrParentsDataMembers(this.name);
                    }
                }
                if (t == null) {
                    t = SYMBOL_TABLE.getInstance().find(this.name);
                }
                if (t == null) {
                    printErrorAndExit("AST VAR:  case 0 no var with name " + name + " defined");
                } else {
                    if (t.isVarDec()) {
                        varIndex = ((TYPE_CLASS_VAR_DEC) t).index;   //SAVE the index of the global/local/dataMember variable
                        varKind = ((TYPE_CLASS_VAR_DEC) t).kind;
                        t = ((TYPE_CLASS_VAR_DEC) t).type;
                    }
                    if (t.isParam()) {
                        varIndex = ((TYPE_PARAM) t).index;  //SAVE the index of the function parameter
                        varKind = ((TYPE_PARAM) t).kind;
                        t = ((TYPE_PARAM) t).type;
                    }
                }
                return t;
            case 1: //var.name var is a class and name is a func dec or a var dec inside class
                v = var.SemantMe();
                if (v == null || !v.isClass()) {
                    var.printErrorAndExit("AST VAR:  case 1 no class with name " + var.name + " defined");
                }
                tc = (TYPE_CLASS) v;
                if (tc != null) {
                    t = tc.findInAllDataMembersAvailableInClass(this.name);
                    if (t != null) {
                        if (t.isVarDec()) {
                            return ((TYPE_CLASS_VAR_DEC) t).type;
                        }
                    }
                    printErrorAndExit("AST VAR:  case 1 no var with name " + name + " defined");
                }
                break;
            case 2:
                t = exp.SemantMe();
                if (t == null || !t.isInt()) {
                    exp.printErrorAndExit("AST VAR: case 2 index is not int");
                }
                v = var.SemantMe();
                if (v == null || !v.isArray()) {
                    var.printErrorAndExit("AST VAR:  case 2 var is not array");
                }
                TYPE_ARRAY ta = (TYPE_ARRAY) v;
                if (ta != null) return ta.type;
                break;
        }
        printErrorAndExit("AST VAR:  error");
        return null;
    }

    public TEMP IRme() {
        switch (caseNum) {
            case 0:
                return IRme_SimpleVar();
            case 1:
                return IRme_VarField();
            case 2:
                return IRme_VarAtIndex();
        }
        return null; //should not reach here
    }


    private TEMP IRme_VarAtIndex() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP varTemp = this.var.IRme();
        TEMP indexTemp = this.exp.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Check_Invalid_Pointer(varTemp));
        IR.getInstance().Add_IRcommand(new IRcommand_Load_Var_At_Index(dst, varTemp, indexTemp));
        return dst;
    }

    private TEMP IRme_VarField() {
        varIndex = GlobalVariables.classNameAstClassD.get(tc.name).fieldNames.indexOf(name) + 1;
        TEMP fieldTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP varTemp = this.var.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Check_Invalid_Pointer(varTemp)); //TODO should check .
        IR.getInstance().Add_IRcommand(new IRcommand_Add_Offset(varTemp, varTemp, 4 * varIndex));
        IR.getInstance().Add_IRcommand(new IRcommand_Load(fieldTemp, varTemp));
        return fieldTemp;
    }

    private TEMP IRme_SimpleVar() {
        TEMP varTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (varKind == VAR_KIND.GLOBAL) {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Global_Var(name, varTemp));
        } else if (varKind == VAR_KIND.LOCAL) {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Local_Var(varIndex, varTemp));
        } else if (varKind == VAR_KIND.DATA_MEMBER) {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Class_Field(varIndex, varTemp));
        } else if (varKind == VAR_KIND.FUNC_PARAM) {
            IR.getInstance().Add_IRcommand(new IRcommand_Load_Param_Var(varIndex, varTemp));
        }
        return varTemp;
    }

    @Override
    public String getName() {
        return "VAR";
    }
}
