package Auxiliary;

import AST.AST_CLASSD;
import AST.AST_VARD;
import IR.IRcommand;
import TYPES.TYPE_FUNCTION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalVariables {

    public static HashMap<String,String> stringsForDataSection = new HashMap<>();
    public static String addStringToDataSectionStringsAndGetLabel(String s){
        if (!stringsForDataSection.containsKey(s)) {
            stringsForDataSection.put(s, "string_"+s);
        }
        return stringsForDataSection.get(s);
    }
    public static HashMap<String, AST_CLASSD> classNameAstClassD = new HashMap<>(); //contains all class nodes
    public static AST_CLASSD InsideClassD= null;   //contains cuurent class node
    public static List<AST_VARD> globalVariablesForDataSection = new ArrayList<>();
    public static boolean preRun = true; //if preRun=true we are searching for all class members in code
    public static int scopeLevel = 0;
    public static int local_count = 1;
    public static int class_vars_count =1;
    public static boolean insertGlobalCommandList = false;


    private static int currentLine;
    public static void setCurrentLine(int line) { //is set in the scan of cup file
        currentLine = line;
    }
    public static int getCurrentLine() {
        return currentLine;
    }



}

