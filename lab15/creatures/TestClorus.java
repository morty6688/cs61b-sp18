package creatures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.awt.Color;
import java.util.HashMap;

import org.junit.Test;

import huglife.Action;
import huglife.Direction;
import huglife.Empty;
import huglife.Impassible;
import huglife.Occupant;

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(1.97, p.energy(), 0.01);
        p.move();
        assertEquals(1.94, p.energy(), 0.01);
        p.stay();
        assertEquals(1.93, p.energy(), 0.01);
        p.stay();
        assertEquals(1.92, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        Clorus offspring = p.replicate();
        assertNotSame(offspring, p);
        assertEquals(1, p.energy(), 0.01);
        assertEquals(1, offspring.energy(), 0.01);
    }

    @Test
    public void testChoose() {
        Clorus p = new Clorus(2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        p = new Clorus(2);
        surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Plip());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Empty());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);

        p = new Clorus(1);
        surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Empty());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);

        assertEquals(expected, actual);

        p = new Clorus(0.5);
        surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Empty());
        actual = p.chooseAction(surrounded);
        expected = new Action(Action.ActionType.MOVE, Direction.RIGHT);

        assertEquals(expected, actual);
    }
}
