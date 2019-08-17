package AST;

import java.util.Arrays;
import java.util.List;

import Auxiliary.ErrorHandler;
import Auxiliary.GlobalVariables;
import TEMP.TEMP;
import TYPES.TYPE;


public abstract class AST_NODE_RULE extends AST_Node {

    private List<AST_Node> children;
    private boolean childrenExist;
    private String nodeContent;
    private int line;

    public AST_NODE_RULE() {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.line = GlobalVariables.getCurrentLine();
        this.nodeContent = this.getName();
    }

    public AST_NODE_RULE(AST_Node... children) {

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        this();

        if (children != null && children.length > 0) {

            this.childrenExist = true;
            /*******************************/
            /* COPY INPUT DATA NENBERS ... */
            /*******************************/

            this.children = Arrays.asList(children);

            /***************************************/
            /* PRINT CORRESPONDING DERIVATION RULE */
            /***************************************/
            StringBuilder childrenNames = new StringBuilder();
            for (AST_Node child : this.children) {
                childrenNames.append(" ");
                childrenNames.append(child.getName());
            }
            System.out.print("====================== " + this.getName() + " ->" + childrenNames + "\n");
        }
    }

    /**************************************************/
    /* The printing message for a declaration class with no extends AST node */

    /**************************************************/
    public void PrintMe() {
        /**********************************/
        /* AST NODE TYPE = AST DEC CLASS ID LBRACE fieldList RBRACE */
        /**********************************/
        System.out.print("AST NODE: " + this.getName() + "\n");

        /***********************************/
        /* RECURSIVELY PRINT CHILDREN */
        /***********************************/

        if (childrenExist) {
            children.forEach(child -> child.PrintMe());
        }

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("%s",this.nodeContent.replace('"','\'')));

        if (childrenExist) {
            children.forEach(child -> AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, child.SerialNumber));
        }
    }

    public void setNodeContent(String nodeText) { this.nodeContent = this.getName()+"\n"+nodeText; }

    public void printErrorAndExit(String errMessage){
        ErrorHandler.printErrorLineAndExit(errMessage,this.line);
    }

    public TEMP IRme() { return null; }

}
