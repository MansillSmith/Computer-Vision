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
        if(args.length != numInputs){
            Counter counter = new Counter(args[0]);
        }
    }

    public Counter(String filename){
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

        }
    }
}