package application;

import application.controller.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

  static Socket socket;
  static PrintStream ps;
  static BufferedReader br;
  public static MyThread t;

  public static Thread getT() {
    return t;
  }

  public static void PutChess(int x,int y,int player)
  {
    ps.println(x+","+y+","+player);
  }
  public static void Reset()
  {
    ps.println("0");
  }
  public void login() {
    try {
      socket=new Socket("10.24.98.149",4399);
      ps = new PrintStream(socket.getOutputStream(),true);
      br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      Scanner sc = new Scanner(System.in);
      t=new MyThread();
      t.start();
//      while(true){
//        System.out.print("请说：");
//        String msg = sc.nextLine();
//        ps.println(msg);
//        ps.flush();
//      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
class MyThread extends Thread{
  @Override
  public void run(){
    try {
      String line = null;
      while ((line = Client.br.readLine()) != null) {
        if (line.equals("0"))
        {
          System.out.println("Our Game Start!");
          System.out.println("-----------------eat------------shit_________");
          Controller.operation="Our Game Start!";
          continue;
//          Controller.side=
//          controller=new Controller();
        }
        else if(line.equals("reset"))
        {
          System.out.println("clean");
          Controller.operation="Reset";
//          Controller.clean();
        }
        else if(line.equals("Your Opponent Run Away!"))
        {
          System.out.println(line);
          Controller.operation=line;
          Client.ps.println("Cancel your thread!");
        }
        else if(line.split(",")[0].equals("1"))
        {
          System.out.println("opponent action!!"+line);
//          int x= Integer.parseInt(line.split(",")[1]);
//          int y= Integer.parseInt(line.split(",")[2]);
//          int player= Integer.parseInt(line.split(",")[3]);
//          System.out.println(x+":"+y);
//          Controller.chessBoard[x][y]=player;
//          Controller.TURN=!Controller.TURN;
//          Controller.frozen=false;
          Controller.operation=line;
//          Controller.refreshBoard(x,y);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}