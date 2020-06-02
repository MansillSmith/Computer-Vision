/*
Mansill Smith
ID: 1341291

Alex Grant
ID: 1350168
*/

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.rmi.server.RMIClassLoader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class Counter{

    //The only input is the image
    static int numInputs = 1;

    static enum Operations {BLUR, SHARPEN};

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
            BufferedImage blur = ImageOperation(img, Operations.BLUR);
            blur = ImageOperation(blur, Operations.BLUR);
            blur = ImageOperation(blur, Operations.BLUR);
            blur = ImageOperation(blur, Operations.BLUR);
            blur = ImageOperation(blur, Operations.BLUR);
            blur = ImageOperation(blur, Operations.BLUR);
            blur = ImageOperation(blur, Operations.BLUR);
            //Creates a form with the image
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(blur)));
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

    private BufferedImage ImageOperation(BufferedImage img, Operations operation){
        //Creates a new image
        BufferedImage newimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        //for each line
        for(int y = 0; y < newimg.getHeight(); y++){
            //for each pixel
            for(int x = 0; x < newimg.getWidth(); x++){
                //Calculate the new value of the pixel
                int newValue = 0;

                switch(operation){
                    case BLUR:
                        int[][] filter = {{3,5,3},{5,8,5},{3,5,3}};
                        newValue = BlurImage(img, filter, x, y);
                        break;
                    case SHARPEN:
                        break;

                }

                Color color = new Color(newValue, newValue, newValue);
                newimg.setRGB(x, y, color.getRGB());
            }
        }
        return newimg;
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
    private int BlurImage(BufferedImage img, int[][] filter, int x, int y){
        //Calculate the new value of the pixel
        int newValue = 0;
        int total = 0;
        //Uses the filter to get the ratio of the neighboring pixels
        for(int i = 0; i < filter.length; i++){
            for(int j = 0; j < filter[i].length; j++){
                int xpixelcoord = x + i -1;
                int ypixelcoord = y + j -1;
                

                int pixelValue = 0;
                //If the new point is valid
                if(xpixelcoord >= 0 && xpixelcoord < img.getWidth() && ypixelcoord >= 0 && ypixelcoord <img.getHeight()){
                    Color color = new Color(img.getRGB(xpixelcoord, ypixelcoord));
                    //The image is greyscale
                    pixelValue = color.getRed();
                }
                else{
                    pixelValue = 255;
                }

                newValue += pixelValue * filter[i][j];
                total += filter[i][j];
            }
        }

        return newValue/total;
    }
}
    