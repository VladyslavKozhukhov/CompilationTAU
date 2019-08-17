package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Store_Param_Var  extends IRcommand {

    int fpOffset;
    TEMP src;

    public IRcommand_Store_Param_Var(int fpOffset, TEMP src) {
        this.fpOffset = fpOffset;
        this.src = src;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().store_param_var(this.fpOffset, this.src);
    }
}

