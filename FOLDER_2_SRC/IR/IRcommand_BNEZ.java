package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_BNEZ extends IRcommand{
    public TEMP cond;
    public String label;

    public IRcommand_BNEZ(TEMP cond, String label) {
        this.cond = cond;
        this.label = label;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().bnez(cond, label);
    }
}
