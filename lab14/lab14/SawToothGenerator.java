package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int state;
    private int period;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state++;
        return normalize(state % period);
    }

    private double normalize(int x) {
        return (double) x * 2 / period - 1;
    }
}
