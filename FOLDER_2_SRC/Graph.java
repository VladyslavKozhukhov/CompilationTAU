import java.util.*;

class Graph {

    private ArrayList<ArrayList<Integer>> adj;
    private HashMap<Integer, Integer> tempToRegMap;
    private int numOfVertices;

    Graph(int numOfVertices) {
        this.numOfVertices = numOfVertices;
        tempToRegMap = new HashMap<>();
        adj = new ArrayList<>();
        for (int i = 0; i < numOfVertices; ++i)
            adj.add(new ArrayList<>());
    }

    HashMap<Integer, Integer> greedyColoring() {
        boolean[] availability = initAvailabilityArray();
        Integer[] res = initResultArray();
        for (int vertex = 1; vertex < numOfVertices; vertex++) {
            for(Integer i : adj.get(vertex)){
                if(res[i]!=null){
                    availability[res[i]] = false;
                }
            }
            int reg;
            for (reg = 0; reg < numOfVertices; reg++) {
                if (availability[reg])
                    break;
            }
            res[vertex] = reg;
            Arrays.fill(availability, true);
        }
        for (int i = 0; i < numOfVertices; i++) {
            tempToRegMap.put(i, res[i]);
        }
        return tempToRegMap;
    }

    private boolean[] initAvailabilityArray() {
        boolean available[] = new boolean[numOfVertices];
        Arrays.fill(available, true);
        return available;
    }

    private Integer[] initResultArray() {
        Integer result[] = new Integer[numOfVertices];
        Arrays.fill(result, null);
        result[0] = 0;
        return result;
    }

    void addEdge(int v, int w) {
        adj.get(v).add(w);
        adj.get(w).add(v);
    }

}

