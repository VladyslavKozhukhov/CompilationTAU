package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Load_Var_At_Index extends IRcommand{

    TEMP dst;
    TEMP var;
    TEMP index;

    public IRcommand_Load_Var_At_Index(TEMP dst,TEMP var, TEMP index)
    {
        this.dst = dst;
        this.var = var;
        this.index = index;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        String notValidIndexLabel = getFreshLabel("not_valid_index");
        String endLabel = getFreshLabel("end");

        TEMP size = TEMP_FACTORY.getInstance().getFreshTEMP();

        //index is ge from zero and lt size
        sir_MIPS_a_lot.getInstance().load(size, var);
        sir_MIPS_a_lot.getInstance().bge(index, size, notValidIndexLabel);
        sir_MIPS_a_lot.getInstance().bltz(index, notValidIndexLabel);
        sir_MIPS_a_lot.getInstance().addi(index, index, 1);
        sir_MIPS_a_lot.getInstance().sll(index, index, 2);
        sir_MIPS_a_lot.getInstance().add(var, var, index);
        sir_MIPS_a_lot.getInstance().load(dst, var);
        sir_MIPS_a_lot.getInstance().jump(endLabel);
        sir_MIPS_a_lot.getInstance().label(notValidIndexLabel);
        sir_MIPS_a_lot.getInstance().printStringFromDataSection("string_access_violation");
        sir_MIPS_a_lot.getInstance().exit();
        sir_MIPS_a_lot.getInstance().label(endLabel);
    }
}
