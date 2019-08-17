package AST;

import Auxiliary.GlobalVariables;
import Auxiliary.*;
import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.*;
import TYPES.*;

public class AST_VARD extends AST_NODE_RULE {

    public String type;
    public String name;
    public AST_EXP exp;
    public AST_NEW_EXP newExp;
    public int caseNum;
    public TYPE t;

    TYPE_CLASS_VAR_DEC myType;

    public AST_VARD(String type, String name) {
        super();
        setNodeContent(String.format("name: %s\ntype: %s", name, type));
        this.name = name;
        this.type = type;
        caseNum = 0;
    }

    public AST_VARD(String type, String name, AST_EXP exp) {
        super(exp);
        setNodeContent(String.format("name: %s\ntype: %s", name, type));
        this.type = type;
        this.name = name;
        this.exp = exp;
        caseNum = 1;
    }

    public AST_VARD(String type, String name, AST_NEW_EXP nExp) {
        super(nExp);
        setNodeContent(String.format("name: %s\ntype: %s", name, type));
        this.type = type;
        this.name = name;
        this.newExp = nExp;
        caseNum = 2;
    }


    public TYPE_CLASS_VAR_DEC SemantMe() {

        verifyVarDecName();

        if (type.equals(TYPE_CLASS.currentClassName)) { //if class type is declared inside itself
            t = TYPE_CLASS.getCurrentClass(); //data members are null although it might not be true
        } else {
            t = SYMBOL_TABLE.getInstance().find(type);
            if (t == null || !t.name.equals(type) || !(t.isInt() || t.isString() || t.isClass() || t.isArray())) {
                printErrorAndExit("VAR DEC: non existing type");
            }
        }

        if (!GlobalVariables.preRun) { //if not pre run perform type comparison tests
            switch (caseNum) {
                case 1:
                    AST_EXP.simpleExp = false;
                    if (!matchTypes(t, this.exp.SemantMe())) {
                        printErrorAndExit("VAR DEC EXP assignment types are not equal");
                    }
                    if (isVarDecAClassDataMember() && !AST_EXP.simpleExp) { //needs to come after to check if simpleExp
                        printErrorAndExit("VAR DEC: a data member with name " + name + " is not assigned to constant");
                    }
                    break;
                case 2:
                    if (isVarDecAClassDataMember()) {
                        printErrorAndExit("VAR DEC: a data member with name " + name + " is generated as new");
                    }
                    if (!matchTypes(t, this.newExp.SemantMe())) {
                        printErrorAndExit("VAR DEC NEW EXP assignment types are not equal");
                    }
                    break;
            }
        }
        myType = new TYPE_CLASS_VAR_DEC(t, name);
        setVarKindAndIndex(); //for IRme
        SYMBOL_TABLE.getInstance().enter(name, myType);
        return myType;
    }

    private void setVarKindAndIndex() {
        if (SYMBOL_TABLE.getInstance().find(SYMBOL_TABLE.getInstance().SCOPE_BOUNDARY) == null) //in global scope
        {
            myType.kind = VAR_KIND.GLOBAL;
            if (!GlobalVariables.preRun) GlobalVariables.globalVariablesForDataSection.add(this);
        }
        if (AST_FUNCD.currentFunction != null) { //in function --> var dec is a local dec
            myType.kind = VAR_KIND.LOCAL;
            myType.index = ++GlobalVariables.local_count;
        }
        if (TYPE_CLASS.inClass() && GlobalVariables.scopeLevel == 1) { //var is a data member -> in class but not in func
            myType.kind = VAR_KIND.DATA_MEMBER;
            myType.index = clacNumberOfFatherVardecs() + GlobalVariables.class_vars_count++;
            //FOR IRme
            GlobalVariables.InsideClassD.fieldNames.add(this.name);
            GlobalVariables.InsideClassD.fieldExps.add(exp);
        }
    }

    private void verifyVarDecName() {
        if (name.equals("int") || name.equals("string") || name.equals("void")/* || name.equals("PrintString") || name.equals("PrintInt")*/) { //TODO change to better
            printErrorAndExit("VAR DEC: EXP type is current class but not assigned to nil");                                     //TODO need to check if name = some class name
        }
        if (SYMBOL_TABLE.getInstance().findInScope(name) != null) {
            printErrorAndExit("VAR DEC: a type with name " + name + " already exists in scope");
        }
        if (isVarDecAClassDataMember() && TYPE_CLASS.findInParentsDataMembersOfCurrentClass(name) != null) { //if var name a class member
            printErrorAndExit("VAR DEC: a data member with name " + name + " already exists in parent class");
        }
        if (isVarDecAClassDataMember() && name.equals(TYPE_CLASS.currentClassName)) {
            printErrorAndExit("VAR DEC: this name " + name + " is already in use as a class name of current class");
        }
        if (isVarDecAClassDataMember() && TYPE_CLASS.currentClassFather != null && (TYPE_CLASS.currentClassFather.name.equals(name) || TYPE_CLASS.currentClassFather.hasParentWithName(name))) {
            printErrorAndExit("VAR DEC: this name " + name + " is already in use as a name of current class parents");
        }
    }

    private boolean isVarDecAClassDataMember() {
        return TYPE_CLASS.inClass() && GlobalVariables.scopeLevel == 1;
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


    public TEMP IRme() {
        if (myType.kind == VAR_KIND.GLOBAL) GlobalVariables.insertGlobalCommandList = true;
        TEMP tmp;
        if (exp == null && newExp == null) {
            tmp = TEMP_FACTORY.getInstance().getFreshTEMP();
            IR.getInstance().Add_IRcommand(new IRcommand_Set_Zero(tmp));
        } else {
            if (caseNum == 1) {
                tmp = exp.IRme();
            } else {
                tmp = newExp.IRme();
            }
        }
        switch (myType.kind) {
            case GLOBAL:
                IR.getInstance().Add_IRcommand(new IRcommand_Store_Global_Var(name, tmp));
                break;
            case LOCAL:
                IR.getInstance().Add_IRcommand(new IRcommand_Store_Local_Var(myType.index, tmp));
                break;
        }
        if (myType.kind == VAR_KIND.GLOBAL) GlobalVariables.insertGlobalCommandList = false;
        return null;
    }


    private int clacNumberOfFatherVardecs() {
        TYPE_CLASS father = TYPE_CLASS.currentClassFather;
        int counter = 0;
        while (father != null) {
            counter += father.classVarsNum;
            father = father.father;
        }
        return counter;
    }

    @Override
    public String getName() {
        return "VAR DEC";
    }

}
