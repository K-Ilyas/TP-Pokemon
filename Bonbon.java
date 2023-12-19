public enum Bonbon {

    // Second Stage
    Florizarre("Florizarre"),
    Dracaufeu("Dracaufeu"),
    Tortank("Tortank"),
    Papilusion("Papilusion"),
    Dardargnan("Dardargnan"),
    Roucarnage("Roucarnage"),
    // First Stage
    Herbizarre("Herbizarre"),
    Reptincel("Reptincel"),
    Carabaffe("Carabaffe"),
    Chrysacier("Chrysacier"),
    Coconfort("Coconfort"),
    Roucoups("Roucoups"),
    Rattatac("Rattatac"),
    Rapasdepic("Rapasdepic"),
    Arbok("Arbok"),
    Raichu("Raichu"),
    // Base Stage
    Bulbizarre("Bulbizarre"),
    Salameche("Salamèche"),
    Carapuce("Carapuce"),
    Chenipan("Chenipan"),
    Aspicot("Aspicot"),
    Roucool("Roucool"),
    Rattata("Rattata"),
    Piafabec("Piafabec"),
    Abo("Abo"),
    Pikachu("Pikachu");

    private String name = "";

    // Constructeur
    Bonbon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
