import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class Zernike_Main2 {
	
	public static double calculateRadial(double r, int m, int n){
	//	System.out.println(r+" "+m+" "+n);
		if(n==0&&m==0){
			return 1;
		}
		else if(n<m || (n-m)%2!=0)
			return 0;
		else{
			if(m-1<0)
				return ((2*r*calculateRadial(r,m+1,n-1))-calculateRadial(r,m,n-2));
			else if(n==m){
				return (r*calculateRadial(r,m-1,n-1));
			}else{
				return (r*(calculateRadial(r,m-1,n-1)+calculateRadial(r,m+1,n-1))-calculateRadial(r,m,n-2));
			}
				
		}
	}
	
	public static void main(String[] args){
		String path="/home/mg/Downloads/tifs/image.tif";
    	ImagePlus imp=IJ.openImage(path);
//    	ImageStack stack = imp.getStack();
    	
    	ImageConverter ic=new ImageConverter(imp);
    	ic.convertToGray8();
    	
    	ImageProcessor ip=imp.getProcessor();
       	byte[]  arr=(byte[]) ip.getPixels();
        
       	final int centerX = imp.getWidth() / 2;
        final int centerY = imp.getHeight() / 2;
        final int max = Math.max(centerX, centerY);
        final double radius = Math.sqrt(2 * max * max);
        
        //zernike moment of order 10 and azimuthal repetition 8 has been taken
        int n=8; //order
        int m=6; //repetiton
        
        
        long startTime = System.currentTimeMillis();
        double real = 0;
        double imag = 0;
        int total=0;
        for(int i=0;i<imp.getHeight();i++){
        	for(int j=0;j<imp.getWidth();j++){
        		total += imp.getPixel(i, j)[0];
       		 
        	}
        }	
        for(int i=0;i<imp.getHeight();i++){
        	for(int j=0;j<imp.getWidth();j++){
        		final int x = j-centerX;
        		final int y = i-centerY;
        		 final double r = Math.sqrt((x * x) + (y * y)) / radius;
        		 
        		// System.out.println(r);
        		 double radial = calculateRadial(r,m,n);
        		 final double ang = m * Math.atan2(y, x);
                	
        		 //final double value = polynomOrthogonalRadial.evaluate(r);
        		 double pixel = imp.getPixel(x, y)[0];
        		 pixel=pixel/255;
        		 
        		 real += pixel * radial * Math.cos(ang);
        		 imag -= pixel * radial * Math.sin(ang);
        		// System.out.println(pixel+"hh"+total);
     			//System.out.println(radial+"sss"+ang+"hh"+real+"jj"+pixel);

        		 //break;
        	}
        	//break;
        }
        long endTime   = System.currentTimeMillis();
		 long totalTime = endTime - startTime;
		System.out.println(totalTime);
        real = (real * (m + 1)) / Math.PI;
		imag = (imag * (m + 1)) / Math.PI;
		Complex result = new Complex(real, imag);
        
		System.out.println(result.getReal());

	}
}
