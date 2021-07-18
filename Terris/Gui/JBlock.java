package Terris.Gui;

import javafx.scene.paint.Color;

public class JBlock extends Tetris
{
    public JBlock() {
        super();
        this.shape = new int[][]{{0,1},{0,1},{1,1}};
        // 初始化不同的旋轉
        for(int i=0;i<this.spinTime;i++)this.getSpinTetris();
        this.color = Color.DARKGOLDENROD;
        this.xPanning = new int[]{-1,1,-1,0};
        this.yPanning = new int[]{0,0,1,-1};
        this.pos = this.spinTime;
    }
}
