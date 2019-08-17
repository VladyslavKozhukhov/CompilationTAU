
package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Store_Local_Var extends IRcommand {
    int offset;
    TEMP src;

    public IRcommand_Store_Local_Var(int offset, TEMP src) {
        this.offset = offset;
        this.src = src;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().storeLocalVar(this.offset, this.src);
    }
}
