package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Change_Field_Var extends IRcommand{
    TEMP val;
    int classOffset;

    public IRcommand_Change_Field_Var(int classOffset, TEMP val) {
        this.val = val;
        this.classOffset = classOffset;
    }

    public void MIPSme() {
        TEMP classTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().load_param_var(classTemp, 0);
        sir_MIPS_a_lot.getInstance().addi(classTemp, classTemp, 4 * classOffset);
        sir_MIPS_a_lot.getInstance().store(classTemp, val);
    }
}
