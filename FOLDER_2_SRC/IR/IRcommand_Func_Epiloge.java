package IR;

import MIPS.sir_MIPS_a_lot;

public class IRcommand_Func_Epiloge extends IRcommand {
    public int frameSize;
    public String name;
    public String funcEndLabel;

    public IRcommand_Func_Epiloge(int frameSize, String funcEndLabel, String name) {
        this.frameSize = frameSize;
        this.name = name;
        this.funcEndLabel = funcEndLabel;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().closeFuncFrame(frameSize, this.funcEndLabel, this.name);
    }
}