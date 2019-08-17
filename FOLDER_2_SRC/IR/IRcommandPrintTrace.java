package IR;

import MIPS.sir_MIPS_a_lot;

public class IRcommandPrintTrace extends IRcommand {
    public void MIPSme()
    {
        String printTraceLabel = IRcommand.getFreshLabel("printTrace");
        sir_MIPS_a_lot.getInstance().print_trace(printTraceLabel);
    }
}
