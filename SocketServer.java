
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Scanner;

public class SocketServer {
    
    final static int PORT_CONN = 4040;
    public static int numberPlayers = 1;
    private ServerSocket server = null;
    private boolean isConnected = false;
    private Hashtable<Player, Socket> con_table = null;
    public static String[] type = null;
    public ArrayList<String[]> matrix = new ArrayList<String[]>();

    public SocketServer() {
        this.server = null;
        try {
            this.server = new ServerSocket(PORT_CONN);
            this.isConnected = true;
            System.out.println("SERVER : CONNECTED");

            this.con_table = new Hashtable<Player, Socket>();

            // this.con_table.put(new UserInformation("ilyas", "ilyas.99"), new Socket());

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            ArrayList<String> lines = readFile("table_types.txt");
            SocketServer.type = lines.get(0).split(" ");
            for (String line : lines) {
                if (lines.indexOf(line) != 0)
                    matrix.add(line.split(" "));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> getMatrix() {
        return matrix;
    }

    public static String[] getType() {
        return type;
    }

    public boolean foundLogIn(Player user, Socket soc) {

        if (!this.con_table.isEmpty()) {

            Enumeration<Player> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    this.con_table.put(user, soc);
                    return true;
                }

            }

            return false;
        } else
            return false;
    }

    public Socket foundUser(String pseudo) {

        if (!this.con_table.isEmpty()) {

            Set<Entry<Player, Socket>> list = this.con_table.entrySet();
            Iterator<Entry<Player, Socket>> iterator = list.iterator();

            while (iterator.hasNext()) {
                Entry<Player, Socket> user = iterator.next();
                if (user.getKey().getUniqueName().equals(pseudo))
                    return user.getValue();
            }
        }
        return null;
    }

    public boolean registration(Player user) {

        if (!this.con_table.isEmpty()) {

            Enumeration<Player> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    public boolean addUser(Player user, Socket soc) {
        this.con_table.put(user, soc);
        System.out.println(" NEW SING IN :  " + "[" + user + "] \t" + soc);
        return true;
    }

    public boolean logOut(Player user) {

        if (!this.con_table.isEmpty()) {

            Enumeration<Player> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    this.con_table.put(user, (new Socket()));
                    return true;
                }
            }
            return false;
        } else
            return false;

    }

    public void startConversation() {

        ExecutorService executorService = Executors.newCachedThreadPool();
        if (this.isConnected) {
            Socket firstSoc = null;
            Socket secondSoc = null;

            for (;;) {
                try {

                    firstSoc = this.server.accept();
                    secondSoc = this.server.accept();
                    (new TraitementServerThreadFight(firstSoc, secondSoc, this)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else
            System.out.println("THE SERVER IS NOT CONNECTED !!!");

    }

    public Hashtable<Player, Socket> getCon_table() {
        return con_table;
    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.startConversation();
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
