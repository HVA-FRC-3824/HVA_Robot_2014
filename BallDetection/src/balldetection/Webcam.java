/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package balldetection;

/**
 *
 * @author FIRST
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

class Webcam {

    private static final int WIDTH = 640, HEIGHT = 480;

    
    private static int counter = 1;
    static VideoCapture camera = new VideoCapture(0);
    
    public static Image toBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            Mat m2 = new Mat();
            Imgproc.cvtColor(m, m2, Imgproc.COLOR_BGR2RGB);
            type = BufferedImage.TYPE_3BYTE_BGR;
            m = m2;
        }
        byte[] b = new byte[m.channels() * m.cols() * m.rows()];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        image.getRaster().setDataElements(0, 0, m.cols(), m.rows(), b);
        return image;
    }

    public static class ImagePanel extends JPanel {
        private Image image;
        public void updateImage(Image image) {
            this.image = image;
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }
    
    public static ImagePanel createPanel(final VideoCapture camera, String frameName) throws IOException {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                camera.release();
                System.exit(0);
            }
        });
        ImagePanel panel = new ImagePanel();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setName(frameName);
        
        //frame.getOwner().setName(frameName);
        frame.setVisible(true);
        return panel;
    }

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      //  VideoCapture camera = new VideoCapture(0);
        Mat frame = new Mat();
        ImagePanel panel = createPanel(camera, "camera");
        while (true) {
            camera.read(frame);
            panel.updateImage(toBufferedImage(frame));
        }
    }
    
    public static void takeScreenshot()
    {
        Mat matFrame = new Mat();
        camera.read(matFrame);
        Highgui.imwrite("screenshots\\screenshot " + counter + ".jpeg", matFrame);
        counter++;
    }
}

