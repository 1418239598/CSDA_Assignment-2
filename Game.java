package application;

import static application.Server.clients;
import static application.Server.games;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Game {
  int name;
//  boolean broken=false;
  player player1;
  player player2;
  PrintStream ps1;
  PrintStream ps2;
  public Game(player player1, player player2,int name) {
    this.player1 = player1;
    player1.inGame="in Game";
    this.player2 = player2;
    player2.inGame="in Game";
    this.name=name;
  }
  public void Start() throws Exception {
    HandlerSocketThreadPool handlerSocketThreadPool=
        new HandlerSocketThreadPool(10,100);
    ps1 = new PrintStream(player1.socket.getOutputStream(),true);
    ps2 = new PrintStream(player2.socket.getOutputStream(),true);
    ps1.println("0");
    ps2.println("0");
    handlerSocketThreadPool.execute(new talk_to_client(player1,player2));
    handlerSocketThreadPool.execute(new talk_to_client(player2,player1));
  }
//  public void Stop(HandlerSocketThreadPool handlerSocketThreadPool)
//  {
//    handlerSocketThreadPool.
//  }
}
class HandlerSocketThreadPool {

  private ExecutorService executor;
  public HandlerSocketThreadPool(int maxPoolSize, int queueSize){
    executor = new ThreadPoolExecutor(
        maxPoolSize,
        maxPoolSize,
        12000L,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>(queueSize) );
  }

  public void execute(Runnable task){
    this.executor.execute(task);
  }
}
class talk_to_client implements Runnable {
  player thisplayer;
  player otherplayer;
  public talk_to_client(player socket,player otherplayer) {
    this.thisplayer = socket;
    this.otherplayer=otherplayer;
  }

  @Override
  public void run() {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(thisplayer.socket.getInputStream()));
      PrintStream ps = new PrintStream(otherplayer.socket.getOutputStream(),true);
      String line = null ;
      while((line = br.readLine())!=null){
        if(line.equals("0"))
        {
          ps.println("reset");
          continue;
        }
        if(line.equals("Cancel your thread!"))
        {
          for (int i = 0; i < clients.size() ; i++) {
            if(clients.get(i).inGame.equals("Waiting") && clients.get(i)!=thisplayer)
            {
              games.add(new Game(clients.get(i),thisplayer,Server.gameID++));
              games.get(games.size()-1).Start();
              break;
            }
          }
          break;
        }
        int x= Integer.parseInt(line.split(",")[0]);
        int y= Integer.parseInt(line.split(",")[1]);
        int player= Integer.parseInt(line.split(",")[2]);
        System.out.println("服务端收到了"+Thread.currentThread().getName()+"  数据： "+x+"    "+y);
        ps.println("1,"+x+","+y+","+player);
        System.out.println("1,"+x+","+y+","+player);
      }
    } catch (Exception e) {
      thisplayer.inGame="Run away";
      if(otherplayer.inGame.equals("in Game")) otherplayer.inGame="Waiting";
      try {
        new PrintStream(otherplayer.socket.getOutputStream(),true).println("Your Opponent Run Away!");

      } catch (IOException ex) {
        ex.printStackTrace();
      }
      System.out.println("有人下线了");
      System.out.println(clients);
    }
  }
}
