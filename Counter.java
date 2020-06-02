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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Counter{

    //The only input is the image
    static int numInputs = 1;

    static enum Operations {BLUR, SHARPEN, MEDIAN, THRESHOLD, MAX, MIN};

    static int[][] blurFilter = {{3,5,3},{5,8,5},{3,5,3}};
    static int[][] sharpenFilter ={{0,1,0},{1,-4,1},{0,1,0}};

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
            BufferedImage thresh = ImageOperation(img, Operations.THRESHOLD);
            BufferedImage region = RegionImage(thresh);
            //blur = ImageOperation(img, FilterOperations.MEDIAN);
            //Creates a form with the image
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(region)));
            frame.getContentPane().add(new JLabel(new ImageIcon(thresh)));
            frame.setSize(img.getWidth()*2,img.getHeight()*2);

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
                        newValue = BlurImage(img, blurFilter, x, y);
                        break;
                    case SHARPEN:
                        newValue = SharpenImage(img, sharpenFilter, x, y);
                        break;
                    case MEDIAN:
                        newValue = MedianFilterImage(img, 3, x, y);
                        break;
                    case THRESHOLD:
                        newValue = ThresholdImage(img, 40, x, y);
                        break;
                    case MAX:
                        newValue = MaxImage(img, 40 , x, y);
                        break;
                    case MIN:
                        newValue = MinImage(img, 40 , x, y);
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
    private int SharpenImage(BufferedImage img, int[][] filter, int x, int y){
        int newval = 0;
        for(int i = 0; i < filter.length; i++){
            for(int j = 0; j < filter[i].length; j++){
                int xvalue = x + i -1;
                int yvalue = y + j -1;

                int temp = 0;
                if(IsValidPixel(img, xvalue, yvalue)){
                    Color color = new Color(img.getRGB(xvalue, yvalue));
                    temp = color.getRed();
                }
                else{
                    temp = 255;
                }

                newval += temp * filter[i][j];
            }
        }
        //Calculates the new value of the pixel
        newval = new Color(img.getRGB(x, y)).getRed() - newval;
        
        if(newval > 255){
            return 255;
        }
        else if(newval < 0){
            return 0;
        }
        else{
            return newval;
        }
    }

    //Thresholds the image, where anything less than num is black, anything more is white
    private int ThresholdImage(BufferedImage img, int num, int x, int y){
        int value = (new Color(img.getRGB(x,y))).getRed();
        
        if(value < num){
            return 0;
        }
        else{
            return 255;
        }
    }

    //Thresholds the image
    private int MaxImage(BufferedImage img, int num, int x, int y){
        int value = (new Color(img.getRGB(x,y))).getRed();
        
        if(value < num){
            return value;
        }
        else{
            return 255;
        }
    }

    //Thresholds the image
    private int MinImage(BufferedImage img, int num, int x, int y){
        int value = (new Color(img.getRGB(x,y))).getRed();
        
        if(value < num){
            return 0;
        }
        else{
            return value;
        }
    }

    //Removes noise using the median filter algorithm
    private int MedianFilterImage(BufferedImage img, int size, int x, int y){
        ArrayList<Integer> neighborhood = new ArrayList<Integer>();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                int xvalue = x + i -1;
                int yvalue = y + i -1;

                int val = 0;
                if(IsValidPixel(img, xvalue, yvalue)){
                    val = new Color(img.getRGB(xvalue, yvalue)).getRed();
                }
                else{
                    val = 255;
                }
                neighborhood.add(val);             
            }
        }
        SortArray(neighborhood);
        return neighborhood.get(4);
    }

    //Bubble sorts the array
    private void SortArray(ArrayList<Integer> list){
        for (int i = 0; i < list.size()-1; i++){       
            for (int j = 0; j < list.size()-i-1; j++){
                if (list.get(j) > list.get(j+1)){
                    //Swap
                    int temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);
                }
            }  
        }
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
        //Input is binary, black and white image
        //Creates a new image
        BufferedImage newimg = CopyImage(img);

        //Background is 255
        int background = 0;
        //foreground is 0
        int foreground = 255;

        int label = 1;
        for(int x = 0; x < newimg.getWidth(); x++){
            //For each pixel in the image
            for(int y = 0; y < newimg.getHeight(); y++){
                int value = (new Color(newimg.getRGB(x,y))).getRed();
                if(value == foreground){
                    Floodfill(newimg, x, y, label, foreground);
                    label ++;
                }
            }
        }
        System.out.println(label - 1);
        return newimg;
    }

    //Copies the image for regioning
    private BufferedImage CopyImage(BufferedImage img){
        //Creates a new image
        BufferedImage newimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for(int i = 0; i < img.getWidth(); i++){
            for(int j = 0; j < img.getHeight(); j++){
                newimg.setRGB(i, j, img.getRGB(i,j));
            }
        }

        return newimg;
    }

    //Floodfills a region
    private void Floodfill(BufferedImage img, int x, int y, int label, int foreground){
        if(IsValidPixel(img, x, y)){
            int value = (new Color(img.getRGB(x,y))).getRed();
            if(value == foreground){
                //Sets the value of the pixel
                Color color = new Color(label, label, label);
                img.setRGB(x, y, color.getRGB());

                //Recursively floodfills the rest of the region
                Floodfill(img, x+1, y, label, foreground);
                Floodfill(img, x-1, y, label, foreground);
                Floodfill(img, x, y+1, label, foreground);
                Floodfill(img, x, y-1, label, foreground);
            }
        } 
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
                if(IsValidPixel(img, xpixelcoord, ypixelcoord)){
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

    private Boolean IsValidPixel(BufferedImage img, int x, int y){
        return x >= 0 && x < img.getWidth() && y >= 0 && y <img.getHeight();
    }
}
    