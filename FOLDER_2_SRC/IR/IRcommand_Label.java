/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Label extends IRcommand
{
	String label_name;
	private static IRcommand_Label instance = null;


	public IRcommand_Label(String label_name)
	{
		this.label_name = label_name;
	}

	public static IRcommand_Label getInstance()
	{
		if (instance == null)
		{
			instance = new IRcommand_Label();
		}
		return instance;
	}
	public IRcommand_Label() {}

	public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().label(label_name);
	}
	public String getLabel(String name){
		return getFreshLabel(name);
	}
}



