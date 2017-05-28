public class ZernikePoint {

    private final Complex[] pTp;
    public final double m;
    private double in; 
    

    public Complex getPosition(int exponent) throws IllegalArgumentException {
        if (exponent < 0 || pTp.length <= exponent) {
            throw new IllegalArgumentException("Exponent is not in correct range.");
        }
        return pTp[exponent];
    }

    public double getIntensity() {
        return in;
    }

    ZernikePoint(Complex position, double m, double in, int maxPower) {
        this.pTp = new Complex[maxPower+1];
        Complex power = new Complex(1.0, 0.0);
        for (int i = 0; i <= maxPower; i++) {
            pTp[i] = power;
            power = power.multiply(position);
        }
        this.m = m;
        this.in = in;
    }

    public void normalizein(double n) throws IllegalArgumentException {
        if (n == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        in /= n;
    }
}
