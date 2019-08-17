package Auxiliary;

import java.io.PrintWriter;

public class ErrorHandler {

    private static PrintWriter file_writer=null;

    public static void printErrorLineAndExit(String desc,int line) {
        System.out.println(String.format("ERROR(%d) - %s", line,desc));
        file_writer.println(String.format("ERROR(%d)", line));
        file_writer.close();
        System.exit(1);
    }

    public static void setWriter(PrintWriter writer) {
        ErrorHandler.file_writer = writer;
    }

}
