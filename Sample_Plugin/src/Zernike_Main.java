
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;

//import ij.gui.Roi;
import ij.plugin.PlugIn;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
//import ij.process.ImageProcessor;

public class Zernike_Main extends ImagePlus implements PlugIn{


    public static int computeF(final int m, final int n, final int s) {
        assert ((m + Math.abs(n)) % 2) == 0;
        assert ((m - Math.abs(n)) % 2) == 0;
        assert (m - Math.abs(n)) >= 0;
        assert (((m - Math.abs(n)) / 2) - s) >= 0;

        final int absN = Math.abs(n);

        final FactorialComputer fc = new FactorialComputer(m);
        fc.multiplyByFactorialOf(m - s);
        fc.divideByFactorialOf(s);
        fc.divideByFactorialOf(((m + absN) / 2) - s);
        fc.divideByFactorialOf(((m - absN) / 2) - s);

        return fc.value();
    }
    
    public static Polynom createR(final int m, final int n) {
        final Polynom result = new Polynom(m);
        int sign = 1;
        for (int k = 0; k <= ((m - Math.abs(n)) / 2); ++k) {
            final int pos = m - (2 * k);
            result.setCoefficient(pos, sign * computeF(m, n, k));
            sign = -sign;
        }
        return result;
    }

    @Override
    public void run(String arg0) {
        // TODO Auto-generated method stub
        ///home/mg/Pictures/aaa.png
    	String path="/home/mg/Downloads/tifs/image.tif";
    	ImagePlus imp=IJ.openImage(path);
    	ImageStack stack = imp.getStack();
    	
    	ImageConverter ic=new ImageConverter(imp);
    	ic.convertToGray8();
    	
    	ImageProcessor ip=imp.getProcessor();
       	byte[]  arr=(byte[]) ip.getPixels();
        
       	final int centerX = imp.getWidth() / 2;
        final int centerY = imp.getHeight() / 2;
        final int max = Math.max(centerX, centerY);
        final double radius = Math.sqrt(2 * max * max);
        
        //zernike moment of order 10 and azimuthal repetition 8 has been taken
        int n=10; //order
        int m=8; //repetiton
        
        
        
        final Polynom polynomOrthogonalRadial = createR(10, 8);
        double real = 0;
        double imag = 0;
        for(int i=0;i<imp.getHeight();i++){
        	for(int j=0;j<imp.getWidth();j++){
        		final int x = j-centerX;
        		final int y = i-centerY;
        		 final double r = Math.sqrt((x * x) + (y * y)) / radius;
        		 final double ang = m * Math.atan2(y, x);
        		 final double value = polynomOrthogonalRadial.evaluate(r);
        		 final int pixel = imp.getPixel(x, y)[0];
        		 real += pixel * value * Math.cos(ang);
        		 imag -= pixel * value * Math.sin(ang);
        		 
        		 
        	}
        	
        }
        real = (real * (m + 1)) / Math.PI;
		imag = (imag * (m + 1)) / Math.PI;
		Complex result = new Complex(real, imag);
        
		System.out.println(result.getReal());
               
    }

    public static void main(String[] args) {    	
    	new Zernike_Main().run("");

    }
}
