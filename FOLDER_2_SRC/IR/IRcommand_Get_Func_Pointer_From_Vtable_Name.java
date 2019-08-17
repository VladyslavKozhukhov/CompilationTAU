package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Get_Func_Pointer_From_Vtable_Name extends IRcommand {
    TEMP dst;
    String src;
    int offset;

    public IRcommand_Get_Func_Pointer_From_Vtable_Name(TEMP dst, String src, int offset)
    {
        this.dst = dst;
        this.src= src;
        this.offset = offset;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        System.out.println("Took func from offset: " + offset);
        TEMP vtable = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().la(vtable,src);
        sir_MIPS_a_lot.getInstance().addi(vtable,vtable,4*offset);
        sir_MIPS_a_lot.getInstance().load(dst, vtable);

    }
}
