public enum EvolutionStage {
    
    Base("Normal"),
    FirstEvolution("First Evolution"),
    SecondEvolution("Second Evolution");

    private String name = "";

    EvolutionStage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

}
