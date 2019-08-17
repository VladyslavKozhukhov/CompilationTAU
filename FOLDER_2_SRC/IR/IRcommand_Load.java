package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Load extends IRcommand {
    TEMP dst;
    TEMP src;

    public IRcommand_Load(TEMP dst,TEMP src)
    {
        this.dst = dst;
        this.src = src;
    }

    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().load(dst, src);

    }
}
