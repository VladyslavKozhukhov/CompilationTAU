package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;
import MIPS.*;

public class IRcommand_Add_To_VTable extends IRcommand {
    public String vTable;
    public String functionLabel;
    public int funcOffset;

    public IRcommand_Add_To_VTable(String vTable, String funcLabel, int offset)
    {
        this.vTable = vTable;
        this.functionLabel = funcLabel;
        this.funcOffset = offset;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        TEMP tableAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP functionAddress = TEMP_FACTORY.getInstance().getFreshTEMP();

        sir_MIPS_a_lot.getInstance().addToFuncTable(vTable, functionLabel, tableAddress, functionAddress, funcOffset);
    }
}
