package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;

public class IRcommand_Binop_Sub_Integers extends IRcommand {
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Sub_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }
    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {

        String endLabel = getFreshLabel("end");
        String overflowLabel = getFreshLabel("overflow");
        String underflowLabel = getFreshLabel("underflow");

        TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();

        sir_MIPS_a_lot.getInstance().sub(dst, t1, t2);
        sir_MIPS_a_lot.getInstance().checkIntOverflowUnderflow(dst, overflowLabel, underflowLabel, endLabel,tmp);
    }
}
