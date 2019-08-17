package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Load_Address extends IRcommand {
    TEMP dst;
    String label;

    public IRcommand_Load_Address(TEMP dst, String label) {
        this.dst = dst;
        this.label = label;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().la(dst, label);
    }
}
