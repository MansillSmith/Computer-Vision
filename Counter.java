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
            img = ImageIO.read(new File(filename));
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

            //Auto contrast the image to make all the cells of all the image the same set of colours
            BufferedImage autoContrast = AutoContrast(img);
            //Turn everything darker than 115 to black, and everything lighter to white
            BufferedImage threshold = ImageOperation(autoContrast,Operations.THRESHOLD, 115);
            //Shrink and grow the image to remove noise
            BufferedImage shrunk = ShrinkImage(threshold, true);
            shrunk = ShrinkImage(shrunk, true);
            shrunk = ShrinkImage(shrunk, true);
            shrunk = GrowImage(shrunk, true);
            shrunk = GrowImage(shrunk, true);
            shrunk = GrowImage(shrunk, true);
            shrunk = ShrinkImage(shrunk, true);
            
            //Calculate the number of regions on the image
            int regions = RegionImage(shrunk);

            Color textColour = new Color(255,0,0);

            //Add the images to the form
            JLabel displayImage = new JLabel(new ImageIcon(img));
            JLabel text = new JLabel("Original Image");
            text.setLocation(0, 0);
            text.setSize(150,15);
            text.setForeground(textColour);
            displayImage.add(text);
            frame.getContentPane().add(displayImage);

            JLabel displayAutoContrast = new JLabel(new ImageIcon(autoContrast));
            text = new JLabel("Auto Constrast Image");
            text.setLocation(0, 0);
            text.setSize(150,15);
            text.setForeground(textColour);
            displayAutoContrast.add(text);
            frame.getContentPane().add(displayAutoContrast);

            JLabel displayThreshold = new JLabel(new ImageIcon(threshold));
            text = new JLabel("Threshold Image");
            text.setLocation(0, 0);
            text.setSize(150,15);
            text.setForeground(textColour);
            displayThreshold.add(text);
            frame.getContentPane().add(displayThreshold);

            JLabel displayShrunkAndGrown = new JLabel(new ImageIcon(shrunk));
            text = new JLabel("Shrunk and grown image");
            text.setLocation(0, 0);
            text.setSize(150,15);
            text.setForeground(textColour);
            displayShrunkAndGrown.add(text);
            frame.getContentPane().add(displayShrunkAndGrown);

            JLabel numberOfRegions = new JLabel("Regions: " + regions);
            text.setLocation(0, 0);
            text.setSize(150,15);
            text.setForeground(textColour);
            frame.getContentPane().add(numberOfRegions);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
    }

    //Performs an image operation to the image
    private BufferedImage ImageOperation(BufferedImage img, Operations operation, int value){
        //Creates a new image
        BufferedImage newimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

        //for each line
        for(int y = 0; y < newimg.getHeight(); y++){
            //for each pixel
            for(int x = 0; x < newimg.getWidth(); x++){
                //Calculate the new value of the pixel
                int newValue = 0;

                //Calculates the new pixel value
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
                        newValue = ThresholdImage(img, value, x, y);
                        break;
                    case MAX:
                        newValue = MaxImage(img, value , x, y);
                        break;
                    case MIN:
                        newValue = MinImage(img, value , x, y);
                        break;
                }

                //Sets the new pixel value
                Color color = new Color(newValue, newValue, newValue);
                newimg.setRGB(x, y, color.getRGB());
            }
        }
        return newimg;
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

    //Grows the shapes in the image
    public BufferedImage GrowImage(BufferedImage img, Boolean n8){
        //Creates a copy of the image
        BufferedImage imageCopy = CopyImage(img);
        
        //For each line
        for(int y = 0; y < img.getHeight(); y++){
            //For each pixel in the line
            for(int x = 0; x < img.getWidth(); x++){
                //Gets the pixel value at the current x,y coordinates
                int value = (new Color(img.getRGB(x, y))).getRed();

                if(value > 0){
                    //Changes the pixel to the left
                    if(IsValidPixel(img, x - 1, y)){
                        imageCopy.setRGB(x - 1, y, Color.WHITE.getRGB());
                    }
                    //Changes the pixel to the right
                    if(IsValidPixel(img, x + 1, y)){
                        imageCopy.setRGB(x + 1, y, Color.WHITE.getRGB());
                    }
                    //Changes the pixel below
                    if(IsValidPixel(img, x, y - 1)){
                        imageCopy.setRGB(x, y - 1, Color.WHITE.getRGB());
                    }
                    //Changes the pixel above
                    if(IsValidPixel(img, x, y + 1)){
                        imageCopy.setRGB(x, y + 1, Color.WHITE.getRGB());
                    }
                    //If you want to use n8 rather than n4
                    if(n8){
                        //Changes the bottom left pixel
                        if(IsValidPixel(img, x - 1, y - 1)){
                            imageCopy.setRGB(x - 1, y - 1, Color.WHITE.getRGB());
                        }
                        //Changes the top left pixel
                        if(IsValidPixel(img, x - 1, y + 1)){
                            imageCopy.setRGB(x - 1, y + 1, Color.WHITE.getRGB());
                        }
                        //Changes the bottom right pixel
                        if(IsValidPixel(img, x + 1, y - 1)){
                            imageCopy.setRGB(x + 1, y - 1, Color.WHITE.getRGB());
                        }
                        //Changes the top right pixel
                        if(IsValidPixel(img, x + 1, y + 1)){
                            imageCopy.setRGB(x + 1, y + 1, Color.WHITE.getRGB());
                        }
                    }
                }
            }
        }

        return imageCopy;
    }

    //Shrinks the shapes in the image
    public BufferedImage ShrinkImage(BufferedImage img, boolean n8){
        BufferedImage imageCopy = CopyImage(img);

        //For each line
        for(int y = 0; y < img.getHeight(); y++){
            //For each pixel in the line
            for(int x = 0; x < img.getWidth(); x++){
                //Gets the pixel value at the current x,y coordinates
                int value = (new Color(img.getRGB(x, y))).getRed();

                if(value == 0){
                    //Changes the pixel to the left
                    if(IsValidPixel(img, x - 1, y)){
                        imageCopy.setRGB(x - 1, y, Color.BLACK.getRGB());
                    }
                    //Changes the pixel to the right
                    if(IsValidPixel(img, x + 1, y)){
                        imageCopy.setRGB(x + 1, y, Color.BLACK.getRGB());
                    }
                    //Changes the pixel below
                    if(IsValidPixel(img, x, y - 1)){
                        imageCopy.setRGB(x, y - 1, Color.BLACK.getRGB());
                    }
                    //Changes the pixel above
                    if(IsValidPixel(img, x, y + 1)){
                        imageCopy.setRGB(x, y + 1, Color.BLACK.getRGB());
                    }
                    if(n8){
                        //Changes the bottom left pixel
                        if(IsValidPixel(img, x - 1, y - 1)){
                            imageCopy.setRGB(x - 1, y - 1, Color.BLACK.getRGB());
                        }
                        //Changes the top left pixel
                        if(IsValidPixel(img, x - 1, y + 1)){
                            imageCopy.setRGB(x - 1, y + 1, Color.BLACK.getRGB());
                        }
                        //Changes the bottom right pixel
                        if(IsValidPixel(img, x + 1, y - 1)){
                            imageCopy.setRGB(x + 1, y - 1, Color.BLACK.getRGB());
                        }
                        //Changes the top right pixel
                        if(IsValidPixel(img, x + 1, y + 1)){
                            imageCopy.setRGB(x + 1, y + 1, Color.BLACK.getRGB());
                        }
                    }
                }
            }
        }

        return imageCopy;
    }

    //Shrinks the shapes in the image
    private int RegionImage(BufferedImage img){
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
        return label -1;
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

    private BufferedImage AutoContrast(BufferedImage img){
        int totalPixels = 0;
        //Creates copy of the image
        BufferedImage imageCopy = CopyImage(img);
        //Gets array of pixel distribution
        int[] pixelDistribution = GetPixelDistribution(img);

        //Gets the total number of pixels
        for(int i = 0; i < 256; i++){
            totalPixels += pixelDistribution[i];
        }

        //Gets the low threshold value
        int low = GetMinValue(pixelDistribution, totalPixels);
        //Gets the high threshold value
        int high = GetMaxValue(pixelDistribution, totalPixels);

        //For each line
        for(int y = 0; y < img.getHeight(); y++){
            //For each pixel
            for(int x = 0; x < img.getWidth(); x++){
                //Get the pixel value at the x and y coordinates
                int value = (new Color(img.getRGB(x,y))).getRed();
                int newValue;

                //Apply autocontrasting
                if(value <= low){
                    newValue = 0;
                }else if(value >= high){
                    newValue = 255;
                }else{
                    newValue = (int)((value - low)*(((double)255)/((double)(high - low))));
                }

                //Generate RGB value for the image
                int rgb = new Color(newValue, newValue, newValue).getRGB();
                //Sets the value in the image copy to be equal to the new value
                imageCopy.setRGB(x, y, rgb);
            }
        }
        return imageCopy;
    }

    //Calculates if the pixel is a valid pixel
    private Boolean IsValidPixel(BufferedImage img, int x, int y){
        return x >= 0 && x < img.getWidth() && y >= 0 && y <img.getHeight();
    }

    //Gets the min value of the pixel for auto contrast
    private int GetMinValue(int[] array, int totalPixels){
        int pixelValue = 0;
        int tempCounter = 0;
        
        //Gets the threshold value from the total number of pixels
        int lowThreshold = (int)(totalPixels * 0.05);

        //Finds the pixel value of the pixel at the threshold level
        for(int i = 0; i <= 255; i++){
            tempCounter += array[i];
            if(tempCounter >= lowThreshold){
                pixelValue = i;
                break;
            }
        }
        return pixelValue;
    }

    //Gets the max value for auto constrast
    private int GetMaxValue(int[] array, int totalPixels){
        int pixelValue = 0;
        int tempCounter = 0;

        //Gets the threshold value from the total number of pixels
        int highThreshold = (int)(totalPixels * 0.95);

        //Finds the pixel value of the pixel at the threshold level
        for(int i = 0; i <= 255; i++){
            tempCounter += array[i];
            if(tempCounter >= highThreshold){
                pixelValue = i;
                break;
            }
        }
        return pixelValue;
    }

    //Gets the pixel distrubution of the image
    private int[] GetPixelDistribution(BufferedImage img){
        int[] array = new int[256];

        for(int y = 0; y < img.getHeight(); y++){
            //For each pixel
            for(int x = 0; x < img.getWidth(); x++){
                //Get the RGB value of the pixel
                int value = (new Color(img.getRGB(x,y))).getRed();
                //If the value of the pixel is more than the current maximum value
                array[value]++;
            }
        }
        return array;
    }
}
    