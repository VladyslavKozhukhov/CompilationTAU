package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Jalr  extends IRcommand{
    TEMP label_name;
        public IRcommand_Jalr(TEMP label_func_name)
        {
            this.label_name=label_func_name;
        }

        /***************/
        /* MIPS me !!! */
        /***************/
        public void MIPSme()
        {
            sir_MIPS_a_lot.getInstance().jalr(label_name);
        }
}
