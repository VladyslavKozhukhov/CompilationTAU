package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Binop_Div_Integers extends IRcommand {
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Div_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }
    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {

        String label_end = getFreshLabel("end");
        String division = getFreshLabel("division");
        String label_overflow = getFreshLabel("overflow");
        String label_underflow = getFreshLabel("underflow");

        TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();

        // check if trying to divide by zero
        sir_MIPS_a_lot.getInstance().bnez(t2, division);
        sir_MIPS_a_lot.getInstance().printStringFromDataSection("string_illegal_div_by_0");
        sir_MIPS_a_lot.getInstance().exit();

        // if not:
        sir_MIPS_a_lot.getInstance().label(division);
        sir_MIPS_a_lot.getInstance().div(dst, t1, t2);
        sir_MIPS_a_lot.getInstance().checkIntOverflowUnderflow(dst, label_overflow, label_underflow, label_end, tmp);
    }
}
