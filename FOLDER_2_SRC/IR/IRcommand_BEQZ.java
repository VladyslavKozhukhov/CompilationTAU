package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_BEQZ extends IRcommand{
    public TEMP cond;
    public String label;

    public IRcommand_BEQZ(TEMP cond, String jumpToLabel) {
        this.cond = cond;
        this.label = jumpToLabel;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().beqz(cond, label);
    }
}
