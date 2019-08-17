package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Allocate_Class extends IRcommand{
    TEMP dst;
    int size;

    public IRcommand_Allocate_Class(TEMP dst,int size)
    {
        this.dst = dst;
        this.size = size*4;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        TEMP tmp =  TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().setConst( tmp , size );
        sir_MIPS_a_lot.getInstance().malloc(dst, tmp);
    }
}
