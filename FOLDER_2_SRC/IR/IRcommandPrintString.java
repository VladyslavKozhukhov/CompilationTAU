package IR;

import MIPS.*;
import TEMP.*;

public class IRcommandPrintString extends IRcommand{
    TEMP tmp;

	public IRcommandPrintString( TEMP tmp){
        this.tmp = tmp;

    }

        /***************/
        /* MIPS me !!! */
        /***************/
        public void MIPSme()
        {
            sir_MIPS_a_lot.getInstance().print_string(tmp);
        }
}
