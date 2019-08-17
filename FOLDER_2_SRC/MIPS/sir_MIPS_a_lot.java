/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/

import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class sir_MIPS_a_lot {

    private int WORD_SIZE = 4;
    /***********************/
    /* The file writer ... */
    /***********************/
    private PrintWriter fileWriter;

    /***********************/
    /* The file writer ... */

    /***********************/
    public void finalizeFile() {
        fileWriter.print("\tli $v0, 10\n");
        fileWriter.print("\tsyscall\n");
        fileWriter.close();
    }

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static sir_MIPS_a_lot instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */

    /*****************************/
    protected sir_MIPS_a_lot() {
    }

    /******************************/
    /* GET SINGLETON INSTANCE ... */

    /******************************/
    public static sir_MIPS_a_lot getInstance() {
        if (instance == null) {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new sir_MIPS_a_lot();

            try {
                /*********************************************************************************/
                /* [1] Open the MIPS text file and write data section with error message strings */
                /*********************************************************************************/
                String dirname = "./FOLDER_5_OUTPUT/";
//                String dirname="/home/vladko/Downloads/COMPILATION/EX4/FOLDER_5_OUTPUT/";
                //String dirname="./FOLDER_5_OUTPUT/";
                String filename = String.format("MIPS.txt");

                /***************************************/
                /* [2] Open MIPS text file for writing */
                /***************************************/
                instance.fileWriter = new PrintWriter(dirname + filename);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*****************************************************/
            /* [3] Print data section with error message strings */
            /*****************************************************/
            instance.fileWriter.print(".data\n");
        }
        return instance;
    }

    //orig
    public void print_int(TEMP t) {
        int idx = t.getSerialNumber();
        fileWriter.format("\tmove $a0,Temp_%d\n", idx);
        fileWriter.format("\tli $v0, 1\n");
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\tli $a0, 32\n");
        fileWriter.format("\tli $v0, 11\n");
        fileWriter.format("\tsyscall\n");
    }

    //orig
    public void allocate(String var_name) {
        fileWriter.format(".data\n");
        fileWriter.format("\tglobal_%s: .word 721\n", var_name);
    }

    //orig
    public void loadGlobalVar(TEMP dst, String var_name) {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tlw Temp_%d,global_%s\n", idxdst, var_name);
    }

    //orig
    public void storeGlobalVar(String var_name, TEMP src) {
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d,global_%s\n", idxsrc, var_name);
    }

    //orig
    public void li(TEMP t, int value) {
        int idx = t.getSerialNumber();
        fileWriter.format("\tli Temp_%d,%d\n", idx, value);
    }

    //orig
    public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();
        fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n", dstidx, i1, i2);
    }

    //orig
    public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();
        fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n", dstidx, i1, i2);
    }

    //orig
    public void label(String inlabel) {
        fileWriter.format("%s:\n", inlabel);
    }

    //orig
    public void jump(String inlabel) {
        fileWriter.format("\tj %s\n", inlabel);
    }


    //orig
    public void blt(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    //orig
    public void bge(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    //orig
    public void bne(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    //orig
    public void beq(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n", i1, i2, label);
    }

    //orig
    public void beqz(TEMP oprnd1, String label) {
        int i1 = oprnd1.getSerialNumber();
        fileWriter.format("\tbeq Temp_%d, $zero, %s\n", i1, label);
    }

    /**
     * ORIG End
     **/

    public void jalr(TEMP address) {

        fileWriter.format("\tjalr Temp_%d\n", address.getSerialNumber());

    }


    public void re_store_registers() {
        for (int i = 0; i < 8; i++) {
            fileWriter.format("\tlw	$t%d, %d($sp)\n", i, 4 * i);
        }
        fileWriter.format("\taddi $sp, $sp, 32\n");
    }

    public void load_return_value(TEMP t) {
        fileWriter.format("\taddi Temp_%d, $v0, 0\n", t.getSerialNumber());

    }

    public void store_return_value(TEMP t) {
        fileWriter.format("\taddi $v0, Temp_%d, 0\n", t.getSerialNumber());
    }


    public void bnez(TEMP oprnd1, String label) {
        int i1 = oprnd1.getSerialNumber();
        fileWriter.format("\tbne Temp_%d, $zero, %s\n", i1, label);
    }

    public void move(TEMP dst, TEMP src) {
        fileWriter.format("\tmove Temp_%d, Temp_%d\n", dst.getSerialNumber(), src.getSerialNumber());
    }

    public void checkIntOverflowUnderflow(TEMP dst, String overflowLabel, String underflowLabel, String endLabel, TEMP tmp) {
        li(tmp, 32767);
        blt(tmp, dst, overflowLabel);
        li(tmp, -32768);
        blt(dst, tmp, underflowLabel);
        jump(endLabel);
        label(overflowLabel);
        li(dst, 32767);
        jump(endLabel);
        label(underflowLabel);
        li(dst, -32768);
        label(endLabel);
    }

    public void addToFuncTable(String vTableLable, String funcionLabel, TEMP tableAddress, TEMP functionAddress, int offset) {
        int idTable = tableAddress.getSerialNumber();
        int idFunc = functionAddress.getSerialNumber();
        la(tableAddress, vTableLable);
        la(functionAddress, funcionLabel);
        addi(tableAddress, tableAddress, 4 * offset);
        fileWriter.format("\tsw Temp_%d,(Temp_%d)\n", idFunc, idTable);

    }

    public void la(TEMP dst, String label) {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tla Temp_%d, %s\n", idxdst, label);
    }

    public void load_param_var(TEMP dst, int fpOffset) {
        int id = dst.getSerialNumber();
        int offset = 4 * fpOffset + 8;
        fileWriter.format("\tlw Temp_%d, %d($fp)\n", id, offset);
    }

    public void store_registers() {
        fileWriter.format("\taddi $sp, $sp, -32\n");
        for (int i = 0; i < 8; i++) {
            fileWriter.format("\tsw	$t%d, %d($sp)\n", i, 4 * i);
        }
    }

    public void allocate_stack(int size) {
        fileWriter.format("\taddi $sp, $sp, %d\n", -4 * size);

    }

    public void store_param_var(int fpOffset, TEMP src) {
        int id = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d, %d($sp)\n", id, 4 * fpOffset);

    }

    public void saveRa() {
        fileWriter.format("\tsw	$ra, 0($sp)\n");    // save ra
    }

    public void openNewFuncFrame(int SizeFrame,TEMP funcNameTemp) {
        fileWriter.format("\taddi $sp, $sp, -8\n");    // make place to save the frame pointer and ra
        fileWriter.format("\tsw	$fp, 0($sp)\n");    // save fp
        fileWriter.format("\tsw	$ra, 4($sp)\n");    // save ra
        fileWriter.format("\taddi $fp, $sp, 0\n");    // set fp = sp
        fileWriter.format("\taddi $sp, $sp, %d\n", -4 * (SizeFrame+1));    // allocate the stack frame + 1 for func name
        storeLocalVar(0,funcNameTemp); //the function name pointer is at offset 0 from fp

    }

    public void closeFuncFrame(int SizeFrame, String endLabel, String name) {
        fileWriter.format("%s: \n", endLabel);
        fileWriter.format("\taddi $sp, $sp, %d\n", 4 * (SizeFrame+1));
        fileWriter.format("\tlw	$ra, 4($fp)\n");
        fileWriter.format("\tlw	$fp, ($fp)\n");
        fileWriter.format("\taddi $sp, $sp, 8\n");
        if (name.equals("main")) {
            return;
        }
        fileWriter.format("\tjr $ra\n");
    }

    public void lb(TEMP dst, TEMP address, int offset) {
        int dstidx = dst.getSerialNumber();
        int addressidx = address.getSerialNumber();
        fileWriter.format("\tlb Temp_%d, %d(Temp_%d)\n", dstidx, offset, addressidx);
    }

    public void sb(TEMP src, TEMP address, int offset) {
        int srcidx = src.getSerialNumber();
        int addressidx = address.getSerialNumber();
        fileWriter.format("\tsb Temp_%d,%d(Temp_%d)\n", srcidx, offset, addressidx);
    }

    public void addi(TEMP dst, TEMP val, int imm) {
        int validx = val.getSerialNumber();
        int dstidx = dst.getSerialNumber();
        fileWriter.format("\taddi Temp_%d,Temp_%d,%d\n", dstidx, validx, imm);
    }

    public void sub(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();
        fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n", dstidx, i1, i2);
    }

    public void compareStrings(TEMP pointerToStr1, TEMP pointerToStr2, TEMP charAtPointer1Pos, TEMP charAtPointer2Pos,
                               String compLoopLabel, String assign1Label, String assign0Label) {
        label(compLoopLabel);
        lb(charAtPointer1Pos, pointerToStr1, 0);
        lb(charAtPointer2Pos, pointerToStr2, 0);
        bne(charAtPointer1Pos, charAtPointer2Pos, assign1Label);
        beqz(charAtPointer1Pos, assign0Label);
        addi(pointerToStr1, pointerToStr1, 1);
        addi(pointerToStr2, pointerToStr2, 1);
        jump(compLoopLabel);
    }

    public void assignZeroOrOne(TEMP dst, String assign1Label, String assign0Label, String endLabel) {
        label(assign1Label);
        li(dst, 1);
        jump(endLabel);
        label(assign0Label);
        li(dst, 0);
        label(endLabel);
    }

    //adds the string length to "TEMP length"
    public void addStringLen(TEMP length, TEMP charTemp, TEMP strPointer, String inLabel) {
        label(inLabel);
        lb(charTemp, strPointer, 0);
        addi(length, length, 1);
        addi(strPointer, strPointer, 1);
        bnez(charTemp, inLabel);
        addi(length, length, -1);
    }

    public void malloc(TEMP dest, TEMP size) {

        int idxdst = dest.getSerialNumber();
        int idxsiz = size.getSerialNumber();
        fileWriter.format("\taddi $a0, Temp_%d, 0\n", idxsiz);
        fileWriter.format("\tli $v0,9\n");
        fileWriter.format("\tsyscall\n");
        fileWriter.format("\taddi Temp_%d , $v0 , 0\n", idxdst);
    }

    public void srl(TEMP dst, TEMP src, int num) {
        int idxdst = dst.getSerialNumber();
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsrl Temp_%d, Temp_%d,%d\n", idxdst, idxsrc, num);

    }

    public void moveAString(TEMP dstStringPointer, TEMP charTemp, TEMP srcStringPointer, String inLabel) {
        label(inLabel);
        lb(charTemp, srcStringPointer, 0);
        sb(charTemp, dstStringPointer, 0);
        addi(dstStringPointer, dstStringPointer, 1);
        addi(srcStringPointer, srcStringPointer, 1);
        bnez(charTemp, inLabel);
        addi(dstStringPointer, dstStringPointer, -1);
    }

    public void concatStrings(TEMP t1, TEMP t2, TEMP dst, TEMP charTemp, TEMP tempPointer, TEMP dstPointer, TEMP length, String s1_len_label, String s2_len_label, String s1_concat_label, String s2_concat_label) {
        //set dst string length
        li(length, 1); // null terminator
        move(tempPointer, t1);
        addStringLen(length, charTemp, tempPointer, s1_len_label);
        move(tempPointer, t2);
        addStringLen(length, charTemp, tempPointer, s2_len_label);

        malloc(dst, length);
        move(dstPointer, dst); //pointer to beginning of allocated memory

        //concat both strings
        move(tempPointer, t1);
        moveAString(dstPointer, charTemp, tempPointer, s1_concat_label);
        move(tempPointer, t2); //TODO check if need to add 1 to dstPointer
        moveAString(dstPointer, charTemp, tempPointer, s2_concat_label);
    }

    public void exit() {
        fileWriter.format("\tli $v0, 10\n");
        fileWriter.format("\tsyscall\n");
    }

    public void div(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();
        fileWriter.format("\tdiv Temp_%d, Temp_%d\n", i1, i2);
        fileWriter.format("\tmflo Temp_%d\n", dstidx);
    }

    public void printStringFromDataSection(String label) {
        fileWriter.format("\tla $a0, %s\n", label);
        fileWriter.format("\tli $v0,4\n");
        fileWriter.format("\tsyscall\n");
    }

    public void bgt(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        fileWriter.format("\tbgt Temp_%d, Temp_%d, %s\n", i1, i2, label);
    }

    public void jal(String label) {
        fileWriter.format("\tjal %s\n", label);

    }

    public void ble(TEMP oprnd1, TEMP oprnd2, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        fileWriter.format("\tble Temp_%d, Temp_%d, %s\n", i1, i2, label);
    }

    public void print_string(TEMP t) {
        fileWriter.format("\taddi $a0, Temp_%d,0\n", t.getSerialNumber());
        fileWriter.format("\tli $v0,4\n");
        fileWriter.format("\tsyscall\n");
    }


    public void storeStringLiteral(String label, String value) {
        fileWriter.format("\t%s: .asciiz \"%s\"\n", label, value);
    }

    public void store(TEMP dst, TEMP src) {
        int idxdst = dst.getSerialNumber();
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d,0(Temp_%d)\n", idxsrc, idxdst);

    }

    public void storeStringMessages() {
        storeStringLiteral("string_access_violation", "Access Violation");
        storeStringLiteral("string_illegal_div_by_0", "Division By Zero");
        storeStringLiteral("string_invalid_ptr_dref", "Invalid Pointer Dereference");
    }

    public void startCodeSegment() {
        fileWriter.print(".text\n");
        label("main");
    }

    public void addGlobalVarToDataSection(String name) {
        fileWriter.printf("\tglobal_%s: .word 0\n", name);
    }

    public void loadLocalVar(TEMP dst, int fpOffset) {
        int idx = dst.getSerialNumber();
        int offset = -4 * (fpOffset+1);
        fileWriter.format("\tlw Temp_%d, %d($fp)\n", idx, offset);
    }

    public void setConst(TEMP tmp, int val) {
        fileWriter.format("\taddi Temp_%d, $zero, %d\n", tmp.getSerialNumber(), val);

    }

    public void initializeFP(){
        fileWriter.format("\taddi $fp, $zero, 0\n");
    }

    public void load(TEMP dst, TEMP src) {
        int idxdst = dst.getSerialNumber();
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tlw Temp_%d,0(Temp_%d)\n", idxdst, idxsrc);
    }

    public void bltz(TEMP oprnd, String label) {
        int idx = oprnd.getSerialNumber();
        fileWriter.format("\tbltz Temp_%d, %s\n", idx, label);

    }

    public void sll(TEMP dst, TEMP src, int num) {
        int idxdst = dst.getSerialNumber();
        int idxsrc = src.getSerialNumber();
        fileWriter.format("\tsll Temp_%d, Temp_%d, %d\n", idxdst, idxsrc, num);
    }


    public void changeFuncParam(TEMP dst, int paramIndex) {
        int idx = dst.getSerialNumber();
        fileWriter.format("\tsw Temp_%d, %d($fp)\n", idx, 4 * (paramIndex) + 8);
    }
    //todo


    public void storeLocalVar(int fpOffset, TEMP src) {
        int idx = src.getSerialNumber();
        fileWriter.format("\tsw Temp_%d, %d($fp)\n", idx, -4 * (fpOffset+1));
    }
    //todo

    public void allocateSpaceData(String name, int size) {
        fileWriter.printf("\t%s: .space %d\n", name, 4 * size);
    }


    public void cleanAlloaction(TEMP address, TEMP size, String label) {
        TEMP offset = TEMP_FACTORY.getInstance().getFreshTEMP();

        int idxadr = address.getSerialNumber();
        int idxsiz = size.getSerialNumber();
        int idxoff = offset.getSerialNumber();

        fileWriter.format("\tadd Temp_%d, Temp_%d, Temp_%d\n", idxoff, idxadr, idxsiz);
        fileWriter.format("%s: \n", label);
        fileWriter.format("\taddi Temp_%d, Temp_%d, -4\n", idxoff, idxoff);
        fileWriter.format("\tsw $zero, 0(Temp_%d)\n", idxoff);
        fileWriter.format("\tbne Temp_%d, Temp_%d, %s\n", idxoff, idxadr, label);
    }

    public void print_trace(String printTraceLabel) {
        TEMP curFp   = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP funcNamePointer = TEMP_FACTORY.getInstance().getFreshTEMP();
        int curFpIdx = curFp.getSerialNumber();
        int funcNamePointerIdx = funcNamePointer.getSerialNumber();

        fileWriter.format("\tmove Temp_%d, $fp\n", curFpIdx);
        label(printTraceLabel);
        fileWriter.format("\tlw Temp_%d, -4(Temp_%d)\n", funcNamePointerIdx, curFpIdx);
        print_string(funcNamePointer);
        load(curFp,curFp);
        bnez(curFp,printTraceLabel);
    }

}
