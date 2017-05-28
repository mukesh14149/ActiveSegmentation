
import ij.IJ;
import ij.ImagePlus;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

//import ij.gui.Roi;
import ij.plugin.PlugIn;
import ij.process.ImageConverter;
//import ij.process.ImageProcessor;

public class First_Plugin extends ImagePlus implements PlugIn{

    static double factorial(int n) throws IllegalArgumentException {
        int x = 1;
        for (int i = 2; i <= n; i++) {
            x *= (double) i;
        }
        return x;
}

    private static Complex zernikeMoment(ZernikePoint[] zps, int zpsSize, int n, int m) {
        int p = (n - m) / 2;
        int q = (n + m) / 2;
        Complex v = new Complex(0, 0);
        double[] gm = new double[p + 1];

        for (int i = 0; i <= p; i += 2) {
            gm[i] = factorial(n - i) / (factorial(i) * factorial(q - i) * factorial(p - i));
        }
        for (int i = 1; i <= p; i += 2) {
            gm[i] = -factorial(n - i) / (factorial(i) * factorial(q - i) * factorial(p - i));
        }

        for (int i = 0; i < zpsSize; i++) {
            double r = zps[i].m;
            Complex z = zps[i].getPosition(m);
            double c = zps[i].getIntensity();
            Complex Vnl = new Complex(0, 0);
            for (int j = 0; j <= p; j++) {
                Vnl = Vnl.add(z.multiply(gm[j] * Math.pow(r, n - 2 * j)));
            }
            v = v.add(Vnl.conjugate().multiply(c));
        }
        v = v.multiply((n + 1) / Math.PI);
        return v;
    }

    public static double[] zernikeMoments(BufferedImage img, int degree, Point2D center, double radius) {
        if (img.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            BufferedImage t = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            t.getGraphics().drawImage(img, 0, 0, null);
            img = t;
        }
        byte[] imgBytes = ((DataBufferByte) img.getData().getDataBuffer()).getData();
        return zernikeMoments(imgBytes, img.getWidth(), degree, center, radius);
    }

    public static double[] zernikeMoments(byte[] img, int imgWidth, int degree, Point2D center, double radius) {
        int imgHeight = img.length / imgWidth;
        int zpsSize = (int) (Math.PI * radius * radius);
        ZernikePoint[] zps = new ZernikePoint[zpsSize];
        int totalIntensity = 0;

        int i = 0;
        for (int y0 = 0; y0 < imgHeight; y0++) {
            double y = (y0 - center.getY()) / radius;
            for (int x0 = 0; x0 < imgWidth; x0++) {
                double x = (x0 - center.getX()) / radius;
                double r = Math.hypot(x, y);
                if (r <= 1) {
                    int c = img[x0 + y0 * imgWidth] & 0xFF;
                    if (c > 0) {
                        r = Math.max(r, 1e-9);
                        Complex z = new Complex(x / r, y / r);
                        zps[i] = new ZernikePoint(z, r, c, degree);
                        totalIntensity += c;
                        i++;
                    }
                }
            }
        }
        zpsSize = i;
        for (i = 0; i < zpsSize; i++) {
            zps[i].normalizein(totalIntensity);
        }

        double[] zvalues = new double[(int) (2 * Math.pow(degree, 2) + 4 * degree - Math.pow(-1, degree) + 1) / 8];
        i = 0;
        for (int n = 0; n < degree; n++) {
            for (int m = 0; m <= n; m++) {
                if ((n - m) % 2 == 0) {
                    Complex z = zernikeMoment(zps, zpsSize, n, m);
                    zvalues[i] = z.modulus();
                    i++;
                }
            }
        }
        return zvalues;

    }


    @Override
    public void run(String arg0) {
        // TODO Auto-generated method stub
        ///home/mg/Pictures/aaa.png
        ImagePlus imp = IJ.openImage("http://imagej.net/images/clown.jpg");
        imp.show();
      //  System.out.println("dddddd");

        final int centerX = imp.getWidth() / 2;
        final int centerY = imp.getHeight() / 2;
        final int max = Math.max(centerX, centerY);
        final double radius = Math.sqrt(2 * max * max);
        Point2D center=new Point2D.Double(centerX, centerY);

        int degree=10;
        double[] result=zernikeMoments(imp.getBufferedImage(),degree, center, radius);
        System.out.println(result.length);
        /*ImageProcessor ip = imp.getProcessor();
        Roi roi = new Roi(30, 40, 100, 100); // x, y, width, height of the rectangle
        ip.setRoi(roi);
        ip.setValue(255);
        ip.fill();
        */
        for(int i=0;i<result.length;i++){
            System.out.println(result[i]);
        }


        ImageConverter ic=new ImageConverter(imp);
        ic.convertToGray8();
        imp.updateAndDraw();

    }

    public static void main(String[] args) {
        //System.out.println(System.getProperty("plugins.dir"));
        new ij.ImageJ();
        new First_Plugin().run("");
    }
}
