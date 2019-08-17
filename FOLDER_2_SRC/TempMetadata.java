import java.util.HashSet;

public class TempMetadata {

    int tempIdx;
    HashSet<Integer> interSet;
    int start;
    int end;

    public TempMetadata(int tempIdx, int start, int end) {
        this.tempIdx = tempIdx;
        interSet = new HashSet<>();
        this.start = start;
        this.end = end;
    }

    public boolean equals(TempMetadata other){
        return this.tempIdx==other.tempIdx;
    }

    public boolean isInter(TempMetadata t){
        return (t.start >= start && t.start <= end) || (start >= t.start && start <= t.end) || (end >= t.start && end <= t.end) || (t.end >= start && t.end <= end);
    }
}
