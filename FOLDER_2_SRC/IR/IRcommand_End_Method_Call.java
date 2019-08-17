package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_End_Method_Call extends IRcommand {

    public TEMP retTmp;
    public TEMP label;
    public int paramCnt;

    public IRcommand_End_Method_Call(TEMP t, int paramCnt) {
        this.retTmp = t;
        this.paramCnt = paramCnt;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {

        sir_MIPS_a_lot.getInstance().allocate_stack(-1 * paramCnt);
        sir_MIPS_a_lot.getInstance().re_store_registers();
        sir_MIPS_a_lot.getInstance().load_return_value(retTmp);
    }
}

