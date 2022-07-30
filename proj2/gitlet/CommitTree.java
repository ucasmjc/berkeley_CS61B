package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.*;

import static gitlet.Utils.writeObject;
public class CommitTree implements Serializable {
    private Set<String> fileset;
    public Set<String> getFileset() {
        return fileset;
    }
    public void addSet(String x) {
        this.fileset.add(x);
    }
    public static class Branch implements Serializable {
        String name;
        String head;
        public Branch(String na, String he) {
            name = na;
            head = he;
        }
    }


    String HEAD;
    TreeMap<String, Branch> branchset;
    Branch present;
    /*public CommitTree(){
        fileset = new HashSet<>();
        Commit first = new Commit("initial commit");
        sentinel = new Node(Utils.sha1(serialize(first)), first);
        set = new TreeMap<>();
        HEAD = sentinel;
        present = new branch("master", sentinel);
        set.put("master", present);
    }*/
    public CommitTree() {
        fileset = new HashSet<>();
        Commit first = new Commit("initial commit");
        branchset = new TreeMap<>();
        present = new Branch("master", first.shaCode());
        branchset.put("master", present);
        HEAD = present.head;
        createcomfile(first);
    }
    public void createcomfile(Commit y) {
        File x = new File(".gitlet/commit/" + y.getShaCode());
        writeObject(x, y);
    }
    public void add(Commit com) {
        com.setParent(HEAD);
        com.time();
        com.shaCode();
        createcomfile(com);
        HEAD = com.getShaCode();
        present.head = HEAD;
    }
    /*public Commit get(String sha) {
        Commit tar = sentinel.get(sha);
        if (tar.equals(null)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        return tar;
    }*/
}
