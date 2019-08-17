package TYPES;

import Auxiliary.VAR_KIND;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	public VAR_KIND kind;
	public int index = 0; //the index of the local variable in a function

	public String getTypeName() { return ""; } //instead of using a.getClass

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	public boolean isArray(){ return false;}

	public boolean isInt(){ return false;}

	public boolean isNil(){ return false;}

	public boolean isString(){ return false;}

	public String getNAME(){return name;}

	public boolean isFunction(){ return false;}

	public boolean isVoid(){ return false;}

	public boolean isVarDec(){ return false;}

	public boolean isParam(){ return false;}

}
