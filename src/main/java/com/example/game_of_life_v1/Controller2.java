package com.example.game_of_life_v1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller2 implements Initializable {
    @FXML Button nextStateBtn;
//    @FXML AnchorPane game_view;
    @FXML Canvas gameOfLifePane;
//===================================================== Parameters =====================================================
    private final int CELL_SIZE = 4;
    private final int CELL_WIDTH = 300;
    private final int CELL_HEIGHT = 200;
    private final Color BG_COLOR = Color.LIGHTGRAY;
    private final Color DEAD_CELL_COLOR = Color.LIGHTGRAY;
    private final Color ALIVE_CELL_COLOR = Color.LIGHTGREEN;
//======================================================================================================================
    private byte[][] curGenCells;
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
        e -> nextGen()
    ));

    private GraphicsContext game_of_live_graphics;



    private final int PANE_WIDTH = CELL_WIDTH * CELL_SIZE;
    private final int PANE_HEIGHT = CELL_HEIGHT * CELL_SIZE;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameOfLifePane.setWidth(PANE_WIDTH);
        gameOfLifePane.setHeight(PANE_HEIGHT);
        game_of_live_graphics = gameOfLifePane.getGraphicsContext2D();

        clearCells();

        game_of_live_graphics.setFill(Color.BLACK);

        setRandom(0.5);
//        d(2,1);
//        d(2,2);
//        d(2,3);
//        nextGen();
//        nextGen();
//        nextGen();
//        nextGen();
//        nextGen();
//        nextGen();
//        nextGen();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void d(int x, int y){
        setCell(x,y,true);
        draw(x,y);
    }

    private void draw(int x, int y){
        if((curGenCells[x][y] & 0b1) > 0)
            game_of_live_graphics.setFill(ALIVE_CELL_COLOR);
        else
            game_of_live_graphics.setFill(DEAD_CELL_COLOR);
        game_of_live_graphics.fillRect(x * CELL_SIZE , y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void nextGen(){
        byte[][] tempCells = Arrays.stream(curGenCells).map(byte[]::clone).toArray(byte[][]::new); //!!

        for (int x = 0; x < CELL_WIDTH; x++) {
            for (int y = 0; y < CELL_HEIGHT; y++) {
                if(tempCells[x][y] == 0)continue;

                int aliveNeighbor = tempCells[x][y] >> 1;
                boolean isAlive = (tempCells[x][y] & 0b1) > 0;
                if(isAlive){        //is alive
                    if((aliveNeighbor != 2) && (aliveNeighbor != 3)){
                        setCell(x, y,false);  //set to dead
                        draw(x, y);
                    }

                }else {             // is dead
                    if(aliveNeighbor == 3){
                        setCell(x, y,true);  //set to alive
                        draw(x, y);
                    }
                }
            }
        }
    }

    private void setCell(int x, int y, boolean alive){
//        int xLeft, xRight, yLeft, yRight;
//
//        if(x == 0)
//            xLeft = CELL_WIDTH - 1;
//        else
//            xLeft = -1;
//
//        if(x == (CELL_WIDTH - 1))
//            xRight = 0;
//        else
//            xRight =0;

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
                setCell(x,y,true);
                draw(x,y);
            }
        }
    }

    private void clearCells(){
        curGenCells = new byte[CELL_WIDTH][CELL_HEIGHT];
        game_of_live_graphics.setFill(BG_COLOR);
        game_of_live_graphics.fillRect(0,0,PANE_WIDTH,PANE_HEIGHT);
    }


//=================================================== Public Methods ===================================================
    public void pauseOnClick(){
        System.out.println("stop");
        timeline.stop();
    }

    public void nextOnClick(){
        System.out.println("next");
        nextGen();
    }


    public void randomOnClick(){
        System.out.println("random");
        setRandom(0.7);
    }


    public void clearOnClick(){
        System.out.println("clear");
        clearCells();
    }

//=================================================== Private Methods ==================================================

}
