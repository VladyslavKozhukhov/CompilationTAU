package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Load_Param_Var extends IRcommand {
    int offset;
    TEMP dest;

    public IRcommand_Load_Param_Var(int offset ,TEMP dest)
    {
        this.offset = offset;
        this.dest = dest;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().load_param_var( dest,this.offset );
    }
}
