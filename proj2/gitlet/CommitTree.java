package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.*;

import static gitlet.Utils.serialize;
import static gitlet.Utils.writeObject;
import static gitlet.Repository.createFile;
public class CommitTree implements Serializable {
    public Set<String> fileset;
    public static class branch implements Serializable {
        String name;
        String head;
        public branch(String na, String he) {
            name = na;
            head = he;
        }
    }


    String HEAD;//永远指向当前的
    TreeMap<String, branch> branchset;
    branch present;
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
        present = new branch("master", first.shaCode());
        branchset.put("master", present);
        HEAD = present.head;
        createcomfile(first);
    }
    public void createcomfile(Commit y) {
        File x = new File(".gitlet/commit/" + y.shaCode);
        writeObject(x, y);
    }
    public void add(Commit com) {//没考虑分支
        com.parent = HEAD;
        com.time();
        com.shaCode();
        createcomfile(com);
        HEAD = com.shaCode;
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
