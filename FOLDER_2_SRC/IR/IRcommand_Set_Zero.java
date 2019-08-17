
package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Set_Zero extends IRcommand
{
    public TEMP dst;

    public IRcommand_Set_Zero(TEMP dst)
    {
        this.dst = dst;
    }

    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().setConst(dst,0);
    }
}

