package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Binop_EQ_Str extends IRcommand {
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_EQ_Str(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    public void MIPSme() {
        TEMP char1 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP char2 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP offset1 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP offset2 = TEMP_FACTORY.getInstance().getFreshTEMP();

        String endLabel = getFreshLabel("end");
        String compLoopLabel = getFreshLabel("CompStrLoop");
        String assign1Label = getFreshLabel("AssignOne");
        String assign0Label = getFreshLabel("AssignZero");

        sir_MIPS_a_lot.getInstance().move(offset1, t1);
        sir_MIPS_a_lot.getInstance().move(offset2, t2);
        sir_MIPS_a_lot.getInstance().compareStrings(offset1, offset2, char1, char2,
                compLoopLabel, assign1Label, assign0Label);
        sir_MIPS_a_lot.getInstance().assignZeroOrOne(dst, assign1Label, assign0Label, endLabel);
    }
}
