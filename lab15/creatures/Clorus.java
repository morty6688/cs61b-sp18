package creatures;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;
    /** energy to lose when moving. */
    private static final double moveEnergyLost = 0.03;
    /** energy to lose when staying. */
    private static final double stayEnergyLost = 0.01;

    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public void move() {
        energy = energy - moveEnergyLost;
    }

    @Override
    public Clorus replicate() {
        energy /= 2;
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy = energy - stayEnergyLost;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plip = getNeighborsOfType(neighbors, "plip");

        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (!plip.isEmpty()) {
            Direction moveDir = HugLifeUtils.randomEntry(plip);
            return new Action(Action.ActionType.ATTACK, moveDir);
        } else if (energy >= 1) {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, moveDir);
        } else {
            Direction moveDir = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, moveDir);
        }
    }

}
