/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package balldetection;

import static balldetection.Webcam.toBufferedImage;
import java.io.IOException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.*;
import static org.opencv.imgproc.Imgproc.CV_HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import java.util.Date;
/**
 *
 * @author FIRST
 */
public class BallDetection {

    private static int counter = 0;
    
    static VideoCapture camera;
    
    private static Mat gray, src, hsv, filter, dst;
        
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CameraWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        CameraWindow cWindow = new CameraWindow();
        cWindow.setVisible(true);
        
        int radius = 0;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        //intialization of matrices
        Mat circles = new Mat();
        gray = new Mat(); hsv = new Mat();
        filter = new Mat(); dst = new Mat();
        
        camera = new VideoCapture(0);
        Mat frame = new Mat();
        Webcam.ImagePanel panel = Webcam.createPanel(camera, "src");
        Webcam.ImagePanel panel2 = Webcam.createPanel(camera, "filter");
        Webcam.ImagePanel panel3 = Webcam.createPanel(camera, "dst");
        
        while (true) 
        {
            
            camera.read(frame);
            src = frame;

            GaussianBlur(src, src, new Size(3,3), 2, 2);
            Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
            
            
            Core.inRange(gray, new Scalar(20, 100, 100), new Scalar(30, 255, 255), gray);
            Core.inRange(hsv, new Scalar(cWindow.get_hLower(), cWindow.get_sLower(), cWindow.get_vLower()), 
                    new Scalar(cWindow.get_hUpper(), cWindow.get_sUpper(), cWindow.get_vUpper()), filter);
            
            Core.inRange(src, new Scalar(cWindow.get_hLower(), cWindow.get_sLower(), cWindow.get_vLower()), 
                    new Scalar(cWindow.get_hUpper(), cWindow.get_sUpper(), cWindow.get_vUpper()), dst);
           
            double[] temp = hsv.get(hsv.rows()/2, hsv.cols()/2);
            System.out.println(temp[0] + ", " + temp[1] + ", " + temp[2] + ", " + radius);
            //System.out.println("Current Distance from ball: " + ((2.5366*radius) - 123.02));
            
            Imgproc.HoughCircles(filter, circles, CV_HOUGH_GRADIENT, cWindow.get_dp(), filter.rows()/2, 
                    cWindow.get_param1(), cWindow.get_param2(), 
                    cWindow.get_minCircleSize(), cWindow.get_maxCircleSize());

            for(int i = 0; i < circles.cols(); i++)
            {
                Point center = new Point(Math.round(circles.get(0,i)[0]), Math.round(circles.get(0,i)[1]));
                radius = (int)Math.round(circles.get(0,i)[2]);
                // draw the circle center
                Core.circle(src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
                // draw the circle outline
                Core.circle(src, center, radius, new Scalar(0,0,255), 3, 8, 0 );
                //System.out.println("" + circles.get(0,0)[0] + ", " + circles.get(0,0)[1] + ", " + circles.get(0,0)[2]);
            }  

            panel.updateImage(toBufferedImage(src));
            panel2.updateImage(toBufferedImage(filter));
            panel3.updateImage(toBufferedImage(dst));
        }
    }
    
    public static void takeScreenshot(int mat)
    {
        Date tempDate = new Date();
        Mat matFrame = new Mat();
        
        switch(mat)
        {
            case 1:
                matFrame = src;
                break;
            case 2:
                matFrame = dst;
                break;
            case 3:
                matFrame = hsv;
                break;
            default:
                matFrame = filter;
                break;
        }
        
        //camera.read(matFrame);
        Highgui.imwrite("screenshots\\ Screenshot " + counter + " -" + String.format("%1$s %2$tb %2$td at %2$tH %2$tM %2$tS","", tempDate) + ".jpeg", matFrame);
        counter++;
    }
}
