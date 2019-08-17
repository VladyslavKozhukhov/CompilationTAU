package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Load_Local_Var extends IRcommand{
    int fpOffset;
    TEMP dest;

    public IRcommand_Load_Local_Var(int fpOffset, TEMP dest) {
        this.fpOffset = fpOffset;
        this.dest = dest;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().loadLocalVar(dest, this.fpOffset);
    }
}
