package AST;

public class AST_TYPE_NAME_LIST extends AST_NODE_RULE {

    AST_TYPE_NAME head;
    AST_TYPE_NAME_LIST tail;

    public AST_TYPE_NAME_LIST(AST_TYPE_NAME tName, AST_TYPE_NAME_LIST tNameList) {
        super(tName, tNameList);
        this.head = tName;
        this.tail = tNameList;
    }

    public AST_TYPE_NAME_LIST(AST_TYPE_NAME tName) {
        super(tName);
        this.head = tName;
    }

    @Override
    public String getName() {
        return "TYPE NAME LIST";
    }
}
