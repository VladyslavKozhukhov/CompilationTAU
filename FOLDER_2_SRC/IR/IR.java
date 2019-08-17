/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */

import AST.AST_CLASSD;
import Auxiliary.GlobalVariables;
import MIPS.sir_MIPS_a_lot;

import java.util.ArrayList;
import java.util.List;

/*******************/

public class IR
{
	private IRcommand head=null;
	private IRcommandList tail=null;

	/************************************/
	/* Add global allocation IR command */
	/************************************/
	public List<IRcommand> globalCommandList = new ArrayList<>();

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd)
	{
		if(GlobalVariables.insertGlobalCommandList) {
			globalCommandList.add(cmd);
		}else{
			if ((head == null) && (tail == null))
			{
				this.head = cmd;
			}
			else if ((head != null) && (tail == null))
			{
				this.tail = new IRcommandList(cmd,null);
			}
			else
			{
				IRcommandList it = tail;
				while ((it != null) && (it.tail != null))
				{
					it = it.tail;
				}
				it.tail = new IRcommandList(cmd,null);
			}
		}
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		printDataToFile();
		sir_MIPS_a_lot.getInstance().startCodeSegment();
		allocateData();
		sir_MIPS_a_lot.getInstance().initializeFP();
		sir_MIPS_a_lot.getInstance().jump("main_function");
		if (head != null) head.MIPSme();
		if (tail != null) tail.MIPSme();
	}

	private void printDataToFile() {
		//add vtable to data segment
		GlobalVariables.classNameAstClassD.forEach((k,v)->v.allocateVTable());
		//strings
		GlobalVariables.stringsForDataSection.forEach((k, v) -> sir_MIPS_a_lot.getInstance().storeStringLiteral(v, k));
		//my constant string (error messages etc)
		sir_MIPS_a_lot.getInstance().storeStringMessages();
		//global vars
		GlobalVariables.globalVariablesForDataSection.forEach(globalVar -> sir_MIPS_a_lot.getInstance().addGlobalVarToDataSection(globalVar.name));

	}

	private void allocateData() {
		globalCommandList.forEach(globalVarAllocation -> globalVarAllocation.MIPSme()); //allocate all global vars before jumping to main
		for(AST_CLASSD cld: GlobalVariables.classNameAstClassD.values())
			cld.fillVtable();
	}
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
}
