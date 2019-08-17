package AST;

import IR.*;
import TEMP.*;

public class AST_INT extends AST_NODE_RULE {
    public int value;
    public String sign;

    public AST_INT(String sign, int value) {
        super();
        setNodeContent(sign + String.valueOf(value));
        this.value = value;
        this.sign = sign;
    }

    public TEMP IRme() {
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        int signToMul = sign.equals("+") ? 1 : -1;
        IR.getInstance().Add_IRcommand(new IRcommand_Const_Int(dst, value * signToMul));
        return dst;
    }

    @Override
    public String getName() {
        return "INT";
    }
}
