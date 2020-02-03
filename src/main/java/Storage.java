import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represent a storage object to handle storing of the list into files
 */
public class Storage {

    protected String filepath;

    /**
     * Constructor for storage
     * @param filepath of the file
     */
    public Storage(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Read the file and convert into a arraylist of tasks
     * @return ArrayList<Task>
     */
    public ArrayList<Task> readFile() {
        ArrayList<Task> list = new ArrayList<>();

        try {
            File f = new File(filepath);
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String word = s.nextLine();
                String[] parsed = TextParser.myThirdParser(word);
                Task currentTask = null;
                switch (parsed[0]) {
                    case "T":
                        currentTask = new Todo(parsed[2]);
                        break;
                    case "D":
                        currentTask = new Deadline(parsed[2], LocalDate.parse(parsed[3]));
                        break;
                    case "E":
                        currentTask = new Event(parsed[2], LocalDate.parse(parsed[3]));
                        break;
                    default:
                        break;
                }

                if (parsed[1].equals("1")) { // 1 means done
                    currentTask.markAsDone();
                }
                list.add(currentTask);
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    /**
     * Writes the arraylist of task into the file
     * @param taskList ArrayList<Task>
     */
    public void writeFile(ArrayList<Task> taskList) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filepath);
            for (Task current : taskList) {
                if (current instanceof Todo) {
                    writer.write("T" + "/" + current.checkDone() + "/" + current.getTask()
                            + System.lineSeparator());
                } else if (current instanceof Deadline) {
                    writer.write("D" + "/" + current.checkDone() + "/" + current.getTask() + "/"
                            + ((Deadline) current).getDate() + System.lineSeparator());
                } else if (current instanceof Event) {
                    writer.write("E" + "/" + current.checkDone() + "/" + current.getTask() + "/" +
                            ((Event) current).getDate() + System.lineSeparator());
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


