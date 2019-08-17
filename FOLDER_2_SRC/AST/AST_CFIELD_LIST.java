package AST;

import TEMP.TEMP;
import TYPES.TYPE_LIST;

public class AST_CFIELD_LIST extends AST_NODE_RULE {
    AST_CFIELD head;
    AST_CFIELD_LIST tail;

    public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail) {
        super(head, tail);
        this.head = head;
        this.tail = tail;
    }

    public AST_CFIELD_LIST(AST_CFIELD head) {
        super(head);
        this.head = head;
    }

    public TYPE_LIST SemantMe() {
        if (tail == null) {
            return new TYPE_LIST(
                    head.SemantMe(),
                    null);
        } else {
            return new TYPE_LIST(
                    head.SemantMe(),
                    tail.SemantMe());
        }
    }


    public TEMP IRme() {
        if (head != null)
            if(head.funcd != null)head.IRme();
        if (tail != null) tail.IRme();
        return null;
    }

    @Override
    public String getName() {
        return "CFIELD LIST";
    }
}
