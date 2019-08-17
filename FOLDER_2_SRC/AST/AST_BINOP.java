package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_INT;
import TYPES.TYPE_STRING;

public class AST_BINOP extends AST_NODE_RULE {

    public AST_EXP e1;
    public AST_EXP e2;
    String op;
    //For IRme
    public TYPE e1Type;
    public TYPE e2Type;


    public AST_BINOP(AST_EXP e1, AST_EXP e2, String op) {
        super(e1, e2);
        setNodeContent(op);
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }


    public TYPE SemantMe() {
        e1Type = e1.SemantMe();
        e2Type = e2.SemantMe();
        if (e1Type != null && e2Type != null) {
            if (this.op.equals("=")) { //check types in case they are array or class (as not always TypeName!=name in this case)
                if (e1Type.name.equals(e2Type.name)) {
                    return TYPE_INT.getInstance();
                }
                if ((e1Type.isClass() && e2Type.isNil()) || (e1Type.isNil() && e2Type.isClass()) || (e1Type.isArray() && e2Type.isNil()) || (e1Type.isNil() && e2Type.isArray())) {
                    return TYPE_INT.getInstance();
                }
                if (e1Type.isClass() && e2Type.isClass()) {
                    TYPE_CLASS c1 = (TYPE_CLASS) e1Type;
                    TYPE_CLASS c2 = (TYPE_CLASS) e2Type;
                    if (c1.hasParent(c2) || c2.hasParent(c1)) {
                        return TYPE_INT.getInstance();
                    }
                }
            }
            if (this.op.equals("-") || this.op.equals("*") || this.op.equals("/") || this.op.equals("<") || this.op.equals(">")) {
                if (e1Type.isInt() && e2Type.isInt()) {
                    return TYPE_INT.getInstance();

                }
            }
            if (this.op.equals("+")) {
                if (e1Type.isInt() && e2Type.isInt()) {
                    return TYPE_INT.getInstance();
                }
                if (e1Type.isString() && e2Type.isString()) {
                    return TYPE_STRING.getInstance();
                }
            }
        }
        printErrorAndExit("BINOP: types " + e1Type.name + " " + e2Type.name + " don't match");
        return null;
    }

    public TEMP IRme() {
        TEMP t1;
        TEMP t2;
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

        t1 = e1.IRme();
        t2 = e2.IRme();

        if (op.equals("=")) {
            if (e1Type.isString() && e2Type.isString()) {
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Str(dst, t1, t2));
            } else {
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
            }
        }
        if (op.equals("+")) {
            if (e1Type.isString() && e2Type.isString()) {
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Strings(dst, t1, t2));
            } else {
                IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, t1, t2));
            }
        }
        if (op.equals("-")) {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst, t1, t2));
        }
        if (op.equals("*")) {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst, t1, t2));
        }
        if (op.equals("/")) {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_Div_Integers(dst, t1, t2));
        }
        if (op.equals(">")) {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_GT_Integers(dst, t1, t2));
        }
        if (op.equals("<")) {
            IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(dst, t1, t2));
        }
        return dst;
    }

    @Override
    public String getName() {
        return "BINOP";
    }
}
