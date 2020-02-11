public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    private static final double G = 6.67e-11;

    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet other) {
        // return Math.sqrt(Math.pow(this.xxPos - other.xxPos, 2) + Math.pow(this.yyPos
        // - other.yyPos, 2));
        double xxDiff = this.xxPos - other.xxPos;
        double yyDiff = this.yyPos - other.yyPos;
        return Math.sqrt(xxDiff * xxDiff + yyDiff * yyDiff);
    }

    public double calcForceExertedBy(Planet other) {
        double dist = calcDistance(other);
        return G * this.mass * other.mass / (dist * dist);
    }

    public double calcForceExertedByX(Planet other) {
        double dist = calcDistance(other);
        double force = calcForceExertedBy(other);
        return (other.xxPos - this.xxPos) / dist * force;
    }

    public double calcForceExertedByY(Planet other) {
        double dist = calcDistance(other);
        double force = calcForceExertedBy(other);
        return (other.yyPos - this.yyPos) / dist * force;
    }

    public double calcNetForceExertedByX(Planet[] others) {
        double totalForce = 0;
        for (Planet other : others) {
            if (this.equals(other))
                continue;
            totalForce += calcForceExertedByX(other);
        }
        return totalForce;
    }

    public double calcNetForceExertedByY(Planet[] others) {
        double totalForce = 0;
        for (Planet other : others) {
            if (this.equals(other))
                continue;
            totalForce += calcForceExertedByY(other);
        }
        return totalForce;
    }

    public void update(double duration, double xxForce, double yyForce) {
        double xxAcc = xxForce / this.mass;
        double yyAcc = yyForce / this.mass;
        double newXXVel = this.xxVel + duration * xxAcc;
        double newYYVel = this.yyVel + duration * yyAcc;
        this.xxVel = newXXVel;
        this.yyVel = newYYVel;
        this.xxPos = this.xxPos + duration * newXXVel;
        this.yyPos = this.yyPos + duration * newYYVel;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

}
