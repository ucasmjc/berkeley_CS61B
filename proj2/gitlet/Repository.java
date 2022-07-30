package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.*;
import static gitlet.Utils.*;
import static gitlet.Utils.writeContents;
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
    public static void add(String x) {
        File presentfile = new File(x);
        if (presentfile.exists()) {
            boolean mark = true;
            CommitTree presentTree = readObject(committree, CommitTree.class);
            Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
            File[] added = stage.listFiles();
            File wait = join(".gitlet", "stage", "addpart", x);
            File test = join(".gitlet", "stage", "removepart", x);
            byte[] data = readContents(presentfile);
            if (test.exists()) {
                test.delete();
                return;
            }
            if (present.document.containsKey(x)) {
                if (sha1(data).equals(present.document.get(x))) {
                    wait.delete();
                    return;
                }
            }
            writeContents(wait, data);
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
        for (File i : added) {
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
        boolean mrak = join(".gitlet/stage/addpart" , x).delete();
        String y = present.document.remove(x);
        if (y==null) {
            if (!mrak) {
                System.out.println("No reason to remove the file.");
                return;
            }
        } else {
            File a = new File(x);
            a.delete();
            File wait = new File(".gitlet/stage/removepart/" + x);
            if (!wait.exists()) {
                createFile(wait);
            }
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
            System.out.println("Date: " + y.time);
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
        CommitTree presentTree = readObject(committree, CommitTree.class);
        String present = presentTree.present.name;
        System.out.println("=== Branches ===");
        for (String i : presentTree.branchset.keySet()) {
            if (present.equals(i)) {
                System.out.println("*" + i);
            } else {
                System.out.println(i);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String i : plainFilenamesIn(".gitlet/stage/addpart")) {
            System.out.println(i);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String i : plainFilenamesIn(".gitlet/stage/removepart")) {
            System.out.println(i);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
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
            writeContents(q, Utils.readContents(p));
        }
    }
    public static void checkout1(String name) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        checkout(name, presentTree.HEAD);
    }
    public static void checkout2(String id, String name) {
        checkout(name,id);
    }
    private static void filewash (CommitTree presentTree, String newer) {
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        Commit next = readObject(new File(commitPath + newer), Commit.class);
        List<String> filelist = plainFilenamesIn(CWD);
        for (String i : filelist) {
            String x = present.document.get(i);
            if (x == null && next.document.get(i) != null) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first." + i);
                System.exit(0);
            } else {
                File a = new File(i);
                if (!Objects.equals(x, sha1(readContents(a)))) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first." + i);
                    System.exit(0);
                }
                a.delete();
            }
        }
        presentTree.HEAD = newer;

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
        presentTree.present = presentTree.branchset.get(name);
        String newer = presentTree.present.head;
        filewash(presentTree, newer);
    }
    public static void branch(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        CommitTree.branch newer = new CommitTree.branch(x, presentTree.HEAD);
        File head = new File(commitPath + presentTree.HEAD);
        Commit present = readObject(head, Commit.class);
        present.splited = true;
        presentTree.branchset.put(x, newer);
        writeObject(committree, presentTree);
        writeObject(head, present);
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
        CommitTree presentTree = readObject(committree, CommitTree.class);
        File a = new File(commitPath + x);
        if (!a.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        presentTree.present.head = x;
        filewash(presentTree, x);
    }
    private static void conflict (String next, String b, String c) {
        File waitt = join(".gitlet", "stage", "addpart", next);
        String[] bb = null;
        String[] cc = null;
        if (b != null) {
            bb = readContentsAsString(join(".gitlet/content/", b)).split("\n");
        }
        if (c != null) {
            cc = readContentsAsString(join(".gitlet/content/", c)).split("\n");
        }
        String mes = "<<<<<<< HEAD\n";
        if (b != null) {
            for (int j = 0; j < bb.length; j++) {
                if (cc == null) {
                    mes += bb[j] + "\n";
                    continue;
                }
                if (bb[j] != cc[j]) {//cc不一定这么长
                    mes += bb[j] + "\n";

                }
            }
        }
        mes += "=======\n";
        if (c != null) {
            for (int j = 0; j < cc.length; j++) {
                if (bb == null) {
                    mes += cc[j] + "\n";
                    continue;
                }
                if (bb[j] != cc[j]) {
                    mes += cc[j] + "\n";
                }
            }
        }
        mes += ">>>>>>>\n";
        File nextFile = new File(next);
        writeContents(nextFile, mes);
        writeContents(waitt, mes);
    }
    private static void check() {
        File[] added = addpart.listFiles();
        File[] removed = removepart.listFiles();
        if (added.length != 0 || removed.length != 0){
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }
    public static void merge(String x) {
        check();
        boolean mark = false;
        CommitTree presentTree = readObject(committree, CommitTree.class);
        CommitTree.branch present = presentTree.present;
        CommitTree.branch given = presentTree.branchset.get(x);
        if (given == null) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (given == present) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        HashSet givenSplited = new HashSet<String>();
        Commit used = readObject(new File(commitPath + present.head), Commit.class);
        Commit gived = readObject(new File(commitPath + given.head), Commit.class);
        Commit p = gived;
        Commit q = used;
        Commit split = null;
        while (true) {
            if (q.splited) {
                givenSplited.add(q.shaCode);
            }
            if (q.parent == null) {
                break;
            }
            q = readObject(new File(commitPath + q.parent), Commit.class);
        }
        while (true) {
            if (p.splited) {
                if (givenSplited.contains(p.shaCode)) {
                    split = p;
                    break;
                }
            }
            if (p.parent == null) {
                break;
            }
            p = readObject(new File(commitPath + p.parent), Commit.class);
        }
        if (split.equals(gived)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (split.equals(used)) {
            chenkout3(x);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        Iterator<String> iterator = split.document.keySet().iterator();
        while (iterator.hasNext()) {
            String next =  iterator.next();
            String a = split.document.get(next);
            String b = used.document.remove(next);
            String c = gived.document.remove(next);
            File qq = new File(next);
            if ((Objects.equals(a, b) && Objects.equals(b, c)) || Objects.equals(a, c) || Objects.equals(b, c)) {
                continue;
            } else if (a.equals(b)) {
                if (c == null) {
                    File y = new File(next);
                    y.delete();
                    File wait = new File(".gitlet/stage/removepart/" + next);
                    if (!wait.exists()) {
                        createFile(wait);
                    }
                } else {
                    File pp = new File(".gitlet/content/" + c);
                    byte[] ppp = Utils.readContents(pp);
                    writeContents(qq, ppp);
                    File waitt = join(".gitlet", "stage", "addpart", next);
                    writeContents(waitt, ppp);
                }
            } else {

                conflict(next, b, c);
                mark = true;
            }
        }
        Iterator<String> iterator1 = used.document.keySet().iterator();
        while (iterator1.hasNext()) {
            String next =  iterator1.next();
            String b = used.document.remove(next);
            String c = gived.document.remove(next);
            if (b == c || c == null) {
                continue;
            } else {
                conflict(next, b, c);
                mark = true;
            }
        }
        Iterator<String> iterator2 = gived.document.keySet().iterator();
        while (iterator2.hasNext()) {
            String next =  iterator2.next();
            String c = gived.document.remove(next);
            File pp = new File(".gitlet/content/" + c);
            File qq = new File(next);
            byte[] ppp = Utils.readContents(pp);
            writeContents(qq, ppp);
            File waitt = join(".gitlet", "stage", "addpart", next);
            writeContents(waitt, ppp);
        }
        commit("Merged " + x + " into " + presentTree.present.name + ".");
        if (mark) {
            System.out.println("Encountered a merge conflict.");
        }
    }
}
