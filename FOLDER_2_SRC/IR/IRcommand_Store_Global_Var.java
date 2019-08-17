package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Store_Global_Var extends  IRcommand {

    String name;
    TEMP val;

    public IRcommand_Store_Global_Var(String name, TEMP val) {
        this.name = name;
        this.val = val;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() { sir_MIPS_a_lot.getInstance().storeGlobalVar(this.name, this.val); }
}
