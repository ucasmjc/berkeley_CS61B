package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File stage = new File(".gitlet/stage");
    public static final File commit = new File(".gitlet/commit");
    public static final File content = new File(".gitlet/content");
    public static final File committree = new File(".gitlet/committree");
    public static final File addcontent = join(".gitlet", "commit","addcontent");
    public static final File addpart = new File(".gitlet/stage/addpart");
    public static final File removepart = new File(".gitlet/stage/removepart");
    public static final String commitPath = ".gitlet/commit/";
    /* TODO: fill in the rest of this class. */
    private static void commitinit() {
        CommitTree tr = new CommitTree();
        try {
            writeObject(committree,tr);
            committree.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void init() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            stage.mkdir();
            content.mkdir();
            commit.mkdir();
            addpart.mkdir();
            removepart.mkdir();
            commitinit();
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
    }
    public static void createFile(File x) {
        try {
            x.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void add(String x) {//与当前commit对比
        File presentfile = new File(x);
        if (presentfile.exists()) {
            boolean mark = true;
            CommitTree presentTree = readObject(committree, CommitTree.class);
            Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
            File[] added = stage.listFiles();
            File wait = join(".gitlet", "stage", "addpart", x);
            byte[] data = readContents(presentfile);
            if (present.document.containsKey(x)) {
                if (sha1(data).equals(present.document.get(x))) {
                    return;
                }
            }
            writeContents(wait, data);
            if (!wait.exists()) {
                createFile(wait);
            }
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }
    public static void commit(String mes) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        Commit next = new Commit(mes);
        next.document = (TreeMap<String, String>) present.document.clone();
        File[] added = addpart.listFiles();
        File[] removed = removepart.listFiles();
        if (added.length == 0 && removed.length == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        for (File i : added) {//假设都是添加的
            byte[] y = readContents(i);
            String x = sha1(y);
            String q = i.getName();
            next.document.put(q, x);
            join(".gitlet","stage","addpart", q).delete();
            if (!presentTree.fileset.contains(x)) {
                File a = new File(".gitlet/content/" + x);
                writeContents(a, y);
                createFile(a);
                presentTree.fileset.add(x);
            }
        }
        for (File i : removed) {
            String q = i.getName();
            next.document.remove(q);
            join(".gitlet/stage/removepart/" + q).delete();
        }
        presentTree.add(next);
        presentTree.HEAD = next.shaCode;
        writeObject(committree, presentTree);
    }
    public static void remove(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        boolean mrak = (new File(".gitlet/stage/addpart" + x)).delete();
        String y = present.document.remove(x);
        if (y.equals(null)) {
            if (!mrak) {
                System.out.println("No reason to remove the file.");
                return;
            }
        } else {
            File wait = new File(".gitlet/stage/removepart/" + x);
            File e = new File(".gitlet/content/" + y);
            writeContents(wait, readContents(e));
        }
    }
    private static Commit getparent(Commit x) {
        if (x.parent == null) {
            return null;
        }
        return readObject(new File(commitPath + x.parent), Commit.class);
    }
    public static void log() {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        while (present != null) {
            System.out.println("===");
            System.out.println("commit " + present.shaCode);
            System.out.println("Date: " + present.time);
            System.out.println(present.message);
            System.out.println();
            present = getparent(present);
        }
    }
    public static void globallog() {
        File[] x = commit.listFiles();
        for (File i : x) {
            Commit y = readObject(i, Commit.class);
            System.out.println("===");
            System.out.println("commit " + y.shaCode);
            System.out.println("Date:" + y.time);
            System.out.println(y.message);
            System.out.println();
        }
    }
    public static void find(String x) {
        File[] s = commit.listFiles();
        boolean mark = true;
        for (File i : s) {
            Commit y = readObject(i, Commit.class);
            if (y.message.equals(x)) {
                System.out.println(y.shaCode);
                mark = false;
            }
        }
        if(mark) {
            System.out.println("Found no commit with that message.");
        }
    }
    public static void status() {

    }
    private static void checkout(String name, String id) {
        File a = new File(commitPath + id);
        if (!a.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit present = readObject(a, Commit.class);
        String y = present.document.get(name);
        if (y == null) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            File p = new File(".gitlet/content/" + y);
            File q = new File(name);
            writeContents(q, readContents(p));
        }
    }
    public static void checkout1(String name) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        checkout(name, presentTree.HEAD);
    }
    public static void checkout2(String id, String name) {
        checkout(name,id);
    }
    public static void chenkout3(String name) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        if (!presentTree.branchset.containsKey(name)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (name.equals(presentTree.present.name)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        List<String> filelist = plainFilenamesIn(CWD);
        for (String i : filelist) {
            String x = present.document.get(i);
            if (x == null) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first." + i);
                System.exit(0);
            } else {
                File a = new File(i);
                if (!x.equals(sha1(readContents(a)))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first." + i);
                    System.exit(0);
                }
                a.delete();
            }
        }
        presentTree.present = presentTree.branchset.get(name);
        String newer = presentTree.present.head;
        presentTree.HEAD = newer;
        Commit next = readObject(new File(commitPath + newer), Commit.class);
        for (String i : next.document.keySet()) {
            File x = new File(i);
            writeContents(x, readContents(new File(".gitlet/content/" + next.document.get(i))));
        }
        for (File i : addpart.listFiles()) {
            i.delete();
        }
        for (File i : removepart.listFiles()) {
            i.delete();
        }
        writeObject(committree,presentTree);
    }
    public static void branch(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        CommitTree.branch newer = new CommitTree.branch(x, presentTree.HEAD);
        presentTree.branchset.put(x, newer);
        writeObject(committree,presentTree);
    }
    public static void rmbranch(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        if (!presentTree.branchset.containsKey(x)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (presentTree.present.equals(presentTree.branchset.get(x))) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        presentTree.branchset.remove(x);
        writeObject(committree,presentTree);
    }
    public static void reset(String x) {

    }

}
