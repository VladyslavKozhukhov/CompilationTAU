package IR;

import MIPS.sir_MIPS_a_lot;

public class IRcommand_Init_Func_Call extends IRcommand {
    public int paramCnt;
    public String name;


    public IRcommand_Init_Func_Call( int paramCnt ,String name)//Ra
    {
        this.paramCnt = paramCnt;
        this.name = name;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {

        if ( !(name.equals("PrintInt") || name.equals("PrintString") ) ){
            sir_MIPS_a_lot.getInstance().store_registers();	// store registers before function call.
        }
        sir_MIPS_a_lot.getInstance().allocate_stack( this.paramCnt );	// make place on the stack for function parameters.
    }
}

