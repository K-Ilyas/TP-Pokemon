
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Main class is the entry point for the Java program.
 */
public class Main {
    public static String[] type = null;
    public static ArrayList<String[]> matrix = new ArrayList<String[]>();

    public static void main(String[] args) {
        try {
            ArrayList<String> lines = readFile("table_types.txt");

            type = lines.get(0).split(" ");

            for (String line : lines) {
                if (lines.indexOf(line) != 0)
                    System.err.println(line.split(" ").length + "wow");
                matrix.add(line.split(" "));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void attack(Player firstPlayer, Player secondPlayer) {

        System.out.println(String.format(
                "|--> The player %s with the pokemon %s is attacking the player %s with the pokemon %s |"));

    }

    public static ArrayList<String> readFile(String input)
            throws FileNotFoundException, IOException {
        ArrayList<String> lines = new ArrayList<String>();
        try (Scanner sc = new Scanner(new File(input), StandardCharsets.UTF_8.name());) {
            lines.add(sc.nextLine());

            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        }
        return lines;
    }
}
