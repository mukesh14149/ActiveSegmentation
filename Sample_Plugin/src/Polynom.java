
public class Polynom {
	
	/** the array of polynom coefficients. */
	private final int[] m_coefficients;

    /** the degree of the polynom. */
    private final int m_degree;

    public Polynom(final int degree) {
        m_degree = degree;
        m_coefficients = new int[m_degree + 1];
        for (int i = 0; i <= m_degree; ++i) {
            setCoefficient(i, 0);
        }
    }

    public void setCoefficient(final int pos, final int coef) {
        m_coefficients[pos] = coef;
    }


    public int getCoefficient(final int pos) {
        return m_coefficients[pos];
    }


    public double evaluate(final double x) {
        double power = 1.0;
        double result = 0.0;
        for (int i = 0; i <= m_degree; ++i) {
            result += m_coefficients[i] * power;
            power *= x;
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i <= m_degree; ++i) {
            if (m_coefficients[i] != 0) {
                result.append(m_coefficients[i] + "X^" + i + "  ");
            }
        }
        return result.toString();
    }
}
