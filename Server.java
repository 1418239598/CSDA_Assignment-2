package application;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

  static ArrayList<player> clients=new ArrayList<>();
  static ArrayList<Game> games=new ArrayList<>();
  static int gameID=0;
//  public static final int[][] chessBoard = new int[3][3];
  public static void main(String[] args) {
    try{
      System.out.println("--------Server Start!--------");
      ServerSocket serverSocket=new ServerSocket(4399);

      while(true)
      {
        Socket socket=serverSocket.accept();
        clients.add(new player(socket));
          for (int i = 0; i <clients.size()-1 ; i++) {
            if(clients.get(i).inGame.equals("Waiting"))
            {
              games.add(new Game(clients.get(i),clients.get(clients.size()-1),gameID++));
              games.get(games.size()-1).Start();
              break;
            }
          }


        System.out.println(clients);
        System.out.println(games);
        System.out.println("Someone comes online!");

      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
class player
{
//  String name;
  Socket socket;
  String inGame="Waiting";
  public player(Socket socket)
  {
//    this.name=name;
    this.socket=socket;
  }

  @Override
  public String toString() {
    return "player{" +
        "socket=" + socket +
        ", inGame=" + inGame +
        '}';
  }
}
