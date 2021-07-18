package Terris.Gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable,EventHandler<KeyEvent>{
    //private static final Paint BLUE = Color.BLUE;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private GridPane gamePanel;
    @FXML
    private Text lose;
    @FXML
    private Button restart;
    @FXML
    private Text score;
    @FXML
    private Text level;
    @FXML
    private ImageView holdBlock;
    @FXML
    private ImageView nextBlock1;
    @FXML
    private ImageView nextBlock2;
    @FXML
    private ImageView nextBlock3;
    Image image_I = new Image("/Terris/resource/I.PNG",60,40,false,false);
    Image image_J = new Image("/Terris/resource/J.PNG",60,40,false,false);
    Image image_L = new Image("/Terris/resource/L.PNG",60,40,false,false);
    Image image_O = new Image("/Terris/resource/O.PNG",40,40,false,false);
    Image image_S = new Image("/Terris/resource/S.PNG",60,40,false,false);
    Image image_T = new Image("/Terris/resource/T.PNG",60,40,false,false);
    Image image_Z = new Image("/Terris/resource/Z.PNG",60,40,false,false);

    //參數區
    Character[] shape = new Character[]{'I','J','L','O','S','T','Z'};
    final int column = 12;
    final int row = 22;
    int point=0;
    int lv=0;
    int recArrCnt = 0;
    int nowRowIndex = 0;
    int nowColumnIndex = 0;
    double blockSize = 18.25;
    int holdNumber=-1;
    int [][] stonePanel = new int[row][column];
    int[] queue = new int[4];

    Tetris block;
    Random random = new Random();
    Timeline timeline;


    //初始化區
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gamePanel.setVisible(true);
        lose.setVisible(false);
        gameing();
    }
    private void gameing()
    {
        /*
        下一行遊戲剛開始先創建第一塊
        timeline裡檢查block是不是空值讓else裡不會出現NPE
        之後如果就可以動就直接動，不行就在這個time cycle裡新增block以防止慢一拍的情況
         */
        //沒有問題
        refreshNextBlock();
        showNextAndHoldBlock();
        //有問題
        createAndAdd();


        timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
//            if(checkGameOver())stopAndDisappear();
            if(block == null)createAndAdd();
            else
            {
                if(moveDown()){}
                else createAndAdd();
            }
            if(checkGameOver())stopAndDisappear();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.rateProperty().bind()
        timeline.play();
    }

    /**
     * 將四個全部刷新
     */
    private void refreshNextBlock()
    {
        int n = random.nextInt(7);
        queue[0] = n;
        for(int i=1;i<4;i++)
        {
            if(queue[i]==0)
            {
                while(true)
                {
                    n = random.nextInt(7);
                    if(n!=queue[i-1])
                    {
                        queue[i] = n;

                        break;
                    }
                }
            }
        }
        for(int i=0;i<4;i++) System.out.print(queue[i]);
        System.out.println();
    }

    /**
     * 讓timeline停止並讓keyboard失去focus
     */
    private void stopAndDisappear()
    {
        if(timeline!=null)
        {
            lose.setVisible(true);
            restart.requestFocus();
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline = null;
        }
    }

    /**
     * 初始化格線
     */
    public void initGameView()
    {
        for(int r =1;r<row-1;r++){
            for(int c=1;c<column-1;c++) {
                Region rectangle = new Region();
                //rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStyle("-fx-border-style: solid; -fx-border-color: black; -fx-min-width: 14; -fx-min-height:14; -fx-max-width:30; -fx-max-height: 30;");
                gamePanel.add(rectangle,c,r);
            }
        }
    }
    /**
     * 將方塊設置到盤面上
     */
    public void addBlockOnGamePanel(Tetris t)
    {
        recArrCnt = 0;
        for(int r=0;r<t.getShape().length;r++){
            for(int c =0;c<t.getShape()[0].length;c++){
                if(t.getShape()[r][c] == 1){
                    Rectangle rect = new Rectangle(blockSize,blockSize);
                    rect.setFill(block.getColor());
                    t.getRectangles()[recArrCnt] = rect;
                    //設在(1,1)的位置
                    gamePanel.add(rect,c+5,r+1);
                    //設置矩形在gridPane的中間
                    gamePanel.setHalignment(rect, HPos.CENTER);
                    recArrCnt++;
                }
            }
        }
        //當新方塊與stone重疊就全部往上一格隨便找一個數字讓他跑一下迴圈
        for(int i=0;i<4;i++)
        {
            if(checkCollision()[i]!=0)setOnStoneTop();
        }
    }

    /**
     * 讓方塊在頂到上方時能正確置頂
     */
    private void setOnStoneTop()
    {
        for(int i=0;i<4;i++)
        {
            nowRowIndex = getNowRowIndex(i);
            if(nowRowIndex ==0)continue;
            gamePanel.setRowIndex(block.getRectangles()[i], nowRowIndex-1);
        }
    }

    /**
     * 左平移尾端新增[3]>>順序顯示[1][2][3]的樣子>>依照陣列[0]生成方塊
     */
    private void createAndAdd()
    {
        //加上判斷blockIsnull的判段防止上方出現一個空的方塊
        if(block == null)
        {
            block = new Tetris();
            //改陣列內容
            shiftAndAddBehind();
            //改顯示圖案
            showNextAndHoldBlock();
            block = block.spawnBlock(shape[queue[0]]);
//            block = block.spawnBlock('I');
            addBlockOnGamePanel(block);
        }

    }

    private void showNextAndHoldBlock()
    {
        setBlockImage(nextBlock1,queue[1]);
        setBlockImage(nextBlock2,queue[2]);
        setBlockImage(nextBlock3,queue[3]);
        setBlockImage(holdBlock,holdNumber);
    }


    /**
     * 取第一個值全部往前移，後放一個新的到後面
     */
    private void shiftAndAddBehind()
    {
        for(int i=0;i<4;i++)
        {
            if(i==3)
            {
                while(true)
                {
                    int n= random.nextInt(7);
                    if(n!=queue[2])
                    {
                        queue[3] = n;
                        System.out.print(n);
                        break;
                    }
                }
            }
            else {
                queue[i] = queue[i+1];
                System.out.print(queue[i]);
            }
        }
        System.out.println();
    }

    @FXML
    private void restart()
    {
        lv=0;
        point=0;
        score.setText("Score: "+ point);
        level.setText("Level: " + lv);
        holdNumber =-1;
        if(timeline==null)gameing();
        timeline.setRate((lv!=0)?lv:1);
        block = block.refresh();
        //清光盤面上的rect
        for(int r=0;r<row-1;r++)
        {
            for(int c=1;c<column-1;c++)
            {
                stonePanel[r][c] = 0;
                removeNodeByRowColumnIndex(r,c);
                if(r==0)for(int i=0;i<4;i++)removeNodeByRowColumnIndex(r,c);
            }
        }
        createAndAdd();
        //拿回鍵盤控制權
        anchorPane.requestFocus();
    }


    //動作區
    @FXML
    @Override
    public void handle(KeyEvent e) {
        if(block !=null)
        {
            switch (e.getCode())
            {
                case RIGHT:
                    if (!checkRightStoneAndBound()) moveRight();
                    break;
                case LEFT:
                    if (!checkLeftStoneAndBound()) moveLeft();
                    break;
                case UP:
                    spin();
                    break;
                case SPACE:
                    hardDrop();
                    break;
                case DOWN:
                    moveDown();
                    break;
                case C:
                    hold();
                    break;
            }

        }
    }

    private void hold()
    {
        if(holdNumber==-1)
        {
            holdNumber = queue[0];
            shiftAndAddBehind();
            for(int i=0;i<4;i++) System.out.print(queue[i]);
            System.out.println();
            System.out.println(holdNumber);
            for(int i=0;i<4;i++)
            {
                nowRowIndex =getNowRowIndex(i);
                nowColumnIndex =getNowColumnIndex(i);
                removeNodeByRowColumnIndex(nowRowIndex,nowColumnIndex);
            }
            block = block.spawnBlock(shape[queue[0]]);
            addBlockOnGamePanel(block);
            showNextAndHoldBlock();
        }
        else
        {
            int a= queue[0];
            queue[0] = holdNumber;
            holdNumber = a;
            for(int i=0;i<4;i++) System.out.print(queue[i]);
            System.out.println();
            System.out.println(holdNumber);
            for(int i=0;i<4;i++)
            {
                nowRowIndex =getNowRowIndex(i);
                nowColumnIndex =getNowColumnIndex(i);
                removeNodeByRowColumnIndex(nowRowIndex,nowColumnIndex);
            }
            block = block.spawnBlock(shape[queue[0]]);
            addBlockOnGamePanel(block);
            showNextAndHoldBlock();
        }
    }

    //旋轉區
    private void spin()
    {
        //fuck u 每次按都會更新
        block = block.getSpinTetris();
        recArrCnt = 0;
        //以最靠近左上的當作新的陣列開頭
        int ReferenceRowIndex = getNowRowIndex(0);
        int ReferenceColumnIndex = getNowColumnIndex(0);
        //注意每按一次都會改變
        int pos = block.getPos();
        //x,y平移量
        int y = block.getyPanning(pos);
        int x = block.getxPanning(pos);
        int [][] check = new int[4][2];
        //第一段儲存預旋轉方塊資料
        for(int r=0;r<block.getShape().length;r++)
        {
            for(int c=0;c<block.getShape()[0].length;c++)
            {
                if(block.getShape()[r][c]==1)
                {
                    //新的位置用check存起來
                    //列即Y座標
                    check[recArrCnt][0] = ReferenceRowIndex+r+y;
                    //行即X座標
                    check[recArrCnt][1] = ReferenceColumnIndex+c+x;
                    recArrCnt++;
                }
            }
        }
        recArrCnt =0;
        //利用上面儲存的資料檢查
        if(checkSpin(check))
        {
            for(int r=0;r<block.getShape().length;r++)
            {
                for(int c=0;c<block.getShape()[0].length;c++)
                {
                    if(block.getShape()[r][c]==1)
                    {
                        gamePanel.setRowIndex(block.getRectangles()[recArrCnt],ReferenceRowIndex+r+y);
                        //IBlock特例處理
                        if(block.color == Color.DARKCYAN && ReferenceColumnIndex ==column-2) gamePanel.setColumnIndex(block.getRectangles()[recArrCnt],ReferenceColumnIndex+c-2);
                        else gamePanel.setColumnIndex(block.getRectangles()[recArrCnt],ReferenceColumnIndex+c+x);
                        recArrCnt++;
                    }
                }
            }
            //各種例外處理
            //如果頂到就下降一行
            if(getNowRowIndex(0)==0) setDownLayoutY();
            //觸底檢查若觸底上移
            if(getNowRowIndex(3)==row){
                setUpLayoutY();
                setUpLayoutY();
            }
            else if (getNowRowIndex(3)==row-1)setUpLayoutY();
            //隨便找個數字讓他跑跑看
            for(int i=0;i<4;i++)adjust(check);
        }
    }

    private boolean checkSpin(int[][] check)
    {
        //以col也就是行=check[i][0]來
        //fuck u sumX要用有小數點的
        double sumX=0;
//        for(int i=0;i<4;i++){
//            for(int j=0;j<2;j++){
//                System.out.print(check[i][j]);
//                System.out.print(' ');
//            }
//            System.out.println();
//        }
        for(int i=0;i<4;i++) sumX = check[i][1] + sumX;
        sumX = sumX/4;
//        System.out.println(sumX);
        int [] a = checkPreCollision(check);
        boolean left =false;
        boolean right =false;
        for(int i=0;i<4;i++)
        {
            if(a[i] !=0)
            {
                //若預轉方塊接觸點比平均大代表右邊不能動，反之
                if(a[i] >sumX ||a[i] ==column-1)right = true;
                else if(a[i] <sumX||a[i] == 0 ) left =true;
                //若右邊不能動且左邊也不能動，代表不能動
                if(a[i]>sumX&& checkPreLeftStoneAndBound(check))
                {
                    right = true;
                    left =true;
                }
                if(a[i]<sumX&& checkPreRightStoneAndBound(check))
                {
                    right = true;
                    left =true;
                }
            }
        }
        //當兩邊都不能動的時候代表不能旋轉
        if(right&& left)
        {
            //記得如果不能旋轉要把最初更改的改回去
            for(int i=0;i<3;i++)
            {
                block.getPos();
                block.getSpinTetris();
            }
            return false;
        }
        else return true;
    }

    /**
     * 檢查預旋轉有沒有重疊
     * @param check 預旋轉資料
     * @return 所有重疊方塊的column
     */
    private int[] checkPreCollision(int[][] check)
    {
        int[] col = new int[]{0,0,0,0};
        for(int i=0;i<4;i++)
        {
            int Row = check[i][0];
            int Column = check[i][1];
            if(Row >=row)break;
            if(Column >=column-1)break;
            if(stonePanel[Row][Column]==1)col[i] = Column;
//            else if(Column ==column-1)col[i] = 100;
            else if(Column == 0)col[i] = -1;
        }
        return  col;
    }
    /**
     * 四個x座標算平均如果碰撞小於平均就往右移，反之
     * @param check 用來確認是左邊還是右邊碰撞
     */
    private void adjust(int[][] check)
    {
        int sumColumn=0;
        for(int i=0;i<4;i++) {
            sumColumn = check[i][1] + sumColumn;
        }
        sumColumn = sumColumn/4;
        for(int i=0;i<4;i++)
        {
            if(checkCollision()[i] <sumColumn && checkCollision()[i]!=0) moveRight();
            else if(checkCollision()[i]>sumColumn&& checkCollision()[i]!=0) moveLeft();
        }
        for(int i =0;i<4;i++)
        {
            if(checkSpinRightWall())moveLeft();
            else if (checkSpinLeftWall())moveRight();
        }
    }
        //旋轉區結束
    /**
     * 檢查是否新方塊與石頭重疊
     * 以目前的未知
     */
    private int[] checkCollision()
    {
        int[] col = new int[]{0,0,0,0};
        for(int i=0;i<4;i++)
        {
            nowRowIndex = getNowRowIndex(i);
            nowColumnIndex = getNowColumnIndex(i);
            if(stonePanel[nowRowIndex][nowColumnIndex]==1)col[i] = nowColumnIndex;
        }
        return col;
    }
    /**
     * 先檢查有沒有到底再讓他往下
     */
    public boolean moveDown()
    {
        if(!checkBottomAndDownStone())
        {
            setDownLayoutY();
            return true;
        }
        return false;
    }
    public void hardDrop()
    {
        while(block !=null &&!checkBottomAndDownStone() ) setDownLayoutY();
    }
    /**
     * 下移整個方塊
     */
    public void setDownLayoutY()
    {
        for(int i=3 ;i>=0;i--) {
            nowRowIndex = getNowRowIndex(i);
            gamePanel.setRowIndex(block.getRectangles()[i], nowRowIndex+1);
        }
    }
    public void setUpLayoutY()
    {
        for(int i=0 ;i<4;i++) {
            nowRowIndex = getNowRowIndex(i);
            gamePanel.setRowIndex(block.getRectangles()[i], nowRowIndex-1);
        }
    }
    private void moveLeft()
    {
        for(int i=0;i<4;i++)
        {
            nowColumnIndex = getNowColumnIndex(i);
            gamePanel.setColumnIndex(block.getRectangles()[i],nowColumnIndex-1);
        }
    }
    private void moveRight()
    {
        for(int i=3;i>=0;i--)
        {
            nowColumnIndex = getNowColumnIndex(i);
            gamePanel.setColumnIndex(block.getRectangles()[i],nowColumnIndex+1);
        }
    }
    private void changeBlockToStone()
    {
        for(int i=0;i<4;i++)
        {
            nowRowIndex = getNowRowIndex(i);
            nowColumnIndex = getNowColumnIndex(i);
            stonePanel[nowRowIndex][nowColumnIndex] = 1;
        }
        block = block.refresh();
    }
    /**
     *檢查有沒有觸底
     */
    public boolean checkBottomAndDownStone()
    {
        //跑迴圈檢查有沒有觸底
        for(int i=0;i<4;i++)
        {
            if(getNowRowIndex(i) ==row-2)
            {
                changeBlockToStone();
                for(int j=0;j<4;j++)
                {
                    int theRow =clean();
                    if(theRow!=0) moveAllBlockDown(theRow);
                }
                createAndAdd();
                score.setText("Score: "+ point);
                level.setText("Level: " + lv);
                timeline.setRate((lv!=0)?lv:1);
                return true;
            }
        }
        //檢查下方石頭
        if(checkDownStone())
        {
            changeBlockToStone();
            for(int j=0;j<4;j++)
            {
                int theRow =clean();
                if(theRow!=0) moveAllBlockDown(theRow);
            }
            score.setText("Score: "+ point);
            level.setText("Level: " + lv);
            timeline.setRate((lv!=0)?lv:1);
            createAndAdd();
            return true;
        }
        return false;
    }

    private int clean()
    {
        int theRow=0;
        for(int r=row-2;r>=1;r--)
        {
            boolean check =true;
            //如果全為1下面要動作
            for(int c =1;c<column-1;c++) if(stonePanel[r][c]!=1)check=false;
            if(check)
            {
                //如果該行全為1就把該行全設為0
                for(int c =0;c<column-1;c++)
                {
                    removeNodeByRowColumnIndex(r,c);
                    stonePanel[r][c]=0;
                }
                point++;
                lv = point/10;
                theRow = r;
                //下面的break一次只消除一行
                break;
            }
//            System.out.println();
        }
        return theRow;
    }
    public void removeNodeByRowColumnIndex(int row,int column) {

        ObservableList<Node> childrens = gamePanel.getChildren();
        for(Node node : childrens) {
            if(node instanceof Rectangle && gamePanel.getRowIndex(node) == row && gamePanel.getColumnIndex(node) == column) {
                gamePanel.getChildren().remove(node);
                break;
            }
        }
    }
    private void moveAllBlockDown(int theRow)
    {
        for(int r=theRow;r>=2;r--)
        {
            for(int c=0;c<column-1;c++)
            {
                Node n = getRectByCoordinate(r-1,c);
                if(n!=null)gamePanel.setRowIndex(n,r);
                stonePanel[r][c] = stonePanel[r-1][c];
            }
        }
    }
    private Node getRectByCoordinate(Integer row, Integer column) {
        for (Node node : gamePanel.getChildren()) {
            if(node instanceof Rectangle &&gamePanel.getRowIndex(node).equals(row) && gamePanel.getColumnIndex(node).equals(column)){
                return node;
            }
        }
        return null;
    }
    //檢查區


//    private void clean()
//    {
//        for(int i=0;i<;i++)
//    }

    private boolean checkDownStone()
    {
        for(int i=3;i>=0;i--)
        {
            nowRowIndex = getNowRowIndex(i);
            nowColumnIndex = getNowColumnIndex(i);
            if(stonePanel[nowRowIndex+1][nowColumnIndex]==1)return true;
        }
        return false;
    }
    private boolean checkLeftStoneAndBound()
    {
        for(int i=0;i<4;i++)
        {
            nowRowIndex = getNowRowIndex(i);
            nowColumnIndex =getNowColumnIndex(i);
            if(stonePanel[nowRowIndex][nowColumnIndex-1] == 1)return true;
            if(nowColumnIndex-1==0)return true;
        }
        return false;
    }
    private boolean checkRightStoneAndBound()
    {
        for(int i=0;i<4;i++)
        {
            nowRowIndex = getNowRowIndex(i);
            nowColumnIndex =getNowColumnIndex(i);
            if(stonePanel[nowRowIndex][nowColumnIndex+1] == 1)return true;
            //判斷右牆
            if(nowColumnIndex+1==column-1)return true;
        }
        return false;
    }
    private boolean checkPreLeftStoneAndBound(int[][] check)
    {
        for(int i=0;i<4;i++)
        {
            int Row = check[i][0];
            int Col =check[i][1];
            nowColumnIndex = getNowColumnIndex(i);
            if(nowColumnIndex-1==0)return true;
            if(stonePanel[Row][Col-1] == 1)return true;
        }
        return false;
    }
    private boolean checkPreRightStoneAndBound(int[][] check)
    {
        for(int i=0;i<4;i++)
        {
            int Row = check[i][0];
            int Col =check[i][1];
            //若預旋轉位置在
            nowColumnIndex = getNowColumnIndex(i);
            if(nowColumnIndex==column-2)return true;
            if(stonePanel[Row][Col+1] == 1)return true;
        }
        return false;
    }

    /**
     * 旋轉檢查觸碰右牆
     */
    private boolean checkSpinRightWall()
    {
        for(int i=0;i<4;i++)
        {
            nowColumnIndex = getNowColumnIndex(i);
            if(nowColumnIndex >=column-1)return true;
        }
        return false;
    }
    private boolean checkSpinLeftWall()
    {
        for(int i=0;i<4;i++)
        {
            nowColumnIndex = getNowColumnIndex(i);
            if(nowColumnIndex <=0)return true;
        }
        return false;
    }
    /**
     * 檢查有沒有頂到row[0]
     */
    private boolean checkGameOver()
    {
        for(int i=0;i<4;i++)
        {
            nowRowIndex = getNowRowIndex(i);
            if(nowRowIndex == 0)return true;
        }
        return false;
    }

    //回傳區
    private int getNowRowIndex(int i)
    {
        return gamePanel.getRowIndex(block.getRectangles()[i]);
    }
    private int getNowColumnIndex(int i)
    {
        return gamePanel.getColumnIndex(block.getRectangles()[i]);
    }
    private void setBlockImage(ImageView nextBlock, int i)
    {
        if(i ==0)nextBlock.setImage(image_I);
        else if(i==1)nextBlock.setImage(image_J);
        else if(i==2)nextBlock.setImage(image_L);
        else if(i==3)nextBlock.setImage(image_O);
        else if(i==4)nextBlock.setImage(image_S);
        else if(i==5)nextBlock.setImage(image_T);
        else if(i==6)nextBlock.setImage(image_Z);
        else if(i==-1)nextBlock.setImage(null);
    }
//    private void setImageWidth()
//    {
//        image_I.setFitWidth()
//    }
}
