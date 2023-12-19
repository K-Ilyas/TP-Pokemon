public enum Type {
    Plante("Plante", "☘️"),
    Feu("Feu", "🔥"),
    Eau("Eau", "🌊"),
    Insecte("Insecte", "🪲"),
    Normal("Normal", "♾️"),
    Poison("Poison", "☣️"),
    Electrique("Electrique", "⚡"),
    Vol("Vol", "🪽");


    private String name = "";
    private String emoji = "";

    Type(String name, String emoji) {
        this.name = name;
        this.emoji = emoji;
    }

    public String getEmoji() {
        return this.emoji;
    }
    @Override
    public String toString() {
        return this.name + " " + this.emoji;
    }
}
