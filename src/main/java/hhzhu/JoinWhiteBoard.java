package hhzhu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class JoinWhiteBoard extends Application {
    LongConnection longConnection;
    @Override
    public void start(Stage stage) {
        Label userName = new Label("User Name:");
        userName.setFont(Font.font(15.0));

        Label serverIPAddress = new Label("Server IP Address:");
        serverIPAddress.setFont(Font.font(15.0));

        Label serverPort = new Label("Server Port:");
        serverPort.setFont(Font.font(15.0));

        TextField userNameTextField = new TextField();
        TextField serverIPAddressTextField = new TextField();
        TextField serverPortTextField = new TextField();

        Button join = new Button("Join");
        Button cancel = new Button("Cancel");

        GridPane gridPane = new GridPane();
        gridPane.add(userName, 0, 0);
        gridPane.add(userNameTextField, 1, 0);
        gridPane.add(serverIPAddress, 0, 1);
        gridPane.add(serverIPAddressTextField, 1, 1);
        gridPane.add(serverPort, 0, 2);
        gridPane.add(serverPortTextField, 1, 2);
        gridPane.add(join, 0, 3);
        gridPane.add(cancel, 1, 3);

        gridPane.setHgap(5);
        gridPane.setVgap(17);

        GridPane.setMargin(join, new Insets(0, 0, 0, 120));
        gridPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.setTitle("Join WhiteBoard");
        stage.setWidth(500);
        stage.setHeight(300);
        stage.setResizable(false);
        stage.show();

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg)
            {
                Platform.exit();
                System.exit(0);
            }
        });

        join.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg)
            {
                String name = userNameTextField.getText();
                if(name.length() == 0) return;

                String server = serverIPAddressTextField.getText().trim();
                try {
                    Integer.parseInt(serverPortTextField.getText().trim());
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Port number should be in range:(0 - 65353)!");
                    alert.showAndWait();
                    return;
                }
                int port = Integer.parseInt(serverPortTextField.getText().trim());

                longConnection = new LongConnection(server, port);
                JSONObject jreq = new JSONObject();
                jreq.put("cmd", "login");
                jreq.put("name", name);

                longConnection.send( jreq, new LongConnection.Callback() {
                    @Override
                    public void onResult(int status, String reason, Object data)
                    {
                        if(status != 0)
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Unable to connect to the server, please enter the correct IP address and port number");
                            alert.showAndWait();
                            return;
                        }

                        //Save current session information
                        JSONObject jdata = (JSONObject)data;
                        SessionInfo.session = jdata.optInt("session", 0);
                        SessionInfo.name = name;

                        //Enter the whiteboard
                        WhiteBoard whiteBoard = new WhiteBoard();
                        whiteBoard.longConnection = longConnection;
                        stage.close();

                        //Start message receiving thread + polling
                        GeometryRetrieveTask task = new GeometryRetrieveTask(whiteBoard, longConnection);
                        task.start();
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
