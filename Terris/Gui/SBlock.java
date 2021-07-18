package Terris.Gui;

import javafx.scene.paint.Color;

public class SBlock extends Tetris
{
    public SBlock() {
        super();
        this.shape = new int[][]{{0,1,1},{1,1,0}};
        // 初始化不同的旋轉
        for(int i=0;i<this.spinTime;i++)this.getSpinTetris();
        this.color = Color.DARKORANGE;
        this.xPanning = new int[]{0,-1,0,-1};
        this.yPanning = new int[]{0,0,0,0};
        this.pos = this.spinTime;
    }
}
