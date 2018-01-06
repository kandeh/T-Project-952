package server.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import server.Cheese;
import server.Rat;

public class Item {
    
    public enum Type {
        BROWN_RAT,
        GRAY_RAT,
        Wall,
        Ladder,
        POISON,
        CHEESE
    }
    
    public Object owner = null;
    private final Type type;
    public int fixX;
    public int fixY;
    public double fixDegree;
    public double fixScaleFactor;
    
    public int id;
    private int row;
    private int col; 
    
    public final static int cellSize = 40;

    public static int initialDy = 0;
    public static int initialDx = 0;
    
    private Animation currentAnim = null;
    private long animStartTime;
    
    private ArrayList<Animation> animations = null;

    private static int lastID = 0;
    
    public void init() {
        animations.clear();
        
        if(this.currentAnim != null) {
            fixDegree += this.currentAnim.getRotateDegree();
            fixScaleFactor += this.currentAnim.getScalefactor();
            fixX += this.currentAnim.getDeltaCol();
            fixY += this.currentAnim.getDeltaRow();
        }

        if(type == Type.BROWN_RAT || type == Type.GRAY_RAT) {
            this.fixX = ((Rat) owner).col * cellSize;
            this.fixY = ((Rat) owner).row * cellSize;
        }
        
        this.currentAnim = null;
    }
    
    public void addAnimation(Animation anim) {
        animations.add(anim);       
    }

    public Type getType() {
        return type;
    }
     
    public Item(Type type, Object owner, int row, int col, int id) {
        this.id = lastID++;
        this.row = row;
        this.col = col;
        this.type = type;
        this.owner = owner;
        this.fixX = col * cellSize;
        this.fixY = row * cellSize;
        this.fixDegree = 0;
        this.fixScaleFactor = 0.122;
        if(type == Type.Ladder) {
            this.fixScaleFactor = 0.14;
        }
        if(type == Type.CHEESE || type == Type.POISON) {
            this.fixScaleFactor = 0.14;
        }
        if(type == Type.BROWN_RAT || type == Type.GRAY_RAT) {
            this.fixScaleFactor = 0.19;
        }
        
        int w = getImage().getWidth(null);
        
        this.fixScaleFactor = (double) cellSize / w;
        
        animations = new ArrayList<Animation>();
        init();
    }
    
    private boolean hasNextAnim() {
        return animations.size() > 0;
    }
    
    private void startAnim() {
        currentAnim = animations.get(0);
        animations.remove(0);
        animStartTime = System.currentTimeMillis();
    }
    
    private void animEnd() {
        fixX = fixX + currentAnim.getDeltaCol() * 1;
        fixY = fixY + currentAnim.getDeltaRow() * 1;
        fixDegree = fixDegree + currentAnim.getRotateDegree() * 1;
        fixScaleFactor = fixScaleFactor + currentAnim.getScalefactor() * 1;
        currentAnim = null;
    }
    
    private double getAnimFactor() {
        if(currentAnim == null) {
            return 0;
        }
        int inte = 500;
        long elapsed_mill = System.currentTimeMillis() - animStartTime;
        if(elapsed_mill >= inte) {
            animEnd();
            return 0;
        }
        return (double) elapsed_mill / inte;
    }
    
    private Image getImage() {
        switch(type) {
            case CHEESE: return Resource.Cheese[((Cheese) owner).getSize() - 1];
            case POISON: return Resource.Poison[((Cheese) owner).getSize() - 1];
            case Wall: return Resource.WallImg;
            case Ladder: return Resource.LadderImg;
            case BROWN_RAT: return Resource.BrownRatImg;
            case GRAY_RAT: return Resource.GrayRatImg;
        }
        return null;
    }
    
    private AffineTransform getAnimTransform(double factor) {
        int x, y;
        double degree, scale_factor;
        if(currentAnim != null) {
            x = (int) (fixX + currentAnim.getDeltaCol() * factor);
            y = (int) (fixY + currentAnim.getDeltaRow() * factor);
            degree = fixDegree + currentAnim.getRotateDegree() * factor;
            scale_factor = fixScaleFactor + currentAnim.getScalefactor() * factor;
        } else {
            x = (int) (fixX);
            y = (int) (fixY);
            degree = fixDegree;
            scale_factor = fixScaleFactor;
        }
        AffineTransform transform = new AffineTransform();
        transform.setTransform(new AffineTransform());
        int w = Resource.BrownRatImg.getWidth(null);
        int h = Resource.BrownRatImg.getHeight(null);
 
        int t_m = initialDy + cellSize / 2;
        
        if(type == Type.CHEESE || type == Type.POISON) {
            
            transform.translate(x - (w * scale_factor ) / 2 + initialDx, y - (h * scale_factor ) / 2 + t_m);
            transform.scale(scale_factor, scale_factor);
        } else {
            if(type == Type.BROWN_RAT || type == Type.GRAY_RAT) {
               //transform.translate(0, -8);
            }
            transform.translate(x - (w * scale_factor ) / 2  + initialDx, y - (h * scale_factor ) / 2 + t_m);
            transform.scale(scale_factor, scale_factor);
            if(type == Type.BROWN_RAT || type == Type.GRAY_RAT) {
               transform.rotate((double)(degree * (Math.PI)) / 180, w / 2, 0.5 * h);
            } else {
                transform.rotate((double)(degree * (Math.PI)) / 180, w / 2, h / 2);
            }
        }
        return transform;
    }

    public void paint(Graphics g) {
        if(currentAnim == null) {
            if(hasNextAnim()) {
                startAnim();
            }
        }
        double anim_factor = getAnimFactor();
        AffineTransform transform = getAnimTransform(anim_factor);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(getImage(), transform, null);
    }
    
}
