
import java.io.*;
import java.io.PrintWriter;

import Auxiliary.ErrorHandler;
import Auxiliary.GlobalVariables;
import SYMBOL_TABLE.SYMBOL_TABLE;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import MIPS.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];

//		String inputFilename ="/home/vladko/Desktop/testPrimes.txt"; //argv[0];
//		String outputFilename = "/home/vladko/Desktop/outPrimes.txt";
//		String outputFilename = "C:/Users/Daniel/Desktop/try.txt";
//		String inputFilename = "C:/Users/Daniel/Documents/University/Semester_7/Compilation/COMPILATION/EX4/FOLDER_4_INPUT/tempTest.txt";
//		String inputFilename = "C:/Users/Daniel/Desktop/TESTS/6call_method_with_params.txt";

		try
		{
			/*
				FIRST ROUND
			 */
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			ErrorHandler.setWriter(file_writer);
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l,file_writer);
			AST = (AST_PROGRAM) p.parse().value;
			AST.SemantMe();

			//INITIALIZE GLOBAL VARIABLES
			GlobalVariables.preRun = false;
			SYMBOL_TABLE.resetInstance();

			/*
				SECOND ROUND
			 */
			file_reader = new FileReader(inputFilename);
			file_writer = new PrintWriter(outputFilename);
			ErrorHandler.setWriter(file_writer);
			l = new Lexer(file_reader);
			p = new Parser(l,file_writer);
			AST = (AST_PROGRAM) p.parse().value;
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();
			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			AST.SemantMe();
			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			AST.IRme();
			/***********************/
			/* [9] MIPS the IR ... */
			/***********************/
			IR.getInstance().MIPSme();
			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();
			/***************************/
			/* [11] Finalize MIPS file */
			/***************************/
			sir_MIPS_a_lot.getInstance().finalizeFile();

			RegisterAllocation.allocate(outputFilename);

			/**************************/
			/* [12] Close output file */
			/**************************/

			file_writer.close();
		}

		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}


