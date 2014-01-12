/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package balldetection;

import static balldetection.Webcam.toBufferedImage;
import java.io.IOException;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();
        Webcam.ImagePanel panel = Webcam.createPanel(camera);
        while (true) {
            Mat gray = new Mat(), src, circles = new Mat();
            Mat hsv = new Mat(), filter = new Mat();
            camera.read(frame);
            src = frame;

            Imgproc.cvtColor(src, hsv, Imgproc.COLOR_BGR2HSV);
            //GaussianBlur(gray, gray, new Size(9,9), 2, 2);
            //Core.inRange(gray, new Scalar(20, 100, 100), new Scalar(30, 255, 255), gray);
            Core.inRange(hsv, new Scalar(75, 30, 140), new Scalar(140, 130, 255), filter);
            //Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(0, 255, 0), filter);
            //Core.inRange(hsv, new Scalar(0, 0, 0), new Scalar(255, 0, 0), filter);
            
            //Imgproc.cvtColor(redOnly, gray, Imgproc.color_h );
            double[] temp = hsv.get(hsv.rows()/2, hsv.cols()/2);
            System.out.println(temp[0] + ", " + temp[1] + ", " + temp[2]);
            
            Imgproc.HoughCircles(filter, circles, CV_HOUGH_GRADIENT, 1, filter.rows()/16, 200, 100, 0, 0);

            for(int i = 0; i < circles.cols(); i++)
            {
                Point center = new Point(Math.round(circles.get(0,i)[0]), Math.round(circles.get(0,i)[1]));
                int radius = (int)Math.round(circles.get(0,i)[2]);
                // draw the circle center
                Core.circle(src, center, 3, new Scalar(0,255,0), -1, 8, 0 );
                // draw the circle outline
                Core.circle(src, center, radius, new Scalar(0,0,255), 3, 8, 0 );
                //System.out.println("" + circles.get(0,0)[0] + ", " + circles.get(0,0)[1] + ", " + circles.get(0,0)[2]);
            }  

            panel.updateImage(toBufferedImage(hsv));
        }
    }
}
