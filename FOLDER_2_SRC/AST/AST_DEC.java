package AST;

import TEMP.TEMP;
import TYPES.TYPE;

public class AST_DEC extends AST_NODE_RULE {

    public AST_VARD vd;
    public AST_FUNCD fd;
    public AST_CLASSD cd;
    public AST_ARRAYD ad;
    public int caseNum;

    public AST_DEC(AST_VARD vd){
        super(vd);
        this.vd = vd;
        this.caseNum=0;
    }

    public AST_DEC(AST_FUNCD fd){
        super(fd);
        this.fd = fd;
        this.caseNum=1;
    }

    public AST_DEC(AST_CLASSD cd){
        super(cd);
        this.cd = cd;
        this.caseNum=2;
    }

    public AST_DEC(AST_ARRAYD ad){
        super(ad);
        this.ad = ad;
        this.caseNum=3;
    }

    public TYPE SemantMe()
    {
        switch (caseNum){
            case 0:
                vd.SemantMe();
                break;
            case 1:
                fd.SemantMe();
                break;
            case 2:
                cd.SemantMe();
                break;
            case 3:
                ad.SemantMe();
                break;
        }
        return null;
    }

    public TEMP IRme()
    {
        switch (caseNum){
            case 0:
                vd.IRme();
                break;
            case 1:
                fd.IRme();
                break;
            case 2:
                cd.IRme();
                break;
            case 3:
                ad.IRme();
                break;
        }
        return null;
    }

    @Override
    public String getName() {
        return "DEC";
    }
}
