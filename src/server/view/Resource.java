package server.view;

import java.awt.Image;
import javax.imageio.ImageIO;

public class Resource {
    
    public static Image BrownRatImg;
    public static Image GrayRatImg;
    public static Image WallImg;
    public static Image LadderImg;
    public static Image Cheese[];
    public static Image Poison[];
    public static Image BackImg;
    
    static {
        Cheese = new Image[3];
        Poison = new Image[3];
        try {
            String b = "server/view/images/";
            BackImg = ImageIO.read(ClassLoader.getSystemResource(b + "background.png"));
            BrownRatImg = ImageIO.read(ClassLoader.getSystemResource(b + "rat_b.png"));
            GrayRatImg = ImageIO.read(ClassLoader.getSystemResource(b + "rat_g.png"));
            WallImg = ImageIO.read(ClassLoader.getSystemResource(b + "block.png"));
            LadderImg = ImageIO.read(ClassLoader.getSystemResource(b + "Ladder.png"));  
            Cheese[0] = ImageIO.read(ClassLoader.getSystemResource(b + "cheese1.png"));
            Cheese[1] = ImageIO.read(ClassLoader.getSystemResource(b + "cheese2.png"));
            Cheese[2] = ImageIO.read(ClassLoader.getSystemResource(b + "cheese3.png"));
            Poison[0] = ImageIO.read(ClassLoader.getSystemResource(b + "poison1.png"));
            Poison[1] = ImageIO.read(ClassLoader.getSystemResource(b + "poison2.png"));
            Poison[2] = ImageIO.read(ClassLoader.getSystemResource(b + "poison3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
