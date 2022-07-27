package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                // TODO: handle the `init` command
                break;
            case "add":
                Repository.add(args[1]);
                break;
                // TODO: handle the `add [filename]` command
            case "commit":
                if (args[1].length()==0) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globallog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status" :
                Repository.status();
                break;
            case "checkout":
                if (args[1].substring(0,2)=="--") {
                    Repository.checkout1(args[1].substring(2));
                } else if (args[1].indexOf("--")>0) {
                    Repository.checkout2(args[1], args[3]);
                } else {
                    Repository.chenkout3(args[1]);
                }
                break;
            case "branch":
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                Repository.rmbranch(args[1]);
                break;
            case "reset":
                Repository.reset(args[1]);
                break;
            case "merge":
                Repository.merge(args[1]);
                break;
            // TODO: FILL THE REST IN
        }
    }
}
