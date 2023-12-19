import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Player class represents a player in a Pokemon game, with a name, a list
 * of captured Pokemon, and
 * a list of collected Bonbons.
 */

public class Player implements Comparable<Player>, Serializable {

    public static int nbMaxPokemons = 6;
    private String name = "";
    private String password = "";
    private String uniqueName = "";
    private ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
    private ArrayList<Bonbon> bonbons = new ArrayList<Bonbon>();
    private boolean inGame = false;
    private boolean isFirstToplay = false;

    private Pokemon lastSelectedPokemon = null;

    public Player(String name) {
        this.name = name;
    }

    public ArrayList<Bonbon> getBonbons() {
        return bonbons;
    }

    public void setFirstToplay(boolean isFirstToplay) {
        this.isFirstToplay = isFirstToplay;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public boolean getFirstToPlay() {
        return this.isFirstToplay;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean getInGame() {
        return this.inGame;
    }

    public static int getNbMaxPokemons() {
        return nbMaxPokemons;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int compareTo(Player o) {
        if (o.getClass().equals(Player.class)) {
            Player cd = (Player) o;
            return this.name.compareTo(cd.getName()) & this.password.compareTo(cd.getPassword());
        }
        return -1;
    }

    public void setLastSelectedPokemon(Pokemon lastSelectedPokemon) {
        if (lastSelectedPokemon != null) {
            if (this.pokemons.contains(lastSelectedPokemon)) {
                this.lastSelectedPokemon = lastSelectedPokemon;
                System.out.println(
                        "You select the current pokemom " + lastSelectedPokemon.getType() + " as your default pokemon");
            } else {
                System.out.println(
                        "This pokemom " + lastSelectedPokemon.getType() + " is not one of your pokemons");
            }
        } else {
            System.out.println(
                    "Right now you are without a pokemon");
        }
    }

    public Pokemon getLastSelectedPokemon() {
        return lastSelectedPokemon;
    }

    public void setBonbons(ArrayList<Bonbon> bonbons) {
        this.bonbons = bonbons;
    }

    public void addPokemon(Pokemon pokemon) {

        if (this.pokemons.size() < nbMaxPokemons) {
            this.pokemons.add(pokemon);
            // System.out.println("|-->  Your Pokemon " + pokemon.getType().getName() + " added successfully. |");
        } else
            System.out.println("|--> Sorry you already have 6 pokemons. |");
    }

    public void addBonbon(Bonbon bonbon) {

        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int count = 0, index = -1;
        Scanner sc = new Scanner(System.in);
        ArrayList<Bonbon> listOfBonbons = new ArrayList<Bonbon>();

        this.bonbons.add(bonbon);

        for (Bonbon bb : this.bonbons) {

            if (bb.getName() == bonbon.getName()) {
                count++;
                listOfBonbons.add(bb);
            }
        }

        if (count >= 5) {

            System.out.println("|-->  You have five Bonbon for " + bonbon.getName() + " Bonbon|");
            System.out.println("|--> You can Develop one of the current pokemons.|");

            for (Pokemon pok : this.pokemons) {
                if (pok.getType().getName() == bonbon.getName()) {
                    indexes.add(this.pokemons.indexOf(pok));
                }
            }

            for (int ind : indexes) {
                System.out.println("|--> Enter {" + ind + "} to Develop the pokemon with Type "
                        + this.pokemons.get(ind).getType()
                        + " and index " + ind + ".|");
            }

            do {
                System.out.print("|--> Please choose a valid number for the numbers listed before.| ==> ");
                index = sc.nextInt();
                sc.nextLine();
            } while (!indexes.contains(index));

            Pokemon oldType = this.pokemons.get(index);
            double oldPv = oldType.getPv();
            double oldPc = oldType.getPc();
            PokType oldTypePok = oldType.getType();
            Pokemon currType = this.pokemons.get(index);

            currType.updateType();

            if (oldTypePok != currType.getType()) {
                switch (currType.getType().getStage()) {
                    case EvolutionStage.FirstEvolution:
                        currType.updatePc(currType.getPc() * 0.1);
                        currType.updatePv(currType.getPv() * 0.1);
                        break;
                    case EvolutionStage.SecondEvolution:
                        currType.updatePc(currType.getPc() * 0.2);
                        currType.updatePv(currType.getPv() * 0.2);
                    default:
                        break;
                }
            } else {
                currType.updatePc(currType.getPc() * 0.5);
                currType.updatePv(currType.getPv() * 0.5);
            }

            System.out.println(
                    "|--> The Pokemon " + oldTypePok + " with the index " + index + " has developed succefly to "
                            + this.pokemons.get(index).getType() + " in the "
                            + oldType.getType().getStage() + ".|");

            System.out.println(String.format(
                    "|--> The new Pokemon %s %s %s %s %s .|",
                    currType.getType().getTypes()[1] != null ? "Types" : "Type",
                    currType.getType().getTypes()[1] != null ? "are" : "is", currType.getType().getTypes()[0],
                    currType.getType().getTypes()[1] != null ? "," : "",
                    currType.getType().getTypes()[1] != null ? currType.getType().getTypes()[1] : ""));
                    
            System.out.println(String.format(
                    "|--> Your pokemon point has beeb improved from  { Pc = %f , Pv = %f } to  { Pc = %f , Pv = %f } .|",
                    oldPc, oldPv, currType.getPc(), currType.getPv()));

            for (Bonbon bb : listOfBonbons) {
                this.bonbons.remove(bb);
            }

        }

    }

}
