// The code snippet is defining an enum called `Type` in Java. An enum is a special type in Java that
// represents a fixed set of constants.

import java.util.ArrayList;
import java.util.List;

public enum PokType {

    // Second Stage
    Florizarre("Florizarre", Type.Plante, Type.Poison, null, EvolutionStage.SecondEvolution, Bonbon.Florizarre),
    Dracaufeu("Dracaufeu", Type.Feu, null, null, EvolutionStage.SecondEvolution, Bonbon.Dracaufeu),
    Tortank("Tortank", Type.Eau, null, null, EvolutionStage.SecondEvolution, Bonbon.Tortank),
    Papilusion("Papilusion", Type.Insecte, Type.Vol, null, EvolutionStage.SecondEvolution, Bonbon.Papilusion),
    Dardargnan("Dardargnan", Type.Insecte, Type.Poison, null, EvolutionStage.SecondEvolution, Bonbon.Dardargnan),
    Roucarnage("Roucarnage", Type.Normal, Type.Vol, null, EvolutionStage.SecondEvolution, Bonbon.Roucarnage),
    // First Stage
    Herbizarre("Herbizarre", Type.Plante, Type.Poison, PokType.Florizarre, EvolutionStage.FirstEvolution,
            Bonbon.Herbizarre),
    Reptincel("Reptincel", Type.Feu, null, PokType.Dracaufeu, EvolutionStage.FirstEvolution, Bonbon.Reptincel),
    Carabaffe("Carabaffe", Type.Eau, null, PokType.Tortank, EvolutionStage.FirstEvolution, Bonbon.Carabaffe),
    Chrysacier("Chrysacier", Type.Insecte, Type.Vol, PokType.Papilusion, EvolutionStage.FirstEvolution,
            Bonbon.Chrysacier),
    Coconfort("Coconfort", Type.Insecte, Type.Poison, PokType.Dardargnan, EvolutionStage.FirstEvolution,
            Bonbon.Coconfort),
    Roucoups("Roucoups", Type.Normal, Type.Vol, PokType.Roucarnage, EvolutionStage.FirstEvolution, Bonbon.Roucoups),
    Rattatac("Rattatac", Type.Normal, null, null, EvolutionStage.FirstEvolution, Bonbon.Rattatac),
    Rapasdepic("Rapasdepic", Type.Normal, Type.Vol, null, EvolutionStage.FirstEvolution, Bonbon.Rapasdepic),
    Arbok("Arbok", Type.Poison, null, null, EvolutionStage.FirstEvolution, Bonbon.Arbok),
    Raichu("Raichu", Type.Electrique, null, null, EvolutionStage.FirstEvolution, Bonbon.Raichu),
    // Base Stage
    Bulbizarre("Bulbizarre", Type.Plante, Type.Poison, PokType.Herbizarre, EvolutionStage.Base, Bonbon.Bulbizarre),
    Salameche("Salamèche", Type.Feu, null, PokType.Reptincel, EvolutionStage.Base, Bonbon.Salameche),
    Carapuce("Carapuce", Type.Eau, null, PokType.Carabaffe, EvolutionStage.Base, Bonbon.Carapuce),
    Chenipan("Chenipan", Type.Insecte, Type.Vol, PokType.Chrysacier, EvolutionStage.Base, Bonbon.Chenipan),
    Aspicot("Aspicot", Type.Insecte, Type.Poison, PokType.Coconfort, EvolutionStage.Base, Bonbon.Aspicot),
    Roucool("Roucool", Type.Normal, Type.Vol, PokType.Roucoups, EvolutionStage.Base, Bonbon.Roucool),
    Rattata("Rattata", Type.Normal, null, PokType.Rattatac, EvolutionStage.Base, Bonbon.Rattata),
    Piafabec("Piafabec", Type.Normal, Type.Vol, PokType.Rapasdepic, EvolutionStage.Base, Bonbon.Piafabec),
    Abo("Abo", Type.Poison, null, PokType.Arbok, EvolutionStage.Base, Bonbon.Abo),
    Pikachu("Pikachu", Type.Electrique, null, PokType.Raichu, EvolutionStage.Base, Bonbon.Pikachu);

    private String name = "";
    private Type firstType = null;
    private Type secondType = null;
    private PokType evolution = null;
    private EvolutionStage stage = null;
    private Bonbon bonbon = null;

    // Constructeur
    PokType(String name, Type firstType, Type secondType, PokType evolution, EvolutionStage stage, Bonbon bonbon) {
        this.name = name;
        this.firstType = firstType;
        this.secondType = secondType;
        this.evolution = evolution;
        this.stage = stage;
        this.bonbon = bonbon;
    }

    public Bonbon getBonbon() {
        return bonbon;
    }

    public String getName() {
        return name;
    }

    public Type[] getTypes() {
        return new Type[] { this.firstType, this.secondType };
    }

    public Type getFirstType() {
        return firstType;
    }

    public Type getSecondType() {
        return secondType;
    }
    public PokType getEvolution() {
        return evolution;
    }

    public EvolutionStage getStage() {
        return stage;
    }

    public String toString() {
        return name;
    }

    /**
     * The function "getBasePokemons" returns a list of Pokemon types that are in
     * their base evolution
     * stage.
     * 
     * @return The method is returning a List of PokType objects.
     */

    public static List<PokType> getBasePokemons() {
        List<PokType> basePokemons = new ArrayList<PokType>();
        for (PokType type : PokType.values()) {
            if (type.getStage() == EvolutionStage.Base) {
                basePokemons.add(type);
            }
        }
        return basePokemons;
    }
}
