package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Change_Func_Param extends IRcommand {
    int paramIndex;
    TEMP dest;

    public IRcommand_Change_Func_Param(int paramIndex, TEMP src)
    {
        this.paramIndex = paramIndex;
        this.dest = src;
    }

    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().changeFuncParam( dest,this.paramIndex);
    }
}
