package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Load_Class_Field extends IRcommand{
    TEMP dst;
    int fieldIndxInClass;

    public IRcommand_Load_Class_Field(int fieldIndxInClass, TEMP dst) {
        this.dst = dst;
        this.fieldIndxInClass = fieldIndxInClass;
    }
    public void MIPSme() {
        TEMP classPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().load_param_var(classPointer, 0);
        sir_MIPS_a_lot.getInstance().addi(classPointer, classPointer, 4 * this.fieldIndxInClass);
        sir_MIPS_a_lot.getInstance().load(dst, classPointer);
    }
}
