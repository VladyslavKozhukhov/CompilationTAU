package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Finnish_Func_Call extends IRcommand {
    public TEMP retTmp;
    public String name;
    public int paramCnt;

    public IRcommand_Finnish_Func_Call( TEMP t, String name, int cnt )
    {
        this.retTmp = t;
        this.name = name;
        this.paramCnt = cnt;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        if (name == null ){
            System.out.println("ERROR NULL NAME");
            System.exit(0);
        }
        sir_MIPS_a_lot.getInstance().jal( String.format( this.name ) );
        sir_MIPS_a_lot.getInstance().allocate_stack( -1*this.paramCnt );
        sir_MIPS_a_lot.getInstance().re_store_registers();
        sir_MIPS_a_lot.getInstance().load_return_value( retTmp );
    }
}
