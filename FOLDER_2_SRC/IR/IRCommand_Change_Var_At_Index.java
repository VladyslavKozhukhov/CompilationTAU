package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;
public class IRCommand_Change_Var_At_Index extends IRcommand{
    TEMP val;
    TEMP dst;
    TEMP index;

    public IRCommand_Change_Var_At_Index(TEMP src, TEMP dst, TEMP index)
    {
        this.val = src;
        this.dst = dst;
        this.index = index;
    }


    public void MIPSme()
    {
        String notValidIndexLabel = getFreshLabel("not_valid_index");
        String endLabel = getFreshLabel("end");

        TEMP size = TEMP_FACTORY.getInstance().getFreshTEMP();

        //index is ge from zero and lt size
        sir_MIPS_a_lot.getInstance().load(size, dst);
        sir_MIPS_a_lot.getInstance().bge(index, size, notValidIndexLabel);
        sir_MIPS_a_lot.getInstance().bltz(index, notValidIndexLabel);

        sir_MIPS_a_lot.getInstance().addi(index, index, 1);
        sir_MIPS_a_lot.getInstance().sll(index, index, 2);
        sir_MIPS_a_lot.getInstance().add(dst, dst, index);
        sir_MIPS_a_lot.getInstance().store(dst,val );
        sir_MIPS_a_lot.getInstance().jump(endLabel);
        sir_MIPS_a_lot.getInstance().label(notValidIndexLabel);
        sir_MIPS_a_lot.getInstance().printStringFromDataSection("string_access_violation");
        sir_MIPS_a_lot.getInstance().exit();
        sir_MIPS_a_lot.getInstance().label(endLabel);
    }
}
