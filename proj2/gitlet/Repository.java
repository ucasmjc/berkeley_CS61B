package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.*;
import static gitlet.Utils.*;
import static gitlet.Utils.writeContents;


/** Represents a gitlet repository.

 *  does at a high level.
 *
 *  @author JunCheng Ma
 */
public class Repository {
    /**

     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    private static File stage = new File(".gitlet/stage");
    private static File commit = new File(".gitlet/commit");
    private static File content = new File(".gitlet/content");
    private static File committree = new File(".gitlet/committree");
    private static File addpart = new File(".gitlet/stage/addpart");
    private static File removepart = new File(".gitlet/stage/removepart");
    private static String commitPath = ".gitlet/commit/";
    private static class Node {
        Commit key;
        int value;
        Node(Commit x, int y) {
            key = x;
            value = y;
        }
    }
    private static void commitinit() {
        CommitTree tr = new CommitTree();
        try {
            writeObject(committree, tr);
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
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
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
            if (present.getDocument().containsKey(x)) {
                if (sha1(data).equals(present.getDocument().get(x))) {
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
    public static void commit(String mes, String parent) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        Commit next = new Commit(mes);
        next.setDocument((TreeMap<String, String>) present.getDocument().clone());
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
            TreeMap fridge = next.getDocument();
            fridge.put(q, x);
            next.setDocument(fridge);
            join(".gitlet", "stage", "addpart", q).delete();
            if (!presentTree.getFileset().contains(x)) {
                File a = new File(".gitlet/content/" + x);
                writeContents(a, y);
                createFile(a);
                presentTree.addSet(x);
            }
        }
        for (File i : removed) {
            String q = i.getName();
            TreeMap fridge = next.getDocument();
            fridge.remove(q);
            next.setDocument(fridge);
            join(".gitlet/stage/removepart/" + q).delete();
        }
        next.setParent1(parent);
        presentTree.add(next);
        presentTree.HEAD = next.getShaCode();
        writeObject(committree, presentTree);
    }
    public static void remove(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        boolean mrak = join(".gitlet/stage/addpart", x).delete();
        TreeMap<String, String> fridge = present.getDocument();
        String y = fridge.remove(x);
        present.setDocument(fridge);
        if (y == null) {
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
        if (x.getParent() == null) {
            return null;
        }
        return readObject(new File(commitPath + x.getParent()), Commit.class);
    }
    public static void log() {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        while (present != null) {
            System.out.println("===");
            System.out.println("commit " + present.getShaCode());
            System.out.println("Date: " + present.getTime());
            System.out.println(present.getMessage());
            System.out.println();
            present = getparent(present);
        }
    }
    public static void globallog() {
        File[] x = commit.listFiles();
        for (File i : x) {
            Commit y = readObject(i, Commit.class);
            System.out.println("===");
            System.out.println("commit " + y.getShaCode());
            System.out.println("Date: " + y.getTime());
            System.out.println(y.getMessage());
            System.out.println();
        }
    }
    public static void find(String x) {
        File[] s = commit.listFiles();
        boolean mark = true;
        for (File i : s) {
            Commit y = readObject(i, Commit.class);
            if (y.getMessage().equals(x)) {
                System.out.println(y.getShaCode());
                mark = false;
            }
        }
        if (mark) {
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
        File[] list = new File(commitPath).listFiles();
        File a = new File(commitPath + id);
        for (File i : list) {
            if (i.getName().startsWith(id)) {
                a = i;
            }
        }
        if (!a.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Commit present = readObject(a, Commit.class);
        String y = present.getDocument().get(name);
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
        checkout(name, id);
    }
    private static void filewash(CommitTree presentTree, String newer, boolean mark) {
        for (File i : addpart.listFiles()) {
            i.delete();
        }
        for (File i : removepart.listFiles()) {
            i.delete();
        }
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        Commit next = readObject(new File(commitPath + newer), Commit.class);
        List<String> filelist = plainFilenamesIn(CWD);
        for (String i : filelist) {
            File a = new File(i);
            String x = present.getDocument().get(i);
            if (x == null && (next.getDocument().get(i) != null
                    || !Objects.equals(next.getDocument().get(i), sha1(readContents(a)))) && mark) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            } else {
                a.delete();
            }
        }
        presentTree.HEAD = newer;
        for (String i : next.getDocument().keySet()) {
            File x = new File(i);
            writeContents(x, (Object) readContents(new File(".gitlet/content/"
                    + next.getDocument().get(i))));
        }
        writeObject(committree, presentTree);
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
        List<String> filelist = plainFilenamesIn(CWD);
        Commit present = readObject(new File(commitPath + presentTree.HEAD), Commit.class);
        Commit next = readObject(new File(commitPath + newer), Commit.class);
        for (String i : filelist) {
            File a = new File(i);
            String x = present.getDocument().get(i);
            if (x == null && (next.getDocument().get(i) != null
                    || !Objects.equals(next.getDocument().get(i),
                    sha1(readContents(a))))) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }
        filewash(presentTree, newer, true);
    }
    public static void branch(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        CommitTree.Branch newer = new CommitTree.Branch(x, presentTree.HEAD);
        File head = new File(commitPath + presentTree.HEAD);
        Commit present = readObject(head, Commit.class);
        present.setSplited(true);
        if (presentTree.branchset.containsKey(x)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
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
        writeObject(committree, presentTree);
    }
    public static void reset(String x) {
        CommitTree presentTree = readObject(committree, CommitTree.class);
        File a = new File(commitPath + x);
        if (!a.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        presentTree.present.head = x;
        writeObject(committree, presentTree);
        filewash(presentTree, x, true);
    }
    private static void conflict(String next, String b, String c) {
        File waitt = join(".gitlet", "stage", "addpart", next);
        String[] bb = null;
        String[] cc = null;
        File w = new File(next);
        if (b != null) {
            bb = readContentsAsString(join(".gitlet/content/", b)).split("\n");
        } else if (w.exists()) {
            CommitTree presentTree = readObject(committree, CommitTree.class);
            CommitTree.Branch present = presentTree.present;
            filewash(presentTree, present.head, false);
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }
        if (c != null) {
            cc = readContentsAsString(join(".gitlet/content/", c)).split("\n");
        }
        String mes = "<<<<<<< HEAD\n";
        if (b != null) {
            for (int j = 0; j < bb.length; j++) {
                if (cc == null || j >= cc.length) {
                    mes += bb[j] + "\n";
                    continue;
                }
                if (bb[j] != cc[j]) {
                    mes += bb[j] + "\n";
                }
            }
        }
        mes += "=======\n";
        if (c != null) {
            for (int j = 0; j < cc.length; j++) {
                if (bb == null || j >= bb.length) {
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
        if (added.length != 0 || removed.length != 0) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
    }
    private static HashSet help3(Commit q) {
        HashSet givenSplited = new HashSet<String>();
        while (true) {
            if (q.getSplited()) {
                givenSplited.add(q.getShaCode());
            }
            if (q.getParent() == null) {
                break;
            }
            if (q.getParent1() != null) {
                HashSet<String> subSplited = help3(readObject(new
                        File(commitPath + q.getParent1()), Commit.class));
                for (String i : subSplited) {
                    givenSplited.add(i);
                }
            }
            q = readObject(new File(commitPath + q.getParent()), Commit.class);
        }
        return givenSplited;
    }
    private static Node help4(Commit p, HashSet givenSplited) {
        Commit split = null;
        int i = 0;
        int j = 1000;
        while (true) {
            if (p.getSplited()) {
                if (givenSplited.contains(p.getShaCode())) {
                    if (i < j) {
                        j = i;
                        split = p;
                    }
                }
            }
            if (p.getParent() == null) {
                break;
            }
            if (p.getParent1() != null) {
                Node kv = help4(readObject(new File(commitPath + p.getParent1()),
                        Commit.class), givenSplited);
                if (i + kv.value < j) {
                    j = i + kv.value;
                    split = kv.key;
                }
            }
            i += 1;
            p = readObject(new File(commitPath + p.getParent()), Commit.class);
        }
        return new Node(split, j);
    }
    private static Commit getsplit(Commit p, Commit q) {
        HashSet givenSplited = help3(p);
        Node kv = help4(q, givenSplited);
        return kv.key;
    }
    private static void help1(String c, String next) {
        File qq = new File(next);
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
    }
    private static void help2(String c, String next, CommitTree presentTree,
                              CommitTree.Branch present) {
        File pp = new File(".gitlet/content/" + c);
        File qq = new File(next);
        if (qq.exists()) {
            filewash(presentTree, present.head, false);
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }
        byte[] ppp = Utils.readContents(pp);
        writeContents(qq, ppp);
        File waitt = join(".gitlet", "stage", "addpart", next);
        writeContents(waitt, ppp);
    }
    public static void merge(String x) {
        check();
        boolean mark = false;
        CommitTree presentTree = readObject(committree, CommitTree.class);
        CommitTree.Branch present = presentTree.present;
        CommitTree.Branch given = presentTree.branchset.get(x);
        if (given == null) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (given == present) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        Commit used = readObject(new File(commitPath + present.head), Commit.class);
        Commit gived = readObject(new File(commitPath + given.head), Commit.class);
        Commit p = gived;
        Commit q = used;
        Commit split = getsplit(p, q);
        if (split.equals(gived)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }
        if (split.equals(used)) {
            chenkout3(x);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        TreeMap<String, String> splitTree = split.getDocument();
        TreeMap<String, String> usedTree = used.getDocument();
        TreeMap<String, String> givedTree = gived.getDocument();
        Iterator<String> iterator = splitTree.keySet().iterator();
        while (iterator.hasNext()) {
            String next =  iterator.next();
            String a = splitTree.get(next);
            String b = usedTree.remove(next);
            String c = givedTree.remove(next);
            if ((Objects.equals(a, b) && Objects.equals(b, c)) || Objects.equals(a, c)
                    || Objects.equals(b, c)) {
                continue;
            } else if (a.equals(b)) {
                help1(c, next);
            } else {
                conflict(next, b, c);
                mark = true;
            }
        }
        Iterator<String> iterator1 = usedTree.keySet().iterator();
        while (iterator1.hasNext()) {
            String next =  iterator1.next();
            String b = usedTree.get(next);
            String c = givedTree.remove(next);
            if (b == c || c == null) {
                continue;
            } else {
                conflict(next, b, c);
                mark = true;
            }
        }
        Iterator<String> iterator2 = givedTree.keySet().iterator();
        while (iterator2.hasNext()) {
            String next =  iterator2.next();
            String c = givedTree.get(next);
            help2(c, next, presentTree, present);
        }
        commit("Merged " + x + " into " + presentTree.present.name + ".", gived.getShaCode());
        if (mark) {
            System.out.println("Encountered a merge conflict.");
        }

    }
}
