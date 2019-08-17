package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Check_Invalid_Pointer extends IRcommand{
    public TEMP var;

    public IRcommand_Check_Invalid_Pointer(TEMP var)
    {
        this.var = var;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        String passedCheckLabel = getFreshLabel("not_nil");
        sir_MIPS_a_lot.getInstance().bnez(var,passedCheckLabel);
        sir_MIPS_a_lot.getInstance().printStringFromDataSection("string_invalid_ptr_dref");
        sir_MIPS_a_lot.getInstance().exit();
        sir_MIPS_a_lot.getInstance().label(passedCheckLabel);
    }
}
