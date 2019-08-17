package AST;

import Auxiliary.GlobalVariables;
import IR.*;
import MIPS.sir_MIPS_a_lot;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

import java.util.*;

public class AST_CLASSD extends AST_NODE_RULE {

    public String name;
    public String extendClass;
    public AST_CFIELD_LIST data_members;
    public int caseNum;
    public String vTable;
    HashMap<String, HashMap<String, String>> funcNameAndLabelMap = new HashMap<>();
    public TYPE_CLASS t = null;

    public ArrayList<String> fieldNames = new ArrayList<>();
    public ArrayList<AST_EXP> fieldExps = new ArrayList<>();
    ;
    public ArrayList<String> functionNames = new ArrayList<>();
    public ArrayList<String> functionLabels = new ArrayList<>();

    public AST_CLASSD(String className, AST_CFIELD_LIST cfl) {
        super(cfl);
        setNodeContent(String.format("class name: %s", className));
        this.name = className;
        this.data_members = cfl;
        caseNum = 0;
        //forIrme
        GlobalVariables.classNameAstClassD.put(name, this);

    }

    public AST_CLASSD(String className, AST_CFIELD_LIST cfl, String extendsClassName) {
        super(cfl);
        setNodeContent(String.format("class name: %s\nextends class: %s", className, extendsClassName));
        this.name = className;
        this.extendClass = extendsClassName;
        data_members = cfl;
        caseNum = 1;
        //ForIrme
        GlobalVariables.classNameAstClassD.put(name, this);
    }

    public TYPE SemantMe() {

        if (SYMBOL_TABLE.getInstance().find(SYMBOL_TABLE.getInstance().SCOPE_BOUNDARY) != null) { //check if class is in global scope
            printErrorAndExit("class is not in global scope");
        }

        if (SYMBOL_TABLE.getInstance().find(name) != null) { //check if class with same name already exists
            printErrorAndExit("class name already exists");
        }

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        //ForIRme
        GlobalVariables.InsideClassD = this;
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE_CLASS.currentClassName = name;


        switch (caseNum) {
            case 0:
                t = new TYPE_CLASS(null, name, null);
                if (GlobalVariables.preRun) TYPE_CLASS.addClass(name, t);
                t.data_members = data_members.SemantMe();
                break;
            case 1:
                TYPE_CLASS father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(extendClass);
                if (father == null) {
                    printErrorAndExit("no father exists");

                } else {
                    TYPE_CLASS.currentClassFather = father;
                    t = new TYPE_CLASS(father, name, null);
                    if (GlobalVariables.preRun) TYPE_CLASS.addClass(name, t);
                    t.data_members = data_members.SemantMe();
                }
                break;
        }

        /*****************/
        /* [3] End Scope */
        /*****************/
        t.classVarsNum = GlobalVariables.class_vars_count-1;
        GlobalVariables.class_vars_count = 1;
        SYMBOL_TABLE.getInstance().endScope();

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(name, t);
        if (GlobalVariables.preRun) TYPE_CLASS.addClass(name, t);
        TYPE_CLASS.currentClassName = "";
        TYPE_CLASS.currentClassFather = null;
        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        GlobalVariables.InsideClassD = null;
        vTable = IRcommand_Label.getInstance().getLabel(name + "_vTable");
        if (caseNum == 1) updateFunctionLabel(); //update labels only if class extends another one
        return null;
    }


    public TEMP IRme() {
        GlobalVariables.InsideClassD = this;
        data_members.IRme();
        GlobalVariables.InsideClassD = null;
        return null;
    }

    public void fillVtable() {
        ArrayList<String> functionLabels = this.functionNames;
        for (int i = 0; i < functionLabels.size(); i++) {
            HashMap<String, String> startLabelAndEndLabel = this.funcNameAndLabelMap.get(functionNames.get(i));
            String func = startLabelAndEndLabel.keySet().iterator().next();
            TEMP tableAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
            TEMP functionAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
            sir_MIPS_a_lot.getInstance().addToFuncTable(vTable, func, tableAddress, functionAddress, i);
        }
    }

    public void allocateVTable() {


        //IR.getInstance().Add_IRcommand(new IRcommand_Allocate_Data_Space(vTable, functionLabels.size()));
        sir_MIPS_a_lot.getInstance().allocateSpaceData(vTable, functionNames.size());
    }

    void updateFunctionLabel() {
        AST_CLASSD fatherNode = GlobalVariables.classNameAstClassD.get(extendClass);
        ArrayList<String> fatherFuncNames = fatherNode.functionNames;
        HashMap<String, HashMap<String, String>> fatherMap = fatherNode.funcNameAndLabelMap;
        ArrayList<String> arrangedFunctionNameList = new ArrayList<>(fatherFuncNames);
        for (String func : functionNames) {
            if (!arrangedFunctionNameList.contains(func)) {
                arrangedFunctionNameList.add(func);
            }
        }
        for (String func : fatherFuncNames) {
            if (!funcNameAndLabelMap.containsKey(func)) {
                funcNameAndLabelMap.put(func, fatherMap.get(func));
            }
        }
        functionNames = arrangedFunctionNameList;
    }


    @Override
    public String getName() {
        return "CLASS DEC";
    }

}
