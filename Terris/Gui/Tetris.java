package Terris.Gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;
public  class Tetris {
    protected int[][] shape;
    protected Color color;
    protected int[] yPanning;
    protected int[] xPanning;
    protected int pos;
    protected Rectangle[] rectangles= new Rectangle[4];
    public int spinTime;
    public Tetris() {
        spinTime = getSpinTime();
    }

    private int getSpinTime()
    {
        Random rand = new Random();
        int time = rand.nextInt(4);
        return time;
    }

    public Tetris spawnBlock(Character c)
    {
        switch (c)
        {
            case 'I':
                IBlock i = new IBlock();
                return i;
            case 'J':
                JBlock j = new JBlock();
                return j;
            case 'L':
                LBlock l = new LBlock();
                return l;
            case 'O':
                OBlock o = new OBlock();
                return  o;
            case 'S':
                SBlock s = new SBlock();
                return s;
            case 'T':
                TBlock t = new TBlock();
                return t;
            case 'Z':
                ZBlock z = new ZBlock();
                return z;
            default:
                return null;
        }
    }
    public Tetris getSpinTetris()
    {
        int col = shape[0].length;
        int row = shape.length;
        int[][] spin = new int[col][row];
        for(int r=0;r<shape.length;r++)
        {
            for(int c=0;c<shape[0].length;c++)
            {
                //根據圖形對應出來的結果
                spin[c][row-r-1] = shape[r][c];
            }
        }
        this.shape = spin;
        return this;
    }
    public int[][] getShape(){ return shape;}
    public Color getColor(){ return color;}

    /**
     *
     * @return 回傳目前要有多少的平移
     */
    public int getPos()
    {
        if(pos>=4)pos = pos%4;
        pos++;
        return pos-1;
    }
    public int getxPanning(int index)
    {
        return xPanning[index];
    }

    public int getyPanning(int index)
    {
        return yPanning[index];
    }

    public Rectangle[] getRectangles() {
        return rectangles;
    }

    public Tetris refresh()
    {
        return null;
    }
}
