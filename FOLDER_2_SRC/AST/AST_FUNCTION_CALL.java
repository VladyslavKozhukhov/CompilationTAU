package AST;

import Auxiliary.GlobalVariables;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

import java.util.ArrayList;

public class AST_FUNCTION_CALL extends AST_NODE_RULE {

    public String funcName;
    public AST_EXP_LIST eLst;
    public AST_VAR astVar;
    public int label_offset = 0;
    TYPE_FUNCTION tf;
    TYPE t;

    private int caseNum;

    public AST_FUNCTION_CALL(String funcName) {
        super();
        setNodeContent(String.format("call(%s) with no params)", funcName));
        this.funcName = funcName;
        caseNum = 0;
    }

    public AST_FUNCTION_CALL(String funcName, AST_EXP_LIST eLst) {
        super(eLst);
        setNodeContent(String.format("call(%s)", funcName));
        this.funcName = funcName;
        this.eLst = eLst;
        caseNum = 1;
    }

    public AST_FUNCTION_CALL(AST_VAR v, String funcName) {
        super(v);
        setNodeContent(String.format("call(%s) from var no params", funcName));
        this.funcName = funcName;
        this.astVar = v;
        caseNum = 2;
    }

    public AST_FUNCTION_CALL(AST_VAR v, String funcName, AST_EXP_LIST eLst) {
        super(v, eLst);
        setNodeContent(String.format("call(%s) from var", funcName));
        this.funcName = funcName;
        this.astVar = v;
        this.eLst = eLst;
        caseNum = 3;
    }

    public TYPE SemantMe() {
        if (caseNum == 0 || caseNum == 1) {
            //start looking for function dec
            if (SYMBOL_TABLE.getInstance().find(SYMBOL_TABLE.getInstance().SCOPE_BOUNDARY) == null) { //in global scope
                t = SYMBOL_TABLE.getInstance().find(funcName);

            } else {
                t = TYPE_CLASS.findInCurrentClassOrParentsDataMembers(funcName); //if not in class or not found in class and parents than null will be returned
                if (t == null || !t.isFunction()) {
                    t = SYMBOL_TABLE.getInstance().findFromLevel(funcName, 0); //look for method in global scope
                }
            }
            if (t == null) {
                if (AST_FUNCD.currentFunction.name.equals(funcName)) {
                    t = AST_FUNCD.currentFunction;
                }
            }
            if (t == null || !t.isFunction()) {
                printErrorAndExit("FUNC CALL: no func with name " + funcName + " found");
            }
            //end looking for function dec

            tf = (TYPE_FUNCTION) t;

            if (tf == null) {
                System.out.println("FUNC CALL casting issue");
                System.exit(1);
            }

            if (caseNum == 0) {
                if (!tf.isDeclarationOf(new TYPE_FUNCTION(null, funcName, null))) {
                    printErrorAndExit("FUNC CALL: case 0 params don't much function declaration");
                }
                return tf.returnType;
            } else {
                if (!tf.isDeclarationOf(new TYPE_FUNCTION(null, funcName, eLst.SemantMe()))) {
                    printErrorAndExit("FUNC CALL: case 1 params don't much function declaration");
                }
                return tf.returnType;
            }
        } else {
            TYPE typeVar = astVar.SemantMe();
            if (typeVar == null || !typeVar.isClass()) {
                astVar.printErrorAndExit("FUNC CALL: case 2/3 var " + astVar.name + " not defined as class");
            }
            TYPE_CLASS tc = TYPE_CLASS.classes.get(typeVar.name); //need to get real class as var does not contain data members
            t = tc.findInAllDataMembersAvailableInClass(funcName);
            if (t == null || !t.isFunction()) {
                printErrorAndExit("FUNC CALL: case 2/3 did not find func with name " + funcName + " in class " + tc.name);
            }
            tf = (TYPE_FUNCTION) t;
            if (caseNum == 3) {
                if (!tf.isDeclarationOf(new TYPE_FUNCTION(null, funcName, eLst.SemantMe()))) {
                    printErrorAndExit("FUNC CALL: case 0 params don't much function declaration");
                }
            }
            return tf.returnType;
        }
    }

    public TEMP IRme() {
        AST_CLASSD clD;
        TEMP vtablePointer;
        String vtableName;
        String fucncLabel = tf.label;
        ArrayList<TEMP> parameterList;
        int paramCnt = tf.paramCnt;
        if (funcName.equals("PrintInt") && tf.cls == null) { //tf.cls==null checks if function is glboal
            parameterList = getTempParameterList();
            IR.getInstance().Add_IRcommand(new IRcommandPrintInt(parameterList.get(0)));
            return null;
        } else if (funcName.equals("PrintString")  && tf.cls == null) {
            parameterList = getTempParameterList();
            IR.getInstance().Add_IRcommand(new IRcommandPrintString(parameterList.get(0)));
            return null;
        } else if (funcName.equals("PrintTrace")  && tf.cls == null) {
            IR.getInstance().Add_IRcommand(new IRcommandPrintTrace());
            return null;
        }
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP func_address;
        switch (caseNum) {
            case 0:
                if (tf.cls != null) {
                    func_address = TEMP_FACTORY.getInstance().getFreshTEMP();
                    clD = GlobalVariables.classNameAstClassD.get(tf.cls);
                    label_offset = clD.functionNames.indexOf(this.funcName);
                    vtableName = GlobalVariables.classNameAstClassD.get(tf.cls).vTable;
                    TEMP classPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
                    IR.getInstance().Add_IRcommand(new IRcommand_Load_Param_Var(0, classPointer));
                    IR.getInstance().Add_IRcommand(new IRcommand_Get_Func_Pointer_From_Vtable_Name(func_address, vtableName, label_offset));
                    IR.getInstance().Add_IRcommand(new IRcommand_Init_Func_Call(1, this.funcName));
                    IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(0, classPointer)); //push class to stack as the first parameter
                    IR.getInstance().Add_IRcommand(new IRcommand_Jalr(func_address));
                    IR.getInstance().Add_IRcommand(new IRcommand_End_Method_Call(t, 1));

                } else {
                    IR.getInstance().Add_IRcommand(new IRcommand_Init_Func_Call(1, this.funcName));
                    IR.getInstance().Add_IRcommand(new IRcommand_Jal(fucncLabel));
                    IR.getInstance().Add_IRcommand(new IRcommand_End_Method_Call(t, 1));

                }
                break;
            case 1:
                if (tf.cls != null) {
                    func_address = TEMP_FACTORY.getInstance().getFreshTEMP();
                    clD = GlobalVariables.classNameAstClassD.get(tf.cls);
                    label_offset = clD.functionNames.indexOf(this.funcName);
                    vtableName = GlobalVariables.classNameAstClassD.get(tf.cls).vTable;
                    TEMP classPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
                    IR.getInstance().Add_IRcommand(new IRcommand_Load_Param_Var(0, classPointer));//save the class pointer
                    IR.getInstance().Add_IRcommand(new IRcommand_Get_Func_Pointer_From_Vtable_Name(func_address, vtableName, label_offset));
                    IR.getInstance().Add_IRcommand(new IRcommand_Init_Func_Call(paramCnt + 1, this.funcName));
                    parameterList = getTempParameterList();
                    int offset = 0;
                    while (offset < parameterList.size()) {
                        IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(offset + 1, parameterList.get(offset)));
                        offset += 1;
                    }
                    IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(0, classPointer)); //push class to stack as the first parameter
                    IR.getInstance().Add_IRcommand(new IRcommand_Jalr(func_address));
                    IR.getInstance().Add_IRcommand(new IRcommand_End_Method_Call(t, paramCnt + 1));
                } else {
                    IR.getInstance().Add_IRcommand(new IRcommand_Init_Func_Call(paramCnt, this.funcName));
                    parameterList = getTempParameterList();
                    int offset = 0;
                    while (offset < parameterList.size()) {
                        IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(offset+1, parameterList.get(offset)));
                        offset += 1;
                    }
                    IR.getInstance().Add_IRcommand(new IRcommand_Jal(fucncLabel));
                    IR.getInstance().Add_IRcommand(new IRcommand_End_Method_Call(t, paramCnt));
                }
                break;
            case 2:
                func_address = TEMP_FACTORY.getInstance().getFreshTEMP();
                clD = GlobalVariables.classNameAstClassD.get(tf.cls);
                label_offset = clD.functionNames.indexOf(this.funcName);
                vtablePointer = this.astVar.IRme();
                IR.getInstance().Add_IRcommand(new IRcommand_Check_Invalid_Pointer(vtablePointer));
                IR.getInstance().Add_IRcommand(new IRcommand_Get_Func_Pointer_From_Vtable_Temp(func_address, vtablePointer, label_offset));
                IR.getInstance().Add_IRcommand(new IRcommand_Init_Func_Call(1, this.funcName));
                IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(0, vtablePointer)); //ADDED NOW !!!
                IR.getInstance().Add_IRcommand(new IRcommand_Jalr(func_address));
                IR.getInstance().Add_IRcommand(new IRcommand_End_Method_Call(t, 1)); //1 for param pointer
                break;
            case 3:
                func_address = TEMP_FACTORY.getInstance().getFreshTEMP();
                clD = GlobalVariables.classNameAstClassD.get(tf.cls);
                label_offset = clD.functionNames.indexOf(this.funcName);
                vtablePointer = this.astVar.IRme();
                IR.getInstance().Add_IRcommand(new IRcommand_Get_Func_Pointer_From_Vtable_Temp(func_address, vtablePointer, label_offset));
                IR.getInstance().Add_IRcommand(new IRcommand_Init_Func_Call(paramCnt + 1, this.funcName));
                if (eLst != null) {
                    eLst.IRme();
                }
                parameterList = getTempParameterList();
                int offset = 0;
                while (offset < parameterList.size()) {
                    IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(offset + 1, parameterList.get(offset)));
                    offset += 1;
                }
                IR.getInstance().Add_IRcommand(new IRcommand_Store_Param_Var(0, vtablePointer));
                IR.getInstance().Add_IRcommand(new IRcommand_Jalr(func_address));
                IR.getInstance().Add_IRcommand(new IRcommand_End_Method_Call(t, paramCnt + 1));//+1 for class pointer
                break;
        }
        return t;
    }

    private ArrayList<TEMP> getTempParameterList() {
        if (eLst != null) {
            TEMP_LIST tmpList = eLst.IRme_List();
            ArrayList<TEMP> paramsList = new ArrayList<>();
            for (TEMP_LIST tmp = tmpList; tmp != null; tmp = tmp.tail) {
                paramsList.add(tmp.head);
            }
            return paramsList;
        }
        return null;
    }

    @Override
    public String getName() {
        return "FUNCTION CALL";
    }
}
