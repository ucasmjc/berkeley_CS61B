package gitlet;

import javax.management.StringValueExp;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class addstructure implements Serializable {
    public TreeMap<String,String> added;
    public Map<String, String> x;
    public addstructure() {
        added =new TreeMap<>();
    }
    public void add(String k, String val) {
        added.put(k,val);
    }
}
