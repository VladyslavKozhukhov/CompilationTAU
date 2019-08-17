package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Return extends IRcommand {
    public String name;
    public TEMP retVal = null;

    public IRcommand_Return(TEMP retVal, String name) {
        this.retVal = retVal;
        this.name = name;
    }

    public IRcommand_Return(String name) {
        this.name = name;
    }

    public void MIPSme() {
        if (retVal != null) sir_MIPS_a_lot.getInstance().store_return_value(retVal);
        sir_MIPS_a_lot.getInstance().jump(name);
    }
}
