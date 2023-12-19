import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SocketClient {

    final static int PORT_CON = 4040;
    private Socket socket = null;
    private Boolean isConnected = false;
    private Scanner scanf = null;
    private Boolean isLoged = false;
    private Boolean isOut = false;
    private Player player = null;
    private Thread recive = null;
    private Thread send = null;

    public SocketClient(Player player, Scanner sc) {

        this.socket = null;
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), PORT_CON);
            this.isConnected = true;
            System.out.println("player : CONNECTED");
            System.out.println("please wait until another player connect");

            this.player = player;
            this.scanf = sc;
            this.startConversation();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void choix() {
        System.out.println("!!-----------------------------!!");
        System.out.println("!!------- POSIBLE CHOICE ------!!");
        System.out.println("!!------- 1 : LOG IN   --------!!");
        System.out.println("!!------- 2 : SING IN  --------!!");

    }

    public int decision() {

        int choix = 0;

        do {
            this.choix();
            choix = this.scanf.nextInt();
            this.scanf.nextLine();
            if (choix < 1 || choix > 2)
                System.out.println("Your choice is Rong please retrait");

        } while (choix < 1 || choix > 2);
        return choix;
    }

    public void startConversation() throws IOException {

        if (this.isConnected) {

            try {
                OutputStream out = this.socket.getOutputStream();
                InputStream in = this.socket.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);
                ObjectOutputStream bos = new ObjectOutputStream(out);
                ObjectInputStream ois = new ObjectInputStream(in);

                int choix = 0;
                String stay = "";
                char n = 0;
                String pseudo = "", password = "", passwordConfirme = "";
                int i = 0;
                boolean isTrue = true;

                bos.writeObject(this.player);
                bos.flush();

                dis.readInt();

                System.out.println("| ************************************************* |");
                System.out.println(dis.readUTF());
                System.out.println("| ************************************************* |");

                String output = dis.readUTF();
                System.out.println("| ************************************************* |");
                System.out.println(output);
                System.out.println("| ************************************************* |");

                int index = -1;
                choix = -1;

                choix = dis.readInt();
                System.out.println("wow" + choix);

                while (choix != 0) {

                    switch (choix) {
                        case 4:

                            this.player.setPokemons(new ArrayList<Pokemon>());

                            int size = dis.readInt();

                            for (int m = 0; m < size; m++) {
                                try {
                                    Pokemon newPokemon = (Pokemon) ois.readObject();
                                    this.player.addPokemon(newPokemon);
                                    System.out.println(
                                            String.format("|--> Enter {%d} to select %s as your default pokemon |",
                                                    m + 1,
                                                    newPokemon.toString()));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                            int secondSize = dis.readInt();

                            do {
                                System.out.print(
                                        "|--> Please choose a valid number for the numbers listed before.| ==> ");
                                index = this.scanf.nextInt();
                                scanf.nextLine();
                            } while (index < 1 || index > size);

                            this.player.setLastSelectedPokemon(this.player.getPokemons().get(index - 1));

                            dos.writeInt(index - 1);
                            dos.flush();
                            index = -1;

                            break;
                        case 5:
                            System.out.println("| ************************************************* |");
                            System.out.println(dis.readUTF());
                            System.out.println("| ************************************************* |");
                            break;
                        case 7:
                            try {
                                this.player.addBonbon((Bonbon) ois.readObject());
                                dos.writeInt(this.player.getPokemons().size());
                                dos.flush();
                                for (Pokemon pok : this.player.getPokemons()) {
                                    bos.writeObject(pok);
                                }
                                bos.flush();

                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                    choix = dis.readInt();
                }

                System.out.println("| **************************************************************** |");
                System.out.println(dis.readUTF());
                System.out.println("| **************************************************************** |");

                this.recive = new Thread(new ReciveMessage(this.socket, this.player));
                this.send = new Thread(new SendMessage(dos, bos, this.player, dis.readUTF()));
                this.send.start();
                this.recive.start();

                try {
                    send.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    recive.join();

                    try {
                        Player newPlayerInfo = (Player) ois.readObject();

                        this.player.setPokemons(newPlayerInfo.getPokemons());
                        this.player.setBonbons(newPlayerInfo.getBonbons());

                        System.out.println("| **************************************************************** |");
                        System.out.println("|--> Your info has been saved ");
                        System.out.println("| **************************************************************** |");

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else
            System.out.println("THE CLIENT IS NOT CONNECTED");

    }
}
