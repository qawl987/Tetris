package Terris.Gui;

import javafx.scene.paint.Color;

public class IBlock extends Tetris
{
    public IBlock() {
        super();
        this.shape = new int[][]{{1,0},{1,0},{1,0},{1,0}};
        // 初始化不同的旋轉
        for(int i=0;i<this.spinTime;i++)this.getSpinTetris();
        this.color = Color.DARKCYAN;
        this.xPanning = new int[]{-1,0,-1,1};
        this.yPanning = new int[]{1,-1,0,-1};
        this.pos = this.spinTime;
    }
}
