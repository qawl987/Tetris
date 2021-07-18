package Terris.Gui;

import javafx.scene.paint.Color;

public class LBlock extends Tetris
{
    public LBlock() {
        super();
        this.shape = new int[][]{{1,0},{1,0},{1,1}};
        // 初始化不同的旋轉
        for(int i=0;i<this.spinTime;i++)this.getSpinTetris();
        this.color = Color.DARKKHAKI;
        this.xPanning = new int[]{-1,0,0,-1};
        this.yPanning = new int[]{1,-1,0,0};
        this.pos = this.spinTime;
    }
}
