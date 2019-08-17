package AST;

import Auxiliary.GlobalVariables;
import IR.*;
import TEMP.*;
import TYPES.*;

public class AST_EXP extends AST_NODE_RULE {

    public int caseNum;
    private AST_VAR astVar;
    private AST_EXP astExp;
    private AST_BINOP astBinop;
    private AST_INT expInt;
    private AST_FUNCTION_CALL astFuncCall;
    public static boolean simpleExp;

    private String stringLabel;

    public AST_EXP(AST_VAR v) {
        super(v);
        setNodeContent("var");
        this.astVar = v;
        caseNum = 0;
    }

    public AST_EXP(AST_EXP e) {
        super(e);
        setNodeContent("(exp)");
        this.astExp = e;
        caseNum = 1;
    }

    public AST_EXP(AST_BINOP b) {
        super(b);
        setNodeContent("binop");
        this.astBinop = b;
        caseNum = 2;
    }

    public AST_EXP(AST_FUNCTION_CALL f) {
        super(f);
        setNodeContent("function call");
        this.astFuncCall = f;
        caseNum = 3;

    }

    public AST_EXP(AST_INT i) {
        super(i);
        setNodeContent("int");
        caseNum = 4;
        this.expInt = i;
    }

    public AST_EXP() {
        super();
        setNodeContent("NIL");
        caseNum = 5;
    }

    public AST_EXP(String s) {
        super();
        String cleanString = s.replace("\"","");
        stringLabel = GlobalVariables.addStringToDataSectionStringsAndGetLabel(cleanString);
        if (!GlobalVariables.stringsForDataSection.containsKey(cleanString)) { //adds string and associates it with label
            GlobalVariables.stringsForDataSection.put(cleanString, IRcommand.getFreshLabel(cleanString));
        }
        stringLabel = GlobalVariables.stringsForDataSection.get(cleanString);
        setNodeContent(String.format("STRING: \"%s\"", s));
        caseNum = 6;
    }

    public TYPE SemantMe() {
        switch (caseNum) {
            case 0:
                return astVar.SemantMe();
            case 1:
                return astExp.SemantMe();
            case 2:
                return astBinop.SemantMe();
            case 3:
                return astFuncCall.SemantMe();
            case 4:
                AST_EXP.simpleExp = true;
                return TYPE_INT.getInstance();
            case 5:
                AST_EXP.simpleExp = true;
                return TYPE_NIL.getInstance();
            case 6:
                AST_EXP.simpleExp = true;
                return TYPE_STRING.getInstance();
        }
        return null;
    }


    public TEMP IRme() {
        switch (caseNum) {
            case 0:
                return astVar.IRme();
            case 1:
                return astExp.IRme();
            case 2:
                return astBinop.IRme();
            case 3:
                return astFuncCall.IRme();
            case 4:
                return expInt.IRme();
            case 5:
                return IRme_ExpNil();
            case 6:
                return IRme_ExpString();
        }
        return null;
    }

    private TEMP IRme_ExpString() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Load_Address(dst, stringLabel));
        return dst;
    }

    private TEMP IRme_ExpNil() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand( new IRcommand_Set_Zero( dst ) );
        return dst;
    }

    @Override
    public String getName() {
        return "EXP";
    }
}