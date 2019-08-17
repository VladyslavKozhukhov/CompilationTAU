package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Const_Int  extends IRcommand{
    TEMP dst;
    int value;

    public IRcommand_Const_Int(TEMP dst, int value) {
        this.dst = dst;
        this.value = value;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().li(dst, value);
    }
}

