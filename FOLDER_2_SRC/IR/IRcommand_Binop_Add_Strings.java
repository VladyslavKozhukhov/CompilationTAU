/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Binop_Add_Strings extends IRcommand {
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Add_Strings(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        TEMP charTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP tempPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP dstPointer = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP length = TEMP_FACTORY.getInstance().getFreshTEMP();

        String s1_len_label = getFreshLabel("Length_String1");
        String s2_len_label = getFreshLabel("Length_String2");
        String s1_concat_label = getFreshLabel("Concat_String1");
        String s2_concat_label = getFreshLabel("Concat_String2");

        sir_MIPS_a_lot.getInstance().concatStrings(t1, t2, dst, charTemp, tempPointer, dstPointer, length, s1_len_label, s2_len_label, s1_concat_label, s2_concat_label);
    }

}
