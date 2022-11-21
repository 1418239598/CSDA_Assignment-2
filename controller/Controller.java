package application.controller;

import application.Client;
//import application.CommonData;
import application.Game;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    public TextArea textAreaLine;
    public TextArea textAreaCircle;

    @FXML
    private Pane base_square;
    @FXML
    private Button reset;

    @FXML
    private Rectangle game_panel;

    public static boolean finish=false;
    public static boolean TURN = false;

    public static  int[][] chessBoard = new int[3][3];
    private static  boolean[][] flag = new boolean[3][3];
    public static boolean frozen=true;
    public static String operation=null;
    private AnimationTimer timer;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game_panel.setOnMouseClicked(event -> {
            int x = (int) (event.getX() / BOUND);
            int y = (int) (event.getY() / BOUND);
            if (refreshBoard(x, y)) {
                Client.PutChess(x,y,TURN?PLAY_1 : PLAY_2);
                frozen=true;
                TURN = !TURN;
            }
        });
//        game_panel.setOnMouseMoved(event -> {
//            drawChess();
//            ifFinish();
//        });
        textAreaLine.appendText("Be Patient!\nWe are searching for your opponent......");
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                drawChess();
//                ifFinish();
                if(operation!=null)
                {
                    if(operation.equals("Our Game Start!"))
                    {
//                        if(base_square.getChildren().size()>5)
//                        {
//                            for (int i = base_square.getChildren().size()-1; i >=5 ; i--) {
//                                base_square.getChildren().remove(i);
//                            }
//                        }
//                        chessBoard=new int[3][3];
//                        flag=new boolean[3][3];
                        textAreaLine.clear();
                        textAreaLine.appendText("Find Your Opponent!\nGame Start!");
                        textAreaCircle.clear();
                        textAreaCircle.appendText("If you wanna go first. Click the square as soon as possible!");
                        clean();
//                        frozen=false;
//                        operation=null;
                    }
                    else if(operation.equals("Your Opponent Run Away!"))
                    {
                        textAreaCircle.clear();
                        textAreaCircle.appendText(operation);
                        textAreaLine.clear();
                        textAreaLine.appendText("Waiting for your next opponent......");
                        operation=null;
                    }
                    else if(operation.split(",")[0].equals("1"))
                    {
                        textAreaCircle.clear();
                        textAreaLine.clear();
                        textAreaLine.appendText(operation.split(",")[3].equals("2")?"\nYou are Circle.":"\nYou are Line.");
                        textAreaCircle.appendText("Your opponent put chess on ("
                            +operation.split(",")[1]+","+ operation.split(",")[2]+
                            ")\nYour color is "+(operation.split(",")[3].equals("2")?"circle":"line")+"\n"
                            +"\nMake your decision as soon as possible!");

                        int x= Integer.parseInt(operation.split(",")[1]);
                        int y= Integer.parseInt(operation.split(",")[2]);
                        int player= Integer.parseInt(operation.split(",")[3]);
                        System.out.println(x+":"+y);
                        chessBoard[x][y]=player;
                        TURN=!Controller.TURN;
                        frozen=false;
                        drawChess();
                        ifFinish();
                        operation=null;
                    }
                }

            }
        };

        timer.start();
//        reset.setOnMouseClicked(event -> {
//            clean();
//        });
    }
    public void clean()
    {
        System.out.println("cleaning.........");
        if(base_square.getChildren().size()>5)
        {
            for (int i = base_square.getChildren().size()-1; i >=5 ; i--) {
                base_square.getChildren().remove(i);
            }
        }
        finish=false;
        chessBoard=new int[3][3];
        flag=new boolean[3][3];
        frozen=false;
        TURN=false;
        operation=null;
    }
    private void ifFinish()
    {

        for (int i = 0; i <3 ; i++) {
            if(chessBoard[i][0]==chessBoard[i][1] && chessBoard[i][1]==chessBoard[i][2] && chessBoard[i][0]!=EMPTY)
            {
                System.out.println(chessBoard[i][0]+"  win!");
                finish=true;
            }
            else if(chessBoard[0][i]==chessBoard[1][i] && chessBoard[1][i]==chessBoard[2][i] && chessBoard[0][i]!=EMPTY)
            {
                System.out.println(chessBoard[0][i]+"  win!");
                finish=true;
            }
        }
        if((chessBoard[0][0]==chessBoard[1][1] && chessBoard[1][1]==chessBoard[2][2] && chessBoard[1][1]!=EMPTY) ||
            (chessBoard[0][2]==chessBoard[1][1] && chessBoard[1][1]==chessBoard[2][0] && chessBoard[1][1]!=EMPTY))
        {
            System.out.println(chessBoard[1][1]+"  win!");
            finish=true;
        }
        if(finish) {
//            timer.stop();
            System.out.println(TURN);
            if(!TURN) {
                textAreaCircle.clear();
                textAreaCircle.appendText("You Win!");
            }
            else{
                textAreaCircle.clear();
                textAreaCircle.appendText("You Lose!");
            }
//            clean();
            return;
//            final Alert finishAlert=new Alert(AlertType.CONFIRMATION,"Game Finish!");
//            finishAlert.setTitle("");
//            if(!TURN) finishAlert.setHeaderText("You Win!");
//            else finishAlert.setHeaderText("You Lose!");
//            finishAlert.show();
        }
        boolean full=true;
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                if(!flag[i][j]) full=false;
            }
        }
        if(full) {
            textAreaCircle.clear();
            textAreaCircle.appendText("Tie!");
            return;
        }
    }


    private boolean refreshBoard (int x, int y) {
        if(frozen || finish) return false;
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2;
            drawChess();
            textAreaCircle.clear();
            textAreaCircle.appendText("Waiting for your opponent's move......");
            ifFinish();
            return true;
        }
        return false;
    }

    private void drawChess () {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
                    // This square has been drawing, ignore.
                    continue;
                }
                switch (chessBoard[i][j]) {
                    case PLAY_1:
                        drawCircle(i, j);
                        break;
                    case PLAY_2:
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }
    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }
}
//class AlertBox {
//
//    public void display(String title , String message){
//        Stage window = new Stage();
//        window.setTitle("title");
//        //modality要使用Modality.APPLICATION_MODEL
//        window.initModality(Modality.APPLICATION_MODAL);
//        window.setMinWidth(300);
//        window.setMinHeight(150);
//
//        Button button = new Button("Close the window");
//        button.setOnAction(e -> window.close());
//
//        Label label = new Label(message);
//
//        VBox layout = new VBox(10);
//        layout.getChildren().addAll(label , button);
//        layout.setAlignment(Pos.CENTER);
//
//        Scene scene = new Scene(layout);
//        window.setScene(scene);
//        //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
//        window.showAndWait();
//    }
//}