package IR;


import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class IRcommand_Func_Prologue extends IRcommand
{
    public String funcLabel;
    public int frameSize;
    public String funcNameLabelForPrintTrace;

    public IRcommand_Func_Prologue(String funcName, int frameSize, String funcNameLabelForPrintTrace)
    {
        this.funcLabel = funcName;
        this.frameSize = frameSize;
        this.funcNameLabelForPrintTrace = funcNameLabelForPrintTrace;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        TEMP funcNameTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().label( this.funcLabel );
        sir_MIPS_a_lot.getInstance().la(funcNameTemp, funcNameLabelForPrintTrace);
        sir_MIPS_a_lot.getInstance().openNewFuncFrame(this.frameSize,funcNameTemp);
    }
}
