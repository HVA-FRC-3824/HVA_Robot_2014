/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package balldetection;

import java.util.Vector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.*;
import static org.opencv.imgproc.Imgproc.CV_HOUGH_GRADIENT;
import static org.opencv.imgproc.Imgproc.GaussianBlur;

/**
 *
 * @author FIRST
 */
public class BallDetection {
    static VideoCapture camera = new VideoCapture(0);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        camera.open(0); //Useless
        Mat rgb = getVideo();
        Mat hsv = new Mat();
        
        Imgproc.cvtColor(rgb, hsv, Imgproc.COLOR_RGB2HSV);
        GaussianBlur(hsv, hsv, new Size(9,9), 2, 2);
        Core.inRange(hsv, new Scalar(20, 100, 100), new Scalar(30, 255, 255), hsv);
        Mat circles = new Mat();
        Vector circlesV = new Vector();
        Imgproc.HoughCircles(hsv, circles, CV_HOUGH_GRADIENT, 2.0, (double)hsv.rows()/4);//, 200, 100);
        
        for(int i = 0; i < circles.rows(); i++)
        {
            //Point center = new Point((int)Math.round(circles[i][0]), (int)Math.round(circles[i][1]));
        }
               
        
    }
    
    private static Mat getVideo() {
        Mat frame = new Mat();
        //camera.grab();
        //System.out.println("Frame Grabbed");
        //camera.retrieve(frame);
        //System.out.println("Frame Decoded");
        camera.read(frame);
        return frame;
    }
    
}
