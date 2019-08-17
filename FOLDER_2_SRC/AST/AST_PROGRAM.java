package AST;

import IR.*;
import TEMP.*;
import TYPES.*;

import java.util.ArrayList;
import java.util.List;

public class AST_PROGRAM extends AST_NODE_RULE {

    public AST_DEC head;
    public AST_PROGRAM tail;
    public static List<String> globalsList;

    public AST_PROGRAM(AST_DEC head, AST_PROGRAM tail) {
        super(head, tail);
        this.head = head;
        this.tail = tail;
        globalsList = new ArrayList<>();

    }

    public AST_PROGRAM(AST_DEC head) {
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

    public TEMP IRme() {
        if (head != null) head.IRme();
        if (tail != null) tail.IRme();
        return null;
    }

    @Override
    public String getName() {
        return "PROGRAM";
    }
}
