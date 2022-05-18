package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author juncheng ma
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = new File(".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        if (!Dog.DOG_FOLDER.exists()) {
            Dog.DOG_FOLDER.mkdir();
        }
        File f =new File(".capers/story.txt");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File f =new File(".capers/story.txt");
        String text1 = readContentsAsString(f);
        writeContents(f,text1,text,"\n");
        System.out.print(text1+text+"\n");
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog j = new Dog(name,breed,age);
        File a = Utils.join(".capers","dogs",name+".txt");
        try {
            writeObject(a,j);
            a.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(j.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog a = Dog.fromFile(name);
        a.haveBirthday();
        a.saveDog();
    }
}
