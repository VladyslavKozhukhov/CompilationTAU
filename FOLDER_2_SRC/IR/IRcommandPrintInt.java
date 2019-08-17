package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommandPrintInt extends IRcommand {
    TEMP tmp;
    public IRcommandPrintInt(TEMP tmp)
    {
        this.tmp =tmp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().print_int( tmp );
    }
}

