package com.example.game_of_life_v1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller2 implements Initializable {
    @FXML Button nextStateBtn;
//    @FXML AnchorPane game_view;
    @FXML Canvas gameOfLifePane;
    @FXML ImageView image;
    @FXML HBox sBox;
    @FXML HBox bBox;
    @FXML Button pauseBtn;
//===================================================== Parameters =====================================================
    private final int CELL_SIZE = 4;
    private final int CELL_WIDTH = 200;
    private final int CELL_HEIGHT = 200;
    private final Color BG_COLOR = Color.TRANSPARENT;
    private final Color DEAD_CELL_COLOR = Color.TRANSPARENT;
    private final Color ALIVE_CELL_COLOR = Color.BLACK;
    private final double STROKE_WIDTH = 0.1;
    private final Color STROKE_COLOR = Color.LIGHTGREY;
    private boolean[] sRule = new boolean[9];
    private boolean[] bRule = new boolean[9];

//======================================================================================================================
    //todo : mouse click
    //todo : change rules
    //todo : get image

//======================================================================================================================
    private byte[][] curGenCells;
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
        e -> nextGen()
    ));

    private GraphicsContext game_of_live_graphics;
    private final int PANE_WIDTH = CELL_WIDTH * CELL_SIZE;
    private final int PANE_HEIGHT = CELL_HEIGHT * CELL_SIZE;

//===================================================== Initialize =====================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameOfLifePane.setWidth(PANE_WIDTH);
        gameOfLifePane.setHeight(PANE_HEIGHT);
        game_of_live_graphics = gameOfLifePane.getGraphicsContext2D();

        image.setFitWidth(PANE_WIDTH);
        image.setFitHeight(PANE_HEIGHT);


        game_of_live_graphics.setStroke(STROKE_COLOR);
        game_of_live_graphics.setLineWidth(STROKE_WIDTH);

        sRule[2]=true;
        sRule[3]=true;
//        sRule[4]=true;

        bRule[3]=true;
//        bRule[4]=true;

        checkBoxInitialize();

        clearCells();
        setRandom(0.5);
        timeline.setCycleCount(Animation.INDEFINITE);

    }

    private void quickSet(int x, int y, boolean alive){
        setCell(x,y,alive);
        drawPicture(x,y);
    }

    private void draw(int x, int y){
        if((curGenCells[x][y] & 0b1) > 0)
            game_of_live_graphics.setFill(ALIVE_CELL_COLOR);
        else
            game_of_live_graphics.setFill(DEAD_CELL_COLOR);
        game_of_live_graphics.fillRect(x * CELL_SIZE , y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawPicture(int x, int y){
        if((curGenCells[x][y] & 0b1) > 0){
            game_of_live_graphics.clearRect(x * CELL_SIZE , y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        }else{
            game_of_live_graphics.setFill(Color.WHITE);
            game_of_live_graphics.fillRect(x * CELL_SIZE , y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }


    private void nextGen(){
        byte[][] tempCells = Arrays.stream(curGenCells).map(byte[]::clone).toArray(byte[][]::new); //!!

        for (int x = 0; x < CELL_WIDTH; x++) {
            for (int y = 0; y < CELL_HEIGHT; y++) {
                //if(tempCells[x][y] == 0)continue;

                int aliveNeighbor = (tempCells[x][y] & 0b00001110) >> 1;
                boolean isAlive = (tempCells[x][y] & 0b1) > 0;
                if(isAlive){        //is alive
                    if(!sRule[aliveNeighbor]){
                        quickSet(x,y,false);
                    }
                }else {             // is dead
                    if(bRule[aliveNeighbor]){
                        quickSet(x, y,true);  //set to alive
                    }
                }
            }
        }

        for (int x = 0; x <= CELL_WIDTH; x++) {
            game_of_live_graphics.strokeLine(x * CELL_SIZE, 0, x * CELL_SIZE, PANE_HEIGHT);
        }
        for (int y = 0; y <= CELL_HEIGHT; y++) {
            game_of_live_graphics.strokeLine(0,y * CELL_SIZE, PANE_WIDTH,y * CELL_SIZE);
        }
    }

    private void setCell(int x, int y, boolean alive){
        //fixme: optimize
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0)continue;

                int k = x + i, l = y + j;
                if(k < 0) k = CELL_WIDTH - 1;
                if(k == CELL_WIDTH) k = 0;
                if(l < 0) l = CELL_HEIGHT - 1;
                if(l == CELL_HEIGHT) l = 0;

                if(alive){
                    curGenCells[x][y] |= 0b01;
                    curGenCells[k][l] += 0b10;
                }else {
                    curGenCells[x][y] &= ~0b01;
                    curGenCells[k][l] -= 0b10;
                }
            }
        }
    }

    private void setRandom(double density){
        if (density > 1) density = 1;
        for (int i = 0; i < CELL_WIDTH * CELL_HEIGHT * density; i++) {
            int x = (int) Math.floor(Math.random() * (CELL_WIDTH-1));
            int y = (int) Math.floor(Math.random() * (CELL_HEIGHT-1));
            if((curGenCells[x][y] & 0b1) == 0){
                quickSet(x,y,true);
            }
        }
    }

    private void clearCells(){
        curGenCells = new byte[CELL_WIDTH][CELL_HEIGHT];
        game_of_live_graphics.setFill(BG_COLOR);
        game_of_live_graphics.clearRect(0,0,PANE_WIDTH,PANE_HEIGHT);
    }

    private void drawAllCells(){
        for (int x = 0; x < CELL_WIDTH; x++) {
            for (int y = 0; y < CELL_HEIGHT; y++) {
                drawPicture(x,y);
            }
        }
    }


//=================================================== Public Methods ===================================================
    public void pauseOnClick(){
        if(timeline.getStatus() == Animation.Status.RUNNING){
            pauseBtn.setText("play");
            timeline.stop();
        } else{
            pauseBtn.setText("stop");
            timeline.play();
        }
    }

    public void nextOnClick(){
        System.out.println("next");
        nextGen();
    }


    public void randomOnClick(){
        System.out.println("random");
        setRandom(0.7);
        drawAllCells();
    }


    public void clearOnClick(){
        System.out.println("clear");
        clearCells();
    }

    public void setRuleOnClick(){

    }

//=================================================== Private Methods ==================================================
    private void checkBoxInitialize(){
        //fixme : optimize
        for (int i = 0; i <= 8 ; i++) {
            // survive checkBoxes
            CheckBox tem = new CheckBox(i+"");
            tem.setId("s"+i);
            CheckBox finalTem = tem;
            finalTem.setSelected(sRule[i]);
            tem.setOnAction(actionEvent -> {
                int id = Integer.parseInt(finalTem.idProperty().getValue().substring(1));
                sRule[id] = finalTem.isSelected();
                System.out.println(id+""+sRule[id]);
                printRules();
            });
            sBox.getChildren().add(tem);
            // born checkBoxes
            tem = new CheckBox(i+"");
            tem.setId("b"+i);
            CheckBox finalTem2 = tem;
            finalTem2.setSelected(bRule[i]);
            tem.setOnAction(actionEvent -> {
                int id = Integer.parseInt(finalTem2.idProperty().getValue().substring(1));
                bRule[id] = finalTem2.isSelected();
                System.out.println(id+""+bRule[id]);
                printRules();
            });
            bBox.getChildren().add(tem);
        }

    }


    private void printRules(){
        System.out.println("sRule:");
        for (int i = 0; i <= 8 ; i++) {
            System.out.print(i+" "+sRule[i]+"\t");
        }
        System.out.println();
        System.out.println("bRule:");
        for (int i = 0; i <= 8 ; i++) {
            System.out.print(i+" "+bRule[i]+"\t");
        }
        System.out.println("\n====================");
    }
}
