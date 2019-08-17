package IR;

import MIPS.sir_MIPS_a_lot;

public class IRcommand_Allocate_Data_Space extends IRcommand  {
    public String label;
    public int funcNum;

    public IRcommand_Allocate_Data_Space(String label, int funcNum)
    {
        this.label = label;
        this.funcNum = funcNum;

    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().allocateSpaceData(label, funcNum);
    }
}
