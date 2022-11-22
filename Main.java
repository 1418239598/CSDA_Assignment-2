package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * . description:
 *
 * @Param $PARAMS$ $RETURN$
 */
public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      Client client = new Client();
      client.login();
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
      Pane root = fxmlLoader.load();
      primaryStage.setTitle("Tic Tac Toe");
      primaryStage.setScene(new Scene(root));
      primaryStage.setResizable(false);
      primaryStage.show();

      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
          System.out.print("监听到窗口关闭");
          Client.ps.println("disappear");
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
