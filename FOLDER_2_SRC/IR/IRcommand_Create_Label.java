package IR;

import MIPS.sir_MIPS_a_lot;

public class IRcommand_Create_Label extends IRcommand{

    public String label;

    public IRcommand_Create_Label(String label)
    {
        this.label = label;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().label(label);
    }
}
