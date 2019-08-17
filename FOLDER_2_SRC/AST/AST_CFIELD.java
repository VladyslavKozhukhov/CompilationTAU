package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public class AST_CFIELD extends AST_NODE_RULE {

    AST_VARD vard;
    AST_FUNCD funcd;
    int caseNum;

    public AST_CFIELD(AST_VARD vd){
        super(vd);
        vard=vd;
        this.caseNum=0;
    }

    public AST_CFIELD(AST_FUNCD fd){
        super(fd);
        funcd=fd;
        this.caseNum=1;
    }

    public TYPE SemantMe() {
        if(caseNum==0){
            return vard.SemantMe();

        }else{
            return funcd.SemantMe();
        }
    }

    public TEMP IRme(){
        if(caseNum==0){
            return vard.IRme();

        }else{
            return funcd.IRme();
        }
    }

    @Override
    public String getName() {
        return "CFIELD";
    }
}
