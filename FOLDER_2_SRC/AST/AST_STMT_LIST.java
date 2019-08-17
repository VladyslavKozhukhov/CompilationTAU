package AST;

import TEMP.TEMP;
import TYPES.*;


public class AST_STMT_LIST extends AST_NODE_RULE {
    public AST_STMT head;
    public AST_STMT_LIST tail;

    public AST_STMT_LIST(AST_STMT head, AST_STMT_LIST tail) {
        super(head, tail);
        this.head = head;
        this.tail = tail;
    }

    public AST_STMT_LIST(AST_STMT head) {
        super(head);
        this.head = head;
    }

    public TYPE SemantMe() {
        if (this.head != null) {
            this.head.SemantMe();
        }
        if (this.tail != null) {
            this.tail.SemantMe();
        }
        return null;
    }

    public TEMP IRme()
    {
        if (head != null) head.IRme();
        if (tail != null) tail.IRme();
        return null;
    }

    @Override
    public String getName() {
        return "STMT LIST";
    }
}

