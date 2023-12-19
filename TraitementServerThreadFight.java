import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class TraitementServerThreadFight extends Thread {

    private Socket firstPlayer = null;
    private Socket secondPlayer = null;
    private final Lock lock = new ReentrantLock();
    public ArrayList<String[]> matrix = new ArrayList<String[]>();
    public String[] type = null;

    private SocketServer socketServer = null;

    public TraitementServerThreadFight(Socket firstPlayer, Socket secondPlayer, SocketServer socketServer) {
        this.firstPlayer = firstPlayer;
        this.socketServer = socketServer;
        this.secondPlayer = secondPlayer;

        try {

            ArrayList<String> lines = TraitementServerThreadFight.readFile("table_types.txt");
            this.type = lines.get(0).split(" ");
            for (String line : lines) {
                if (lines.indexOf(line) != 0) {
                    this.matrix.add(line.split(" "));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {

            OutputStream out = this.firstPlayer.getOutputStream();
            InputStream in = this.firstPlayer.getInputStream();
            OutputStream outSecond = this.secondPlayer.getOutputStream();
            InputStream inSecond = this.secondPlayer.getInputStream();

            DataInputStream dis = new DataInputStream(in);
            DataOutputStream dos = new DataOutputStream(out);
            ObjectInputStream ois = new ObjectInputStream(in);
            ObjectOutputStream oos = new ObjectOutputStream(out);

            DataInputStream disSecond = new DataInputStream(inSecond);
            DataOutputStream doSecond = new DataOutputStream(outSecond);
            ObjectInputStream oisSecond = new ObjectInputStream(inSecond);
            ObjectOutputStream oosSecond = new ObjectOutputStream(outSecond);
            int i = 0;
            int iSecond = 0;

            boolean isTrue = true;

            Player firstPalyerInfo = null;
            Player secondPalyerInfo = null;

            firstPalyerInfo = null;
            try {
                firstPalyerInfo = (Player) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (firstPalyerInfo.getUniqueName().equals("")) {

                firstPalyerInfo.setUniqueName(
                        String.format("%s-%d", firstPalyerInfo.getName(),
                                SocketServer.numberPlayers));
                SocketServer.numberPlayers++;
            }
            this.socketServer.addUser(firstPalyerInfo, this.firstPlayer);

            dos.writeInt(1);
            dos.writeUTF("SERVER : YOUR REGISTARTION HAS ESTABLISHED SUCCESFULY pealse remenber your unique name "
                    + firstPalyerInfo.getUniqueName());

            dos.flush();

            // for the second player

            secondPalyerInfo = null;
            try {
                secondPalyerInfo = (Player) oisSecond.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (secondPalyerInfo.getUniqueName().equals("")) {
                secondPalyerInfo.setUniqueName(
                        String.format("%s-%d", secondPalyerInfo.getName(),
                                SocketServer.numberPlayers));

                SocketServer.numberPlayers++;
            }

            this.socketServer.addUser(secondPalyerInfo, this.secondPlayer);

            doSecond.writeInt(1);
            doSecond.writeUTF("SERVER : YOUR REGISTARTION HAS ESTABLISHED SUCCESFULY pealse remenber your unique name "
                    + secondPalyerInfo.getUniqueName());

            doSecond.flush();

            String str1 = "|--> You are going to play againt " +
                    secondPalyerInfo.getName();
            String str2 = "|--> You are going to play againt " +
                    firstPalyerInfo.getName();
            dos.writeUTF(str1);
            dos.flush();
            doSecond.writeUTF(str2);
            doSecond.flush();

            int intFirst = 0;
            int intSecond = 0;
            String[] types = new String[] { "Normal", "Feu", "Eau", "Plante", "Électrik", "Glace", "Combat",
                    "Poison", "Sol", "Vol", "Psy", "Insecte", "Roche", "Spectre", "Dragon", "Ténèbres", "Acier"
            };
            while (true) {

                dos.writeInt(4);

                dos.writeInt(firstPalyerInfo.getPokemons().size());

                dos.flush();
                for (Pokemon pok : firstPalyerInfo.getPokemons()) {
                    oos.writeObject(pok);
                }
                oos.flush();

                dos.writeInt(firstPalyerInfo.getBonbons().size());
                dos.flush();

                // ---------------------------------------------------------------------------------------------------------------------------

                doSecond.writeInt(4);

                doSecond.writeInt(secondPalyerInfo.getPokemons().size());

                doSecond.flush();

                for (Pokemon pok : secondPalyerInfo.getPokemons()) {
                    oosSecond.writeObject(pok);
                }

                oosSecond.flush();

                doSecond.writeInt(secondPalyerInfo.getBonbons().size());
                oosSecond.flush();

                doSecond.writeInt(5);
                doSecond.writeUTF("|--> jaust wait for the first player to make his decision");
                doSecond.flush();
                int selectedPokemonIndex = dis.readInt();

                firstPalyerInfo.setLastSelectedPokemon(firstPalyerInfo.getPokemons().get(selectedPokemonIndex));

                dos.writeInt(5);
                dos.writeUTF("|--> jaust wait for the second player to make his decision");
                dos.flush();

                int selectedPokemonIndexSecond = disSecond.readInt();
                secondPalyerInfo.setLastSelectedPokemon(secondPalyerInfo.getPokemons().get(selectedPokemonIndexSecond));

                int data = 0;
                for (String str : types) {
                    if (secondPalyerInfo.getLastSelectedPokemon().getType().getTypes()[0].toString() == str.trim()) {
                        intSecond = data;
                        break;
                    }
                    data++;
                }
                data = 0;
                for (String str : types) {
                    if (firstPalyerInfo.getLastSelectedPokemon().getType().getTypes()[0].toString() == str.trim()) {
                        intFirst = data;
                        break;
                    }
                    data++;
                }

                int random = TraitementServerThreadFight.getRandomNumber(50, 100000);
                lock.lock();

                try {
                    while (firstPalyerInfo.getLastSelectedPokemon().getPv() > 0.0
                            && secondPalyerInfo.getLastSelectedPokemon().getPv() > 0.0) {

                        if (random % 2 == 0) {
                            doSecond.writeInt(5);
                            doSecond.writeUTF("|--> Your pokemomn "
                                    + secondPalyerInfo.getLastSelectedPokemon().getType().getName() + " just lost " +
                                    Double.parseDouble(this.matrix.get(intFirst)[intSecond])
                                            * firstPalyerInfo.getLastSelectedPokemon().getPv()
                                    + " Pv points");
                            doSecond.flush();

                            secondPalyerInfo.getLastSelectedPokemon()
                                    .updatePv(Double.parseDouble(this.matrix.get(intFirst)[intSecond])
                                            * firstPalyerInfo.getLastSelectedPokemon().getPv());

                            if (secondPalyerInfo.getLastSelectedPokemon().getPv() <= 0) {
                                secondPalyerInfo.getPokemons().remove(secondPalyerInfo.getLastSelectedPokemon());
                                secondPalyerInfo.setLastSelectedPokemon(null);
                                doSecond.writeInt(5);
                                doSecond.writeUTF("|--> Sorry you lost the fight");
                                doSecond.flush();
                                dos.writeInt(5);
                                dos.writeUTF("|--> Congratulations you pokemon "
                                        + firstPalyerInfo.getLastSelectedPokemon().getType().getName() + " has won.");
                                dos.flush();

                                dos.writeInt(5);
                                dos.writeUTF("|--> Congratulations you win a bonbon of type "
                                        + firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                dos.flush();

                                dos.writeInt(7);

                                dos.flush();

                                oos.writeObject(firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon());

                                oos.flush();

                                int count = 0;
                                ArrayList<Bonbon> listOfBonbons = new ArrayList<Bonbon>();
                                for (Bonbon bb : firstPalyerInfo.getBonbons()) {

                                    if (bb.getName() == firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon()
                                            .getName()) {
                                        count++;
                                        listOfBonbons.add(bb);
                                    }
                                }

                                if (count + 1 >= 5) {
                                    for (Bonbon bb : listOfBonbons) {
                                        firstPalyerInfo.getBonbons().remove(bb);
                                    }
                                } else {
                                    firstPalyerInfo
                                            .addBonbon(firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                }

                                firstPalyerInfo.setPokemons(new ArrayList<Pokemon>());

                                int size = dis.readInt();

                                for (int m = 0; m < size; m++) {
                                    try {
                                        Pokemon newPokemon = (Pokemon) ois.readObject();
                                        firstPalyerInfo.addPokemon(newPokemon);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;

                            }

                            dos.writeInt(5);
                            dos.writeUTF("|--> Your pokemomn "
                                    + firstPalyerInfo.getLastSelectedPokemon().getType().getName() + " just lost " +
                                    Double.parseDouble(this.matrix.get(intSecond)[intFirst])
                                            * secondPalyerInfo.getLastSelectedPokemon().getPv()
                                    + " Pv points");
                            dos.flush();

                            firstPalyerInfo.getLastSelectedPokemon()
                                    .updatePv(Double.parseDouble(this.matrix.get(intSecond)[intFirst])
                                            * secondPalyerInfo.getLastSelectedPokemon().getPv());
                            if (firstPalyerInfo.getLastSelectedPokemon().getPv() <= 0) {
                                firstPalyerInfo.getPokemons().remove(firstPalyerInfo.getLastSelectedPokemon());
                                firstPalyerInfo.setLastSelectedPokemon(null);
                                dos.writeInt(5);
                                dos.writeUTF("|--> Sorry you lost the fight");
                                dos.flush();
                                doSecond.writeInt(5);
                                doSecond.writeUTF("|--> Congratulations you pokemon "
                                        + secondPalyerInfo.getLastSelectedPokemon().getType().getName() + " has won.");
                                doSecond.flush();

                                doSecond.writeInt(5);
                                doSecond.writeUTF("|--> Congratulations you win a bonbon of type "
                                        + secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                doSecond.flush();

                                doSecond.writeInt(7);

                                doSecond.flush();

                                oosSecond.writeObject(secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon());

                                oosSecond.flush();
                                int count = 0;
                                ArrayList<Bonbon> listOfBonbons = new ArrayList<Bonbon>();
                                for (Bonbon bb : secondPalyerInfo.getBonbons()) {

                                    if (bb.getName() == secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon()
                                            .getName()) {
                                        count++;
                                        listOfBonbons.add(bb);
                                    }
                                }

                                if (count + 1 >= 5) {
                                    for (Bonbon bb : listOfBonbons) {
                                        secondPalyerInfo.getBonbons().remove(bb);
                                    }
                                } else {
                                    secondPalyerInfo
                                            .addBonbon(secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                }

                                secondPalyerInfo.setPokemons(new ArrayList<Pokemon>());

                                int size = disSecond.readInt();

                                for (int m = 0; m < size; m++) {
                                    try {
                                        Pokemon newPokemon = (Pokemon) oisSecond.readObject();
                                        secondPalyerInfo.addPokemon(newPokemon);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;
                            }
                        } else {
                            dos.writeInt(5);
                            dos.writeUTF("|--> Your pokemomn "
                                    + firstPalyerInfo.getLastSelectedPokemon().getType().getName() + " just lost " +
                                    Double.parseDouble(this.matrix.get(intSecond)[intFirst])
                                            * secondPalyerInfo.getLastSelectedPokemon().getPv()
                                    + " Pv points");
                            dos.flush();

                            firstPalyerInfo.getLastSelectedPokemon()
                                    .updatePv(Double.parseDouble(this.matrix.get(intSecond)[intFirst])
                                            * secondPalyerInfo.getLastSelectedPokemon().getPv());
                            if (firstPalyerInfo.getLastSelectedPokemon().getPv() <= 0) {
                                firstPalyerInfo.getPokemons().remove(firstPalyerInfo.getLastSelectedPokemon());
                                firstPalyerInfo.setLastSelectedPokemon(null);
                                dos.writeInt(5);
                                dos.writeUTF("|--> Sorry you lost the fight");
                                dos.flush();
                                doSecond.writeInt(5);
                                doSecond.writeUTF("|--> Congratulations you pokemon "
                                        + secondPalyerInfo.getLastSelectedPokemon().getType().getName() + " has won.");
                                doSecond.flush();

                                doSecond.writeInt(5);

                                doSecond.writeUTF("|--> Congratulations you win anw bonbon of type "
                                        + secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                doSecond.flush();

                                doSecond.writeInt(7);

                                doSecond.flush();

                                oosSecond.writeObject(secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon());

                                oosSecond.flush();

                                int count = 0;
                                ArrayList<Bonbon> listOfBonbons = new ArrayList<Bonbon>();
                                for (Bonbon bb : secondPalyerInfo.getBonbons()) {

                                    if (bb.getName() == secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon()
                                            .getName()) {
                                        count++;
                                        listOfBonbons.add(bb);
                                    }
                                }

                                if (count + 1 >= 5) {
                                    for (Bonbon bb : listOfBonbons) {
                                        secondPalyerInfo.getBonbons().remove(bb);
                                    }
                                } else {
                                    secondPalyerInfo
                                            .addBonbon(secondPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                }

                                secondPalyerInfo.setPokemons(new ArrayList<Pokemon>());

                                int size = disSecond.readInt();

                                for (int m = 0; m < size; m++) {
                                    try {
                                        Pokemon newPokemon = (Pokemon) oisSecond.readObject();
                                        secondPalyerInfo.addPokemon(newPokemon);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;
                            }
                            doSecond.writeInt(5);
                            doSecond.writeUTF("|--> Your pokemomn "
                                    + secondPalyerInfo.getLastSelectedPokemon().getType().getName() + " just lost " +
                                    Double.parseDouble(this.matrix.get(intFirst)[intSecond])
                                            * firstPalyerInfo.getLastSelectedPokemon().getPv()
                                    + " Pv points");
                            doSecond.flush();

                            secondPalyerInfo.getLastSelectedPokemon()
                                    .updatePv(Double.parseDouble(this.matrix.get(intFirst)[intSecond])
                                            * firstPalyerInfo.getLastSelectedPokemon().getPv());

                            if (secondPalyerInfo.getLastSelectedPokemon().getPv() <= 0) {
                                secondPalyerInfo.getPokemons().remove(secondPalyerInfo.getLastSelectedPokemon());
                                secondPalyerInfo.setLastSelectedPokemon(null);
                                doSecond.writeInt(5);
                                doSecond.writeUTF("|--> Sorry you lost the fight");
                                doSecond.flush();
                                dos.writeInt(5);
                                dos.writeUTF("|--> Congratulations you pokemon "
                                        + firstPalyerInfo.getLastSelectedPokemon().getType().getName() + "has won.");
                                dos.flush();

                                dos.writeInt(5);

                                dos.writeUTF("|--> Congratulations you win anw bonbon of type "
                                        + firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                dos.flush();
                                dos.writeInt(7);

                                dos.flush();

                                oos.writeObject(firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon());

                                oos.flush();
                                int count = 0;
                                ArrayList<Bonbon> listOfBonbons = new ArrayList<Bonbon>();
                                for (Bonbon bb : firstPalyerInfo.getBonbons()) {

                                    if (bb.getName() == firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon()
                                            .getName()) {
                                        count++;
                                        listOfBonbons.add(bb);
                                    }
                                }

                                if (count + 1 >= 5) {

                                    for (Bonbon bb : listOfBonbons) {
                                        firstPalyerInfo.getBonbons().remove(bb);
                                    }

                                } else {
                                    firstPalyerInfo
                                            .addBonbon(firstPalyerInfo.getLastSelectedPokemon().getType().getBonbon());
                                }

                                firstPalyerInfo.setPokemons(new ArrayList<Pokemon>());

                                int size = dis.readInt();

                                for (int m = 0; m < size; m++) {
                                    try {
                                        Pokemon newPokemon = (Pokemon) ois.readObject();
                                        firstPalyerInfo.addPokemon(newPokemon);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;

                            }
                        }

                    }

                } finally {
                    lock.unlock();
                }

                lock.lock();
                try {
                    if (secondPalyerInfo.getPokemons().size() == 0 || firstPalyerInfo.getPokemons().size() == 0) {
                        dos.writeInt(0);
                        dos.flush();
                        doSecond.writeInt(0);
                        doSecond.flush();
                        break;
                    }
                } finally {
                    lock.unlock();
                }

            }

            File firstPlayerFile = new File("PlayersAchivements/" + firstPalyerInfo.getUniqueName() + ".txt");
            firstPlayerFile.createNewFile(); // if file already exists will do nothing

            try (ObjectOutputStream firstPlayerObjectOutputStream = new ObjectOutputStream(
                    new DataOutputStream(new FileOutputStream(firstPlayerFile, false)))) {
                firstPlayerObjectOutputStream.writeObject(firstPalyerInfo);
            }

            File secondPlayerFile = new File("PlayersAchivements/" + secondPalyerInfo.getUniqueName() + ".txt");
            secondPlayerFile.createNewFile(); // if file already exists will do nothing

            try (ObjectOutputStream secondPlayerObjectOutputStream = new ObjectOutputStream(
                    new DataOutputStream(new FileOutputStream(secondPlayerFile, false)))) {
                secondPlayerObjectOutputStream.writeObject(secondPalyerInfo);
            }

            dos.writeUTF(
                    "|--> your going to have a conversion with the second player " + secondPalyerInfo.getUniqueName()
                            + " about the game [ to quit the chat just press n/N ]");
            dos.flush();

            doSecond.writeUTF(
                    "|--> your going to have a conversion with the first player " + firstPalyerInfo.getUniqueName()
                            + " about the game [ to quit the chat just press n/N ]");
            doSecond.flush();

            dos.writeUTF(secondPalyerInfo.getUniqueName());
            dos.flush();

            doSecond.writeUTF(firstPalyerInfo.getUniqueName());
            doSecond.flush();

            Thread firstThread = new Thread(new ServerMesageHandle(this.socketServer, dos, dis, ois));

            firstThread.start();

            Thread secondThread = new Thread(new ServerMesageHandle(this.socketServer, doSecond, disSecond, oisSecond));

            secondThread.start();

            try {
                firstThread.join();
                oos.writeObject(firstPalyerInfo);
                oos.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                secondThread.join();
                oosSecond.writeObject(secondPalyerInfo);
                oosSecond.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (

        FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}