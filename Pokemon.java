import java.io.Serializable;
import java.io.UTFDataFormatException;
import java.nio.charset.StandardCharsets;

import javax.sound.sampled.LineListener;

public class Pokemon implements Serializable {

    private PokType type = null;
    private double pc = 0;
    private double pv = 0;

    public Pokemon(PokType type, int pc, int pv) {
        this.type = type;
        this.pc = pc;
        this.pv = pv;
    }

    public double getPc() {
        return pc;
    }

    public double getPv() {
        return this.pv;
    }

    public PokType getType() {
        return this.type;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public void setType(PokType type) {
        this.type = type;
    }

    public void updatePv(double points) {
        this.pv -= points;
    }

    public void updatePc(double points) {
        this.pc += points;
    }

    public PokType updateType() {
        if (this.type.getEvolution() != null) {
            this.type = this.type.getEvolution();
            return this.type;
        }
        return null;
    }

    @Override
    public String toString() {

        return String.format("|--> Pokemon %s he has points as follow { Pc : %f , Pv : %f } %s.|", this.type.getName(),
                this.pc, this.pv, String.format(
                        "|--> The new Pokemon %s %s %s %s %s .|",
                        this.getType().getTypes()[1] != null ? "Types" : "Type",
                        this.getType().getTypes()[1] != null ? "are" : "is", this.getType().getTypes()[0].getEmoji(),
                        this.getType().getTypes()[1] != null ? "," : "",
                        this.getType().getTypes()[1] != null ? this.getType().getTypes()[1].getEmoji() : ""));
    }
}
