package IR;

        import MIPS.sir_MIPS_a_lot;
        import TEMP.*;

public class IRcommand_Get_Func_Pointer_From_Vtable_Temp extends IRcommand {
    TEMP dst;
    TEMP src;
    int offset;

    public IRcommand_Get_Func_Pointer_From_Vtable_Temp(TEMP dst, TEMP src, int offset)
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
        TEMP label_address = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().load(label_address, src);
        sir_MIPS_a_lot.getInstance().addi(label_address,label_address,4*offset);
        sir_MIPS_a_lot.getInstance().load(dst,label_address); //take the function address to dst
    }
}
