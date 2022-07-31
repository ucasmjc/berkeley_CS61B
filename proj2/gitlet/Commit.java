package gitlet;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import java.util.Date;
/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author JunCheng Ma
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private String time;
    private String shaCode;
    private String parent;
    private boolean splited;
    private TreeMap<String, String> document;
    private String parent1;
    public String getParent1() {
        return parent1;
    }
    public void setParent1(String x) {
        this.parent1 = x;
    }
    public String getMessage() {
        return message;
    }
    public TreeMap<String, String> getDocument() {
        return document;
    }
    public void setSplited(boolean x) {
        this.splited = x;
    }
    public void setDocument(TreeMap x) {
        this.document = x;
    }
    public String getTime() {
        return time;
    }
    public String getShaCode() {
        return shaCode;
    }
    public String getParent() {
        return parent;
    }
    public boolean getSplited() {
        return splited;
    }
    public void setParent(String x) {
        this.parent = x;
    }
    public Commit(String mes) {
        parent = null;
        message = mes;
        time = "Mon Jan 1 08:00:00 1970 +0800";
        document = new TreeMap<>();
        splited = false;
        parent1 = null;
    }
    public String shaCode() {
        shaCode = Utils.sha1(Utils.serialize(this));
        return shaCode;
    }
    public String time() {
        Date now = new Date();
        DateFormat B = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
        time = B.format(now) + " +0800";
        return time;
    }
}
