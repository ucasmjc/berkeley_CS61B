package gitlet;

import java.util.Objects;

import static gitlet.Repository.GITLET_DIR;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author JunCheng Ma
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        if ((!GITLET_DIR.exists()) && !Objects.equals(firstArg, "init")) {
            System.out.println(firstArg);
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
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
                int length = args.length;
                if (args[1].equals("--")) {
                    Repository.checkout1(args[2]);
                } else if (args.length == 2) {
                    Repository.chenkout3(args[1]);
                } else if (args.length == 4){
                    if (args[2].equals("--")) {
                        Repository.checkout2(args[1], args[3]);
                    } else {
                        System.out.println("Incorrect operands.");
                    }
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
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
