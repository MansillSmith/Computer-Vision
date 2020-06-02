/*
Mansill Smith
ID: 1341291

Alex Grant
ID: 1350168
*/

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Counter{

    //The only input is the image
    static int numInputs = 1;

    public static void main(String[] args){
        if(args.length == numInputs){
            Counter counter = new Counter(args[0]);
        }
    }

    public Counter(String filename){
        //Read in the file
        BufferedImage img = null;
        try{
            img=ImageIO.read(new File(filename));
        }
        catch (IOException e){
            img = null;
        }

        if(img == null){
            System.err.println("Invalid file");
        }
        else{
            //Creates a form with the image
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(img)));
            frame.setSize(img.getWidth(),img.getHeight());

            //Do some point operations on the image

            //Do edge detection somewhere

            //Threshold the image

            //Shrink / grow the shapes to make them solid and remove noise

            //Do region detection

            //Output the number of regions detected

            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    //Seperates the image into regions
    private BufferedImage CalculateRegions(BufferedImage img){
        return img;
    }

    //Sharpens the image
    private BufferedImage SharpenImage(BufferedImage img){
        return img;
    }

    //Thresholds the image, where anything less than num is black, anything more is white
    private BufferedImage ThresholdImage(BufferedImage img, int num){
        return img;
    }

    //Thresholds the image
    private BufferedImage MaxImage(BufferedImage img, int num){
        return img;
    }

    //Thresholds the image
    private BufferedImage MinImage(BufferedImage img, int num){
        return img;
    }

    //Removes noise using the median filter algorithm
    private BufferedImage MedianFilterImage(BufferedImage img){
        return img;
    }

    //Shrinks the shapes in the image
    private BufferedImage ShrinkImage(BufferedImage img){
        return img;
    }

    //Grows the shapes in the image
    private BufferedImage GrowImage(BufferedImage img){
        return img;
    }

    //Shrinks the shapes in the image
    private BufferedImage RegionImage(BufferedImage img){
        return img;
    }

    //Calculates the edges of an image
    private BufferedImage KirshEdgeFilter(BufferedImage img){
        return img;
    }

    //Blurs the image
    private BufferedImage BlurImage(BufferedImage img){
        return img;
    }
}
    