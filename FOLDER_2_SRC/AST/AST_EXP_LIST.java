package AST;

import TEMP.*;
import TYPES.TYPE_LIST;

public class AST_EXP_LIST extends AST_NODE_RULE {

    private AST_EXP head;
    private AST_EXP_LIST tail;

    public AST_EXP_LIST(AST_EXP e, AST_EXP_LIST el){
        super(e,el);
        this.head = e;
        this.tail = el;
    }

    public AST_EXP_LIST(AST_EXP e){
        super(e);
        this.head = e;
    }

    public TYPE_LIST SemantMe()
    {
        if (tail == null)
        {
            return new TYPE_LIST(
                    head.SemantMe(),
                    null);
        }
        else
        {
            return new TYPE_LIST(
                    head.SemantMe(),
                    tail.SemantMe());
        }
    }


    public TEMP_LIST IRme_List()
    {
        if (tail==null){
            return new TEMP_LIST(head.IRme(),null);
        }else {
            return new TEMP_LIST(head.IRme(),tail.IRme_List());
        }
    }

    @Override
    public String getName() {
        return "EXP LIST";
    }
}
