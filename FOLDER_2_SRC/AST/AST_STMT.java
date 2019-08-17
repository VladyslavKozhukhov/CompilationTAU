package AST;

import Auxiliary.GlobalVariables;
import Auxiliary.VAR_KIND;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;


public class AST_STMT extends AST_NODE_RULE {

    int caseNum;
    String stmtType;
    AST_EXP cond;
    AST_STMT_LIST body;
    AST_FUNCTION_CALL f;
    AST_VARD vd;
    AST_VAR v;
    AST_EXP e;
    AST_NEW_EXP ne;

    public AST_STMT(AST_VARD vd) {
        super(vd);
        setNodeContent("var dec");
        caseNum = 0;
        this.vd = vd;
    }

    public AST_STMT(AST_VAR v, AST_EXP e) {
        super(v, e);
        setNodeContent("assign exp (left := right)");
        caseNum = 1;
        this.v = v;
        this.e = e;
    }


    public AST_STMT(AST_VAR v, AST_NEW_EXP ne) {
        super(v, ne);
        setNodeContent("assign new exp (left := right)");
        caseNum = 2;
        this.v = v;
        this.ne = ne;
    }

    public AST_STMT(AST_EXP e) {
        super(e);
        setNodeContent("return exp");
        caseNum = 3;
        this.e = e;
    }


    public AST_STMT() {
        super();
        setNodeContent("return ;");
        caseNum = 4;
    }

    public AST_STMT(String stmtType, AST_EXP cond, AST_STMT_LIST body) {
        super(cond, body);
        this.cond = cond;
        this.body = body;
        this.stmtType = stmtType;
        setNodeContent(String.format("%s(exp) { stmt list };", stmtType));
        caseNum = 5;
    }

    public AST_STMT(AST_FUNCTION_CALL f) {
        super(f);
        setNodeContent("fucntion call;");
        this.f = f;
        caseNum = 6;
    }

    //TODO finish semantMe for other cases + case of class NIL!!!!!!

    public TYPE SemantMe() {
        switch (this.caseNum) {
            case 0:
                if (vd != null) {
                    vd.SemantMe();
                    return null;
                }
                printErrorAndExit("STMT: case 0");
            case 1:
                if (this.v != null && this.e != null) {
                    if (!matchTypes(v.SemantMe(), e.SemantMe())) {
                        printErrorAndExit("stmt case 1: assignment issue");
                    }
                    return null;
                }
                printErrorAndExit("STMT: case 1");
            case 2:
                if (this.v != null && this.ne != null) {
                    if (!matchTypes(v.SemantMe(), ne.SemantMe())) {
                        printErrorAndExit("stmt case 2");
                    }
                    return null;

                }
                printErrorAndExit("stmt null");
            case 3:
                if (this.e != null) {
                    if (!matchTypes(AST_FUNCD.currentFunction.returnType, e.SemantMe())) {
                        printErrorAndExit("stmt case 1");
                    }
                    return null;
                }
                printErrorAndExit("stmt null");

            case 4:
                if (AST_FUNCD.currentFunction.returnType != TYPE_VOID.getInstance())
                    printErrorAndExit("Type miss match");
                return null;


            case 5:
                if (stmtType != null) {
                    if (stmtType.equals("if") || stmtType.equals("while")) {
                        if (!(this.cond.SemantMe().isInt())) {
                            printErrorAndExit("stmt case 5");
                        }
                        SYMBOL_TABLE.getInstance().beginScope();
                        this.body.SemantMe();
                        SYMBOL_TABLE.getInstance().endScope();
                        return null;
                    }
                }
                printErrorAndExit("smt null");
            case 6:
                if (f != null) {
                    f.SemantMe();
                    return null;
                }
                printErrorAndExit("smt null");
            default:
                printErrorAndExit("Type missmatch");

        }
        return null;
    }

    private boolean matchTypes(TYPE type, TYPE assignmentType) {
        if (type.name.equals(assignmentType.name) && type.getTypeName().equals(assignmentType.getTypeName())) {
            return true;
        }
        if ((type.isClass() || type.isArray()) && assignmentType.isNil()) {
            return true;
        }
        if (type.isClass() && assignmentType.isClass()) {
            TYPE_CLASS assignmentClass = (TYPE_CLASS) assignmentType;
            if (assignmentClass.hasParent((TYPE_CLASS) type)) {
                return true;
            }
        }
        if (caseNum == 2 && type.isArray() && assignmentType.isArray()) {
            TYPE_ARRAY arr = (TYPE_ARRAY) type;
            TYPE_ARRAY assignmentArr = (TYPE_ARRAY) assignmentType;
            if (arr.type.name.equals(assignmentArr.type.name)) {
                return true;
            }
        }
        return false;
    }


    public TEMP IRme() { //will return always null
        switch (this.caseNum) {
            case 0:
                return vd.IRme();
            case 1:
                return IRme_Assign();
            case 2:
                return IRme_Assign();
            case 3:
                return IRme_Return();
            case 4:
                return IRme_Return();
            case 5:
                return stmtType.equals("if") ? IRme_If() : IRme_While();
            case 6:
                return f.IRme();
         }
         return null;
    }

    public TEMP IRme_Assign(){
        switch (v.caseNum){
            case 0:
                return IRme_AssignSimpleVar();
            case 1:
                return IRme_AssignVarField();
            case 2:
                return IRme_AssignVarAtIndex();
        }
        return null; //should not reach here
    }

    public TEMP IRme_AssignSimpleVar(){
        TEMP expTemp;
        if(caseNum==1){
            expTemp = e.IRme();
        }else{
            expTemp = ne.IRme();
        }
        if (v.varKind == VAR_KIND.GLOBAL) {
            IR.getInstance().Add_IRcommand(new IRcommand_Store_Global_Var(v.name,expTemp));
        } else if (v.varKind == VAR_KIND.LOCAL) {
            IR.getInstance().Add_IRcommand(new IRcommand_Store_Local_Var(v.varIndex,expTemp));
        } else if (v.varKind == VAR_KIND.DATA_MEMBER) {
            IR.getInstance().Add_IRcommand(new IRcommand_Change_Field_Var(v.varIndex,expTemp));
        } else if (v.varKind == VAR_KIND.FUNC_PARAM) {
            IR.getInstance().Add_IRcommand(new IRcommand_Change_Func_Param(v.varIndex,expTemp));
        }
        return null;
    }

    public TEMP IRme_AssignVarField(){
        TEMP expTemp;
        TEMP varTemp = v.var.IRme();
        int fieldIndex = GlobalVariables.classNameAstClassD.get(v.tc.name).fieldNames.indexOf(v.name)+1;
        if(caseNum==1){
            expTemp = e.IRme();
        }else{
            expTemp = ne.IRme();
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Check_Invalid_Pointer(varTemp));
        IR.getInstance().Add_IRcommand(new IRcommand_Add_Offset(varTemp,varTemp,4*(fieldIndex)));
        IR.getInstance().Add_IRcommand(new IRcommand_Store(varTemp, expTemp));
        return null;
    }

    public TEMP IRme_AssignVarAtIndex(){
        TEMP indexTemp = v.exp.IRme();
        TEMP varTemp = v.var.IRme();
        TEMP expTemp;
        if(caseNum==1){
            expTemp = e.IRme();
        }else{
            expTemp = ne.IRme();
        }
        IR.getInstance().Add_IRcommand( new IRCommand_Change_Var_At_Index(expTemp,varTemp,indexTemp) );
        return null;
    }

    public TEMP IRme_Return(){
        if(caseNum==3) { //return exp;
            IR.getInstance().Add_IRcommand(new IRcommand_Return(e.IRme(), AST_FUNCD.currentFunction.endLabel));
        }else { //return ;
            IR.getInstance().Add_IRcommand(new IRcommand_Return(AST_FUNCD.currentFunction.endLabel));
        }
        return null;
    }

    public TEMP IRme_If(){
        TEMP condTemp = null;
        if(cond != null) condTemp = cond.IRme();
        String afterIfLabel = IRcommand.getFreshLabel("afterIf");
        IR.getInstance().Add_IRcommand(new IRcommand_BEQZ(condTemp, afterIfLabel));
        if(body != null) body.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Create_Label(afterIfLabel));
        return null;
    }

    public TEMP IRme_While(){
        TEMP condTemp = null;
        String whileCondLabel = IRcommand.getFreshLabel("whileCondition");
        String whileBodyLabel = IRcommand.getFreshLabel("whileBody");
        IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(whileCondLabel));
        IR.getInstance().Add_IRcommand(new IRcommand_Create_Label(whileBodyLabel));
        if(body != null) body.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Create_Label(whileCondLabel));
        if(cond != null) condTemp = cond.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_BNEZ(condTemp, whileBodyLabel));
        return null;
    }

    @Override
    public String getName() {
        return "STMT";
    }
}
