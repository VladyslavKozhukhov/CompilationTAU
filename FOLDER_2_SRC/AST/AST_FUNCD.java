package AST;

import Auxiliary.GlobalVariables;
import Auxiliary.VAR_KIND;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.*;

import java.util.HashMap;


public class AST_FUNCD extends AST_NODE_RULE {
    public String returnTypeName;
    public String name;
    public AST_TYPE_NAME_LIST params;
    public AST_STMT_LIST body;
    public int caseNum;
    //global variable contains the current function during program run
    public static TYPE_FUNCTION currentFunction = null;
    //variables for IRme
    public int localVariablesNum;
    private String funcNameLabelForPrintTrace;
    public TYPE_FUNCTION myFuncType = null;


    TYPE t;

    public AST_FUNCD(String retType, String name, AST_TYPE_NAME_LIST params, AST_STMT_LIST body) {
        super(params, body);
        setNodeContent(String.format("func(%s)\n:%s\n", name, retType));
        this.returnTypeName = retType;
        this.name = name;
        this.params = params;
        this.body = body;
        caseNum = 0;
    }


    public AST_FUNCD(String retType, String name, AST_STMT_LIST body) {
        super(body);
        setNodeContent(String.format("func(%s)\n:%s\n", name, retType));
        caseNum = 1;
        this.returnTypeName = retType;
        this.name = name;
        this.body = body;
    }

    public TYPE SemantMe() {
        TYPE returnType = null;
        TYPE_LIST type_list = null;

        /* GLOBAL FUNCTIO CHECK */
        boolean globalFunction = false;
        if (SYMBOL_TABLE.getInstance().find(SYMBOL_TABLE.getInstance().SCOPE_BOUNDARY) == null) {
            globalFunction = true;
        }
        /*******************/
        /* [0] return type */
        /*******************/
        returnType = SYMBOL_TABLE.getInstance().find(returnTypeName);
        if (returnType == null || !returnType.name.equals(returnTypeName) || !(returnType.isInt() || returnType.isString() || returnType.isClass() || returnType.isArray() || returnType.isVoid())) {
            if (returnTypeName.equals(TYPE_CLASS.currentClassName)) {
                returnType = TYPE_CLASS.getCurrentClass();
            } else {
                printErrorAndExit("FUNC DEC the return type was not found in table");
            }
        }

        /****************************/
        /* [1] Begin Function Scope */
        /****************************/

        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Input Params */
        /***************************/
        int paramIndex = 1;
        for (AST_TYPE_NAME_LIST it = params; it != null; it = it.tail) //check that all parameter types exist and build
        {
            t = SYMBOL_TABLE.getInstance().find(it.head.type);
            if (t == null) {
                if (it.head.type.equals(TYPE_CLASS.currentClassName)) {
                    t = TYPE_CLASS.getCurrentClass();
                } else {
                    printErrorAndExit("FUNC DEC parameter does not exist in table");
                }
            }
            type_list = new TYPE_LIST(t, type_list);
            TYPE_PARAM tp = new TYPE_PARAM(t, it.head.name);
            tp.index = paramIndex++;        //INDEXING for IRme
            tp.kind = VAR_KIND.FUNC_PARAM;
            SYMBOL_TABLE.getInstance().enter(it.head.name, tp);
        }

        //reverse list
        TYPE_LIST reversedParameters = null;
        for (TYPE_LIST it = type_list; it != null; it = it.tail) //check that all parameter types exist and build
        {
            reversedParameters = new TYPE_LIST(it.head, reversedParameters);
        }

        currentFunction = new TYPE_FUNCTION(returnType, name, reversedParameters, paramIndex);
        myFuncType = currentFunction;

        //ForIRme
        setFuncLabels();
        if (GlobalVariables.InsideClassD != null) {
            myFuncType.cls = GlobalVariables.InsideClassD.name;
            GlobalVariables.InsideClassD.functionLabels.add(myFuncType.label);
            GlobalVariables.InsideClassD.functionLabels.add(myFuncType.endLabel);
            HashMap<String, String> labelsPair = new HashMap<String, String>();
            labelsPair.put(myFuncType.label, myFuncType.endLabel);
            GlobalVariables.InsideClassD.funcNameAndLabelMap.put(this.name, labelsPair);
            GlobalVariables.InsideClassD.functionNames.add(this.name);
        }
        /*
            NAME CHECK
         */
        if (globalFunction) {
            if (SYMBOL_TABLE.getInstance().findFromLevel(name, 0) != null) {
                printErrorAndExit("FUNC DEC: global function name in use");
            }
        } else {
            if (TYPE_CLASS.inClass() && GlobalVariables.scopeLevel == 2) { //scope level is will be 2 as we opened a scope in class (1 for class 1 for func) -> class method
                if (SYMBOL_TABLE.getInstance().findUntilLevel(name, 1) != null) { //[1] check if name is in current scope
                    printErrorAndExit("FUNC DEC this name " + name + " is already in use in class scope");
                } else { //[2] check in class members
                    if (name.equals(TYPE_CLASS.currentClassName)) {
                        printErrorAndExit("FUNC DEC: this name " + name + " is already in use as a class name of current class");
                    }
                    if (TYPE_CLASS.currentClassFather != null && (TYPE_CLASS.currentClassFather.name.equals(name) || TYPE_CLASS.currentClassFather.hasParentWithName(name))) {
                        printErrorAndExit("FUNC DEC: this name " + name + " is already in use as a name of current class parents");
                    }
                    TYPE foundType = TYPE_CLASS.findInParentsDataMembersOfCurrentClass(name);
                    if (foundType != null && foundType.isFunction()) {
                        TYPE_FUNCTION overrideMethod = (TYPE_FUNCTION) foundType;
                        if (!overrideMethod.equals(new TYPE_FUNCTION(returnType, name, type_list))) { //if yes -> overriding | no -> overloading
                            printErrorAndExit("FUNC DEC method overloading");
                        }
                    }
                }

            } else {
                printErrorAndExit("FUNC DEC found not in class or in global");

            }
        }

        /*******************/
        /* [3] Semant Body */
        /*******************/
        if (!GlobalVariables.preRun) {
            body.SemantMe();
        }
        /*****************/
        /* [4] End Scope */
        /*****************/

        localVariablesNum = GlobalVariables.local_count;
        GlobalVariables.local_count = 0;
        SYMBOL_TABLE.getInstance().endScope();
        /***************************************************/
        /* [5] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(name, myFuncType);
        currentFunction = null;
        /*********************************************************/
        /* [6] Return value is irrelevant for class declarations */
        /*********************************************************/
        return myFuncType;
    }


    void setFuncLabels() {
        if (this.name.equals("main")) {
            myFuncType.label = "main_function";
        } else {
            myFuncType.label = IRcommand.getFreshLabel(this.name);
        }
        funcNameLabelForPrintTrace = GlobalVariables.addStringToDataSectionStringsAndGetLabel(name);
        myFuncType.endLabel = IRcommand.getFreshLabel("end" + this.name);
    }

    public TEMP IRme() {
        IR.getInstance().Add_IRcommand(new IRcommand_Func_Prologue(myFuncType.label, localVariablesNum,funcNameLabelForPrintTrace));
        currentFunction = myFuncType; //used in stmt return
        body.IRme();
        currentFunction = null;
        IR.getInstance().Add_IRcommand(new IRcommand_Func_Epiloge(localVariablesNum, myFuncType.endLabel, name));
        return null;
    }

    @Override
    public String getName() {
        return "FUNC DEC";
    }
}
