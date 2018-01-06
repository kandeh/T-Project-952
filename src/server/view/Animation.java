package server.view;

public class Animation {
    
    private int deltaRow;
    private int deltaCol;
    private double rotateDegree;
    private double scaleFactor;

    /*private Type type;
    
    enum Type {
        MOVE,
        ROTATE,
        SCALE
    }*/

    public Animation(int deltaRow, int deltaCol, double rotateDegree, double scaleFactor) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
        this.rotateDegree = rotateDegree;
        this.scaleFactor = scaleFactor;
    }
    
    /*public static Animation createMoveAnim(int delta_Row, int delta_Col) {
        Animation res = new Animation();
        res.deltaRow = delta_Row;
        res.deltaCol = delta_Col;
        res.type = Type.MOVE;
        return res;
    }
    
    public static Animation createRotateAnim(double degree) {
        Animation res = new Animation();
        res.rotateDegree = degree;
        res.type = Type.ROTATE;
        return res;
    }
    
    public static Animation createScaleAnim(double factor) {
        Animation res = new Animation();
        res.scaleFactor = factor;
        res.type = Type.SCALE;
        return res;
    }*/
    
    private Animation() {}

    /*public Type getType() {
        return type;
    }*/

    public int getDeltaCol() {
        return deltaCol;
    }

    public int getDeltaRow() {
        return deltaRow;
    }

    public double getRotateDegree() {
        return rotateDegree;
    }

    public double getScalefactor() {
        return scaleFactor;
    }
    
}
