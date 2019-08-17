/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

import TEMP.*;
import MIPS.*;

public class IRcommand_Binop_EQ_Integers extends IRcommand {
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_EQ_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        String label_end = getFreshLabel("end");
        String assign1Label = getFreshLabel("AssignOne");
        String assign0Label = getFreshLabel("AssignZero");

        sir_MIPS_a_lot.getInstance().beq(t1, t2, assign1Label);
        sir_MIPS_a_lot.getInstance().bne(t1, t2, assign0Label);
        sir_MIPS_a_lot.getInstance().assignZeroOrOne(dst, assign1Label, assign0Label, label_end);
    }
}
