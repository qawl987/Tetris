package Terris.Gui;

import javafx.scene.paint.Color;

public class ZBlock extends Tetris
{
    public ZBlock() {
        super();
        this.shape = new int[][]{{1,1,0},{0,1,1}};
        // 初始化不同的旋轉
        for(int i=0;i<this.spinTime;i++)this.getSpinTetris();
        this.color = Color.DARKORCHID;
        this.xPanning = new int[]{1,-2,1,-2};
        this.yPanning = new int[]{0,0,0,0};
        this.pos = this.spinTime;
    }
}
