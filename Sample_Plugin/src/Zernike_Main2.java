import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class Zernike_Main2 {
	
	// Array Zps object
	public static Zps[] zps;
	
	public static double calculateRadial(double r, int m, int n, Zps zps){
		//Check if radial value for m and n already present.
		if(zps.get(m, n)!=0){
			return zps.get(m, n);
		}
		if(n==0&&m==0){
			return 1;
		}
		else if(n<m || (n-m)%2!=0)
			return 0;
		else{
			if(m-1<0)
				return ((2*r*calculateRadial(r,m+1,n-1,zps))-calculateRadial(r,m,n-2,zps));
			else if(n==m){
				return (r*calculateRadial(r,m-1,n-1,zps));
			}else{
				return (r*(calculateRadial(r,m-1,n-1,zps)+calculateRadial(r,m+1,n-1,zps))-calculateRadial(r,m,n-2,zps));
			}
				
		}
	}
	
	public static void main(String[] args){
		String path="/home/mg/Downloads/tifs/image.tif";
    	ImagePlus imp=IJ.openImage(path);
//    	ImageStack stack = imp.getStack();
    	
    	//Converted into Grayscale image
    	ImageConverter ic=new ImageConverter(imp);
    	ic.convertToGray8();
    	
    	ImageProcessor ip=imp.getProcessor();
       	byte[]  arr=(byte[]) ip.getPixels();
        
       	final int centerX = imp.getWidth() / 2;
        final int centerY = imp.getHeight() / 2;
        final int max = Math.max(centerX, centerY);
        final double radius = Math.sqrt(2 * max * max);
        
        //zernike moment of order 6 and azimuthal repetition 4 has been taken
        int n=6; //order
        int m=4; //repetiton
        
        
        long startTime = System.currentTimeMillis();
        
        
        ArrayList<Double> real = null; //list contain zernike moment real values upto order 6,4 
    	ArrayList<Double> imag = null;
        
    	int index=0;
        //Need to create Zps object for each pixel.
        zps=new Zps[imp.getHeight()*imp.getWidth()];
        
        
        for(int i=0;i<imp.getHeight();i++){
        	for(int j=0;j<imp.getWidth();j++){
        		final int x = j-centerX;
        		final int y = i-centerY;
        		final double r = Math.sqrt((x * x) + (y * y)) / radius;
        		//For each pixel create zps object
        		zps[index]=new Zps(m,n);
        		
        		real=new ArrayList<Double>();
        		imag=new ArrayList<Double>();
        		
        		for(int k=0;k<n;k++){
        			for(int l=0;l<m;l++){
        				
        				if((k-l)%2==0){
        					//Calculate radial_value
        					double radial_value = calculateRadial(r, l, k, zps[index]);
        					final double ang = l * Math.atan2(y, x);
        					double pixel = imp.getPixel(x, y)[0];
        					real.add(pixel * radial_value * Math.cos(ang));
        	        		imag.add(pixel * radial_value * Math.sin(ang));
        					zps[index].set(l, k, radial_value, ang, pixel); 
        				}
        			}
        		}
        		
        		zps[index].setComplex(real, imag);
        		index++;
        		
        		 	
         	}
       }
        
        double[] real_result=new double[real.size()];
        for(int i=0;i<zps.length;i++){
        	ArrayList<Double> temp=zps[i].getReal();
        	for(int j=0;j<temp.size();j++){
        		real_result[j]+=(temp.get(j)* (m + 1)) / Math.PI;
        	}
        }
        
        System.out.println(real_result.length);
        for(int i=0;i<real_result.length;i++){
        	System.out.println(real_result[i]);
        }
		
	}
}
