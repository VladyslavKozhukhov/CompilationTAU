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

public class IRcommand_Load_Global_Var extends IRcommand {
    TEMP dst;
    String var_name;

    public IRcommand_Load_Global_Var(String var_name, TEMP dst) {
        this.dst = dst;
        this.var_name = var_name;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().loadGlobalVar(dst,var_name);
    }
}
