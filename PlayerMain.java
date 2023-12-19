import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.plaf.TreeUI;

public class PlayerMain {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        int choice = 0;
        Scanner sc = new Scanner(System.in);
        Player player = null;
        String name = "";
        SocketClient socket = null;
        boolean quit = true;
        boolean replay = true;

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_16); // true = autoflush
        out.println("\u4e16\u754c\u4f60\u597d\uff01");


        System.out.println("|--> Welcome to the pokemon game <--|");

        boolean gameMood = false;
        try {
            File audioFile = new File("pokemon.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            System.out.println("Error playing audio file: " + ex.getMessage());
        }

        while (!gameMood) {
            do {
                System.out.println("|--> Enter {1} to create your new profile ? |");
                System.out.println(
                        "|--> Enter {2} to connect to the server to recover your account and acheivements ? | ");
                System.out.print("|--> your choice .| ==> ");
                choice = sc.nextInt();
                sc.nextLine();
            } while (choice != 1 && choice != 2);

            switch (choice) {
                case 1:
                    do {
                        System.out.print("|--> Enter a vlide name { more than 5 chars } .| ==> ");
                        name = sc.nextLine();
                    } while (name.length() < 5);
                    player = new Player(name);
                    System.out.println("|--> Congratulations You have created succefly your account .|");
                    gameMood = true;
                    break;
                case 2:
                    do {
                        System.out.print(
                                "|--> Enter enter your unique name given by the server { more than 5 chars} .| ==> ");
                        name = sc.nextLine();
                    } while (name.length() < 5);

                    File f = new File("PlayersAchivements/" + name + ".txt");

                    if (f.exists() && !f.isDirectory()) {
                        try (ObjectInputStream playerObject = new ObjectInputStream(
                                new DataInputStream(new FileInputStream(f)))) {
                            player = (Player) playerObject.readObject();

                            gameMood = true;

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println("|--> Sorry it looks like your name dosen't exist .|");
                    }
                    break;

                default:
                    System.out.println("|--> Something went wrong !!! .|");
                    break;
            }
        }
        System.out.println("|--> Welcome " + player.getName() + " to the pokemon game <--|");
        do {
            choice = 0;
            do {
                System.out.println("|************************ Pokemon Menu *********************|");
                System.out.println("|--> Enter {1} to select a random pokemon .| ");
                System.out.println("|--> Enter {2} to start a battle in the server .| ");
                System.out.println("|--> Enter {3} to list your pokemons .| ");
                System.out.println("|--> Enter {4} to list your bonbons .| ");
                System.out.print("|--> your choice .| ==> ");
                try {
                    choice = sc.nextInt();
                } catch (Exception e) {
                    System.out.println("|--> Please Enter a valid number .| ");
                }
                sc.nextLine();
            } while (choice != 1 && choice != 2 && choice != 3 && choice != 4);

            switch (choice) {
                case 1:
                    PlayerMain.getPokemon(player);
                    break;
                case 2:
                    if (player.getPokemons().size() != 6) {
                        System.out.println(String.format(
                                "|--> Sorry try to have more pokemons , To strat a battle in the server you should make sure you have 6 pokemons | You still need %d .| ",
                                6 - player.getPokemons().size()));
                    } else {
                        quit = false;
                        replay = false;
                    }
                    break;
                case 3:
                    if (player.getPokemons().size() != 0) {
                        System.err.println("|--> Your list of pokemons .|");
                        for (Pokemon pokemon : player.getPokemons()) {
                            System.out.println(pokemon);
                        }
                    } else {
                        System.err.println("|--> Sorry you don't have pokemons yet .|");
                    }
                    break;
                case 4:
                    if (player.getBonbons().size() != 0) {
                        System.err.println("|--> Your list of bonbons .|");
                        for (Bonbon bonbon : player.getBonbons()) {
                            System.out.println("|--> " + bonbon + " |");
                        }
                    } else {
                        System.err.println("|--> Sorry you don't have bonbons yet .|");
                    }
                    break;
                default:
                    System.err.println("|--> Sorry you have to choose a valid number between 1 and 4 .|");
                    break;
            }

            if (!quit) {
                socket = new SocketClient(player, sc);
                String choiceContinue = "";
                do {
                    System.out.println("| ************************************************************ |");
                    System.out.println("|--> Do you want to replay [y/Y] or quit [n/N] ? |");
                    System.out.println("| ************************************************************ |");

                    choiceContinue = sc.nextLine();

                } while (!choiceContinue.toUpperCase().equals("Y")
                        && !choiceContinue.toUpperCase().equals("N"));

                if (choiceContinue.toUpperCase().charAt(0) == 'N') {
                    System.out.println("| ************************************************************ |");
                    System.out.println("|--> Goodbye se you later |");
                    System.out.println("| ************************************************************ |");
                } else {
                    System.out.println("| ************************************************************ |");
                    System.out.println("|--> You are going to replay. ready !!!!! |");
                    System.out.println("| ************************************************************ |");
                    quit = true;
                }
            }

        } while (quit);

    }

    public static void getPokemon(Player player) {
        if (player.getPokemons().size() >= 6) {
            System.out.println("|--> Sorry you already have 6 pokemons.|");
        } else {
            if (getRandomNumber(1, 5000) % 2 == 0) {

                List<PokType> normal = PokType.getBasePokemons();
                Random randomizer = new Random();
                PokType random = normal.get(randomizer.nextInt(normal.size()));
                Pokemon newPokemon = new Pokemon(random, getRandomNumber(0, 1000), getRandomNumber(0, 1000));
                player.addPokemon(newPokemon);
                player.addBonbon(newPokemon.getType().getBonbon());
                System.out.println(
                        "|--> Congratulations you have found a pokemon of type " + newPokemon.getType() + " .|");
                System.out.println(String.format(
                        "|--> Your new pokemon point are  { Pc = %f , Pv = %f } .|",
                        newPokemon.getPc(), newPokemon.getPv()));
                System.out.println(String.format(
                        "|--> You also win a bonbon of type %s |", newPokemon.getType().getBonbon()));

            } else {
                System.out.println("|--> Sorry you get nothing this time try again .|");
            }
        }
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
