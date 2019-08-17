package AST;

public class AST_TYPE_NAME extends AST_NODE_RULE {
    public String type;
    public String name;

    public AST_TYPE_NAME(String type, String name) {
        super();
        setNodeContent(String.format("name:%s type:%s", name, type));
        this.type = type;
        this.name = name;
    }

    @Override
    public String getName() {
        return "TYPE NAME";
    }
}
