package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Add_Offset extends IRcommand {
    TEMP dst;
    TEMP src;
    int offset;

    public IRcommand_Add_Offset(TEMP dst, TEMP src, int offset) {
        this.dst = dst;
        this.src = src;
        this.offset = offset;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().addi(dst, src, offset);
    }
}
