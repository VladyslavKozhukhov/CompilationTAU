/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

/*******************/

public abstract class IRcommand {
    /*****************/
    /* Label Factory */
    /*****************/
    protected static int label_counter = 0;

    public static String getFreshLabel(String msg) {
        return String.format("Label_%d_%s", label_counter++, msg);
    }

    public static String getNewGlobalLabel(int place) {
        return String.format("Label_%d_Global_%d", label_counter++, place);
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public abstract void MIPSme();
}
