
public class Complex {

    public final double real;
    public final double imag;

    Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex conjugate() {
        return new Complex(real, -1 * imag);
    }

    public Complex add(Complex z) {
        return new Complex(real + z.real, imag + z.imag);
    }

    public Complex multiply(double x) {
        return new Complex(real * x, imag * x);
    }

    public Complex multiply(Complex z) {
        return new Complex(real * z.real - imag * z.imag, real * z.imag + imag * z.real);
    }

    public double modulus() {
        return Math.hypot(real, imag);
    }
}
