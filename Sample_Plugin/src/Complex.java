
public class Complex {
        /** real part. */
        private double m_real;

        /** imaginary part. */
        private double m_imaginary;

        public Complex(final double real) {
            m_real = real;
            m_imaginary = 0;
        }

        public Complex(final double real, final double imaginary) {
            m_real = real;
            m_imaginary = imaginary;
        }

        public double getReal() {
            return m_real;
        }

        public double getImaginary() {
            return m_imaginary;
        }

        public Complex multiplyTo(final Complex c) {
            return new Complex((this.m_real * c.m_real) - (this.m_imaginary * c.m_imaginary),
                    (this.m_real * c.m_imaginary) + (this.m_imaginary * c.m_real));
        }

        public void add(final Complex c) {
            m_real += c.m_real;
            m_imaginary += c.m_imaginary;
        }
        
        public Complex conjugate() {
            return new Complex(this.m_real, this.m_imaginary);
        }

        public double abs() {
            return Math.sqrt((m_real * m_real) + (m_imaginary * m_imaginary));
        }
 }

