package com.example.game_of_life_v1;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML Button nextStateBtn;
    @FXML AnchorPane game_view;

    private Game_of_Life game_of_life;//is a Pane

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1),
                                    e -> {
                                                nextState();
                                        }
                                    ));
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int rowNum = 200;
        int colNum = 100;
        int[][] t = {{0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,1,0,0,0,0},
                     {0,0,0,0,1,1,0,0,0,0},
                     {0,0,0,0,1,1,0,0,0,0},
                     {0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,0,0,0,0,0},
                     {0,0,0,0,0,0,0,0,0,0},
        };
//        game_of_life.setCells(t);

        game_of_life = new Game_of_Life(rowNum,colNum);
        game_view.getChildren().add(game_of_life);
        game_of_life.paint();

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
//=================================================== Public Methods ===================================================
    @FXML
    public void nextState(){
        game_of_life.nextGeneration();
        game_of_life.paint();
    }
    @FXML
    public void setRandom(){
        game_of_life.setRandom();
    }
    @FXML
    public void setClear(){
        game_of_life.clear();
    }


//=================================================== Private Methods ==================================================

}


class Game_of_Life extends AnchorPane{
    private Cell[][] curGen;
    private Cell[][] nextGen;
    private final int rowCellsNum;
    private final int colCellsNum;
    private final double squareSize = 2;

    public Game_of_Life(int rowCellsNum, int colCellsNum){
        this.rowCellsNum = rowCellsNum;
        this.colCellsNum = colCellsNum;
        curGen = createEmptyCells();
        nextGen = createEmptyCells();
        setUp();
    }

    public void nextGeneration(){
        for (int i = 1; i < rowCellsNum - 1; i++) {
            for (int j = 1; j < colCellsNum - 1; j++) {
                int aliveNeighboursNum = 0;
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        //todo : rule set up
                        if(k == 0 && l == 0)continue;
                        if(curGen[i+k][j+l].isAlive()){//?
                            aliveNeighboursNum++;
                        }
                    }
                }

                if(curGen[i][j].isAlive()){
                    if(aliveNeighboursNum < 2){
                        nextGen[i][j].setState(false);
                    }else if (aliveNeighboursNum > 3){
                        nextGen[i][j].setState(false);
                    }else{
                        nextGen[i][j].setState(true);
                    }
                }else {
                    if(aliveNeighboursNum == 3){
                        nextGen[i][j].setState(true);
                    }else {
                        nextGen[i][j].setState(false);
                    }
                }
            }
        }
        Cell[][] temp;
        temp = curGen;
        curGen = nextGen;
        nextGen = temp;
    }

    public void setRandom(){
        for (int i = 1; i < rowCellsNum - 1; i++) {
            for (int j = 1; j < colCellsNum - 1; j++) {
                curGen[i][j] = new Cell(Math.random() > 0.5);
            }
        }
    }

    public void paint(){
        this.getChildren().clear();
        for (int i = 0; i < rowCellsNum; i++) {
            for (int j = 0; j < colCellsNum; j++) {
                Rectangle temp = new Rectangle(squareSize,squareSize);
                if (curGen[i][j].isAlive()){
                    temp.setStroke(Color.GREEN);
                    temp.setFill(Color.GREEN);
                }else {
                    temp.setStroke(Color.WHITE);
                    temp.setFill(Color.WHITE);
                }
                this.getChildren().add(temp);
                AnchorPane.setTopAnchor(temp,0 + squareSize * i);
                AnchorPane.setLeftAnchor(temp,0 + squareSize * j);
            }
        }
    }

    public void setCells(int[][] t){
        Cell[][] temp = new Cell[rowCellsNum][colCellsNum];
        for (int i = 0; i < rowCellsNum; i++) {
            for (int j = 0; j < colCellsNum; j++) {
                temp[i][j] = new Cell(t[i][j] > 0);
            }
        }
        curGen = temp;
    }

    public void clear(){
        curGen = createEmptyCells();
        nextGen = createEmptyCells();
    }
//=================================================== Private Methods ==================================================

    private void setUp(){

    }

    private Cell[][] createEmptyCells(){
        Cell[][] temp = new Cell[rowCellsNum][colCellsNum];
        for (int i = 0; i < rowCellsNum; i++) {
            for (int j = 0; j < colCellsNum; j++) {
                temp[i][j] = new Cell(false);
            }
        }
        return temp;
    }


}
