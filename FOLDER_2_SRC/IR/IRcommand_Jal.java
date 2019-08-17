package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Jal  extends IRcommand{
    String label_name;
    public IRcommand_Jal(String label_func_name)
    {
        this.label_name=label_func_name;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        sir_MIPS_a_lot.getInstance().jal(label_name);
    }
}
