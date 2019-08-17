package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Allocate_Array extends IRcommand {

    public TEMP array_address;
    public TEMP size;

    public IRcommand_Allocate_Array(TEMP array_address,TEMP size)
    {
        this.array_address = array_address;
        this.size = size;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        String label = getFreshLabel("clearAllocation");
        sir_MIPS_a_lot.getInstance().addi(this.size,this.size,1);
        sir_MIPS_a_lot.getInstance().sll(this.size,this.size,2);
        sir_MIPS_a_lot.getInstance().malloc(this.array_address, this.size);
        sir_MIPS_a_lot.getInstance().cleanAlloaction(array_address, size, label);
        sir_MIPS_a_lot.getInstance().srl(this.size,this.size,2);
        sir_MIPS_a_lot.getInstance().addi(this.size,this.size,-1);
        sir_MIPS_a_lot.getInstance().store(this.array_address,this.size);
    }
}
