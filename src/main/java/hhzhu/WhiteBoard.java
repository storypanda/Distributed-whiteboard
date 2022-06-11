package hhzhu;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class WhiteBoard {
    LongConnection longConnection;
    private final Stage stage = new Stage();
    //menubar
    MenuBar menuBar = new MenuBar();

    //menu
    Menu fileMenu = new Menu("File");
    Menu helpMenu = new Menu("Help");

    //Canvas
    Canvas canvas = new Canvas(800, 600);
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

    //show collaborator
    Text collaboratorTitle = new Text("Online collaborator: ");
    Text collaborator = new Text();

    public WhiteBoard(){
        Geometry geometry =new Geometry();
        geometry.setLineWidth(1.0);
        geometry.setColor(Color.BLACK);
        geometry.setText("Please click the \"set text\" button to set the text you want to show.");
        Screen screen = Screen.getPrimary();
        AnchorPane ap = new AnchorPane();

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        //menuitem
        MenuItem newFileMenuItem = new MenuItem("New");
        newFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+n"));//Set shortcut keys
        newFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Created successfully");
            }
        });

        MenuItem openFileMenuItem = new MenuItem("Open");
        openFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+o"));//Set shortcut keys
        openFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Open successfully");
            }
        });

        MenuItem saveFileMenuItem = new MenuItem("Save");
        saveFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+s"));//Set shortcut keys
        saveFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Successfully saved");
            }
        });

        MenuItem saveAsFileMenuItem = new MenuItem("Save As");
        saveAsFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+shift+s"));//Set shortcut keys
        saveAsFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Successfully saved as");
            }
        });

        MenuItem exitFileMenuItem = new MenuItem("Exit");
        exitFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+e"));//Set shortcut keys
        exitFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Exit successfully");
            }
        });

        fileMenu.getItems().addAll(newFileMenuItem, openFileMenuItem, saveFileMenuItem, saveAsFileMenuItem, exitFileMenuItem);

        MenuItem helpMenuItem = new MenuItem("Help");
        helpMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+h"));//Set shortcut keys
        helpMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help Information");
                alert.setHeaderText(null);
                alert.setContentText("Please click any button in the upper middle of the canvas to select the type you want to draw. Note: If you want to add text, please click the \"set text\" button to set the text first.");
                alert.showAndWait();
                System.out.println("Help successfully");
            }
        });

        helpMenu.getItems().addAll(helpMenuItem);

        //show collaborator
        HBox hBoxCollaborator = new HBox(collaboratorTitle, collaborator);
        hBoxCollaborator.setAlignment(Pos.TOP_LEFT);

        //toolbar
        ToggleButton line = new ToggleButton("line");
        ToggleButton circle = new ToggleButton("circle");
        ToggleButton triangle = new ToggleButton("triangle");
        ToggleButton rectangle = new ToggleButton("rectangle");
        ToggleButton text = new ToggleButton("text");

        Button setText = new Button("set text");
        setText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg)
            {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Text Input Dialog");
                dialog.setHeaderText(null);
                dialog.setGraphic(null);
                dialog.setContentText("Please enter the text you want:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()){
                    geometry.setText(result.get());
                }
            }
        });

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg)
            {
                Color color = colorPicker.getValue();
                geometry.setColor(color);
            }
        });

        ToggleGroup toggleGroup = new ToggleGroup();
        line.setToggleGroup(toggleGroup);
        circle.setToggleGroup(toggleGroup);
        triangle.setToggleGroup(toggleGroup);
        rectangle.setToggleGroup(toggleGroup);
        text.setToggleGroup(toggleGroup);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        geometry.setStartPointX(event.getX());
                        geometry.setStartPointY(event.getY());
                    }
                });

//        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
//                new EventHandler<MouseEvent>(){
//                    @Override
//                    public void handle(MouseEvent event) {
//
//                    }
//                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        geometry.setEndPointX(event.getX());
                        geometry.setEndPointY(event.getY());

                        if(line.isSelected()){
                            graphicsContext.setStroke(geometry.getColor());
                            graphicsContext.setLineWidth(geometry.getLineWidth());
                            graphicsContext.strokeLine(geometry.getStartPointX(),geometry.getStartPointY(),geometry.getEndPointX(),geometry.getEndPointY());
                            geometry.setType("line");
                            sendGeometry(geometry);
                        }

                        if(circle.isSelected()){
                            double x, y, width, height;
                            if(geometry.getStartPointX()<geometry.getEndPointX()){
                                x = geometry.getStartPointX();
                                width = geometry.getEndPointX()-geometry.getStartPointX();
                            }else{
                                x = geometry.getEndPointX();
                                width = geometry.getStartPointX()-geometry.getEndPointX();
                            }
                            if(geometry.getStartPointY()<geometry.getEndPointY()){
                                y = geometry.getStartPointY();
                                height = geometry.getEndPointY()-geometry.getStartPointY();
                            }else{
                                y = geometry.getEndPointY();
                                height = geometry.getStartPointY()-geometry.getEndPointY();
                            }
                            graphicsContext.setStroke(geometry.getColor());
                            graphicsContext.setLineWidth(geometry.getLineWidth());
                            graphicsContext.strokeOval(x,y,width,height);
                            geometry.setType("circle");
                            sendGeometry(geometry);
                        }

                        if(triangle.isSelected()){
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(geometry.getStartPointX()+(geometry.getEndPointX()-geometry.getStartPointX())/2,geometry.getStartPointY());
                            graphicsContext.lineTo(geometry.getStartPointX(),geometry.getEndPointY());
                            graphicsContext.lineTo(geometry.getEndPointX(),geometry.getEndPointY());
                            graphicsContext.lineTo(geometry.getStartPointX()+(geometry.getEndPointX()-geometry.getStartPointX())/2,geometry.getStartPointY());
                            graphicsContext.setStroke(geometry.getColor());
                            graphicsContext.setLineWidth(geometry.getLineWidth());
                            graphicsContext.stroke();
                            graphicsContext.closePath();
                            geometry.setType("triangle");
                            sendGeometry(geometry);
                        }

                        if(rectangle.isSelected()){
                            double x, y, width, height;
                            if(geometry.getStartPointX()<geometry.getEndPointX()){
                                x = geometry.getStartPointX();
                                width = geometry.getEndPointX()-geometry.getStartPointX();
                            }else{
                                x = geometry.getEndPointX();
                                width = geometry.getStartPointX()-geometry.getEndPointX();
                            }
                            if(geometry.getStartPointY()<geometry.getEndPointY()){
                                y = geometry.getStartPointY();
                                height = geometry.getEndPointY()-geometry.getStartPointY();
                            }else{
                                y = geometry.getEndPointY();
                                height = geometry.getStartPointY()-geometry.getEndPointY();
                            }
                            graphicsContext.setStroke(geometry.getColor());
                            graphicsContext.setLineWidth(geometry.getLineWidth());
                            graphicsContext.strokeRect(x, y, width, height);
                            geometry.setType("rectangle");
                            sendGeometry(geometry);
                        }

                        if(text.isSelected()){
                            graphicsContext.setStroke(geometry.getColor());
                            graphicsContext.setLineWidth(geometry.getLineWidth());
                            graphicsContext.strokeText(geometry.getText(), geometry.getEndPointX(), geometry.getEndPointY());
                            geometry.setType("text");
                            sendGeometry(geometry);
                        }
                    }
                });

        HBox hBoxTop = new HBox(rectangle, triangle, circle, line, text, setText, colorPicker);
        hBoxTop.setAlignment(Pos.CENTER);

        ap.getChildren().addAll(menuBar, canvas, hBoxCollaborator, hBoxTop);
        Scene scene = new Scene(ap);
        stage.setScene(scene);
        stage.setTitle("White Board");
        stage.setWidth(screen.getVisualBounds().getWidth());
        stage.setHeight(screen.getVisualBounds().getHeight());
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();

        //set menubar
        menuBar.setPrefWidth(stage.getWidth());
        menuBar.setPrefHeight(30);

        //set canvas
        ap.setTopAnchor(canvas, menuBar.getHeight());
        canvas.setWidth(stage.getWidth());
        canvas.setHeight(stage.getHeight() - menuBar.getHeight());

        //set collaborator
        ap.setTopAnchor(hBoxCollaborator, 4.0+menuBar.getHeight());

        //set toolbar
        hBoxTop.setPrefWidth(stage.getWidth());
        ap.setTopAnchor(hBoxTop, 4.0+menuBar.getHeight());

        //change length
        ap.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                menuBar.setPrefWidth(stage.getWidth());
                canvas.setWidth(stage.getWidth());
                hBoxTop.setPrefWidth(stage.getWidth());
                System.out.println(hBoxTop.getWidth());
            }
        });

        //change height
        ap.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                canvas.setHeight(stage.getHeight() - menuBar.getHeight());
            }
        });
    }

    public void sendGeometry(Geometry geometry){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cmd", "sendAll");
        jsonObject.put("startPointX", geometry.getStartPointX());
        jsonObject.put("startPointY", geometry.getStartPointY());
        jsonObject.put("endPointX", geometry.getEndPointX());
        jsonObject.put("endPointY", geometry.getEndPointY());
        jsonObject.put("lineWidth", geometry.getLineWidth());
        jsonObject.put("color", geometry.getColor().toString());
        jsonObject.put("text", geometry.getText());
        jsonObject.put("type", geometry.getType());

        longConnection.send(jsonObject, new LongConnection.Callback() {
            @Override
            public void onResult(int status, String reason, Object data)
            {
                if(status != 0)
                {
                    return;
                }

                System.out.println("Send successfully");
            }
        });

    }

    public void showCollaborator(String collaborator){
        if(!this.collaborator.getText().equals(collaborator)){
            this.collaborator.setText(collaborator);
        }
    }

    public void drawGeometry(JSONObject geometryJson){
        Double startPointX = geometryJson.optDouble("startPointX",0.0);
        Double startPointY = geometryJson.optDouble("startPointY", 0.0);
        Double endPointX = geometryJson.optDouble("endPointX", 0.0);
        Double endPointY = geometryJson.optDouble("endPointY", 0.0);
        Double lineWidth = geometryJson.optDouble("lineWidth", 0.0);
        String color = geometryJson.optString("color", "0x000000ff");
        String text = geometryJson.optString("text", "");
        String type = geometryJson.optString("type", "");

        if(type.equals("line")){
            graphicsContext.setStroke(Color.web(color));
            graphicsContext.setLineWidth(lineWidth);
            graphicsContext.strokeLine(startPointX,startPointY,endPointX,endPointY);
        }

        if(type.equals("circle")){
            double x, y, width, height;
            if(startPointX<endPointX){
                x = startPointX;
                width = endPointX-startPointX;
            }else{
                x = endPointX;
                width = startPointX-endPointX;
            }
            if(startPointY<endPointY){
                y = startPointY;
                height = endPointY-startPointY;
            }else{
                y = endPointY;
                height = startPointY-endPointY;
            }
            graphicsContext.setStroke(Color.web(color));
            graphicsContext.setLineWidth(lineWidth);
            graphicsContext.strokeOval(x, y, width, height);
        }

        if(type.equals("triangle")){
            graphicsContext.beginPath();
            graphicsContext.moveTo(startPointX+(endPointX-startPointX)/2,startPointY);
            graphicsContext.lineTo(startPointX,endPointY);
            graphicsContext.lineTo(endPointX,endPointY);
            graphicsContext.lineTo(startPointX+(endPointX-startPointX)/2,startPointY);
            graphicsContext.setStroke(Color.web(color));
            graphicsContext.setLineWidth(lineWidth);
            graphicsContext.stroke();
            graphicsContext.closePath();
        }

        if(type.equals("rectangle")){
            double x, y, width, height;
            if(startPointX<endPointX){
                x = startPointX;
                width = endPointX-startPointX;
            }else{
                x = endPointX;
                width = startPointX-endPointX;
            }
            if(startPointY<endPointY){
                y = startPointY;
                height = endPointY-startPointY;
            }else{
                y = endPointY;
                height = startPointY-endPointY;
            }
            graphicsContext.setStroke(Color.web(color));
            graphicsContext.setLineWidth(lineWidth);
            graphicsContext.strokeRect(x, y, width, height);
        }

        if(type.equals("text")){
            graphicsContext.setStroke(Color.web(color));
            graphicsContext.setLineWidth(lineWidth);
            graphicsContext.strokeText(text, endPointX, endPointY);
        }

    }
}
