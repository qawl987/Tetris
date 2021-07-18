package Terris.Gui;

import javafx.scene.paint.Color;

public class OBlock extends Tetris
{
    public OBlock() {
        super();
        this.shape = new int[][]{{1,1},{1,1}};
        // 初始化不同的旋轉
        this.color = Color.DARKGREEN;
        this.xPanning = new int[]{0,0,0,0};
        this.yPanning = new int[]{0,0,0,0};
        this.pos = this.spinTime;
    }
}
