package hhzhu;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Optional;


/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Geometry geometry =new Geometry();
        geometry.setLineWidth(1.0);
        geometry.setText("Please click the \"set text\" button to set the text you want to show.");
        Screen screen = Screen.getPrimary();
        AnchorPane ap = new AnchorPane();

        //menubar
        MenuBar menuBar = new MenuBar();

        //menu
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        //menuitem
        MenuItem newFileMenuItem = new MenuItem("New",new ImageView("/hhzhu/New_File.png"));
        newFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+n"));//Set shortcut keys
        newFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("创建成功");
            }
        });

        MenuItem openFileMenuItem = new MenuItem("Open",new ImageView("/hhzhu/Open_File.png"));
        openFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+o"));//Set shortcut keys
        openFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("打开成功");
            }
        });

        MenuItem saveFileMenuItem = new MenuItem("Save",new ImageView("/hhzhu/Save_File.png"));
        saveFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+s"));//Set shortcut keys
        saveFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("保存成功");
            }
        });

        MenuItem exitFileMenuItem = new MenuItem("Exit",new ImageView("/hhzhu/Exit_File.png"));
        exitFileMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+e"));//Set shortcut keys
        exitFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("退出成功");
            }
        });

        fileMenu.getItems().addAll(newFileMenuItem, openFileMenuItem, saveFileMenuItem, exitFileMenuItem);

        //Canvas
        Canvas canvas = new Canvas(800, 600);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

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
                        }

                        if(text.isSelected()){
                            graphicsContext.setStroke(geometry.getColor());
                            graphicsContext.setLineWidth(geometry.getLineWidth());
                            graphicsContext.strokeText(geometry.getText(), geometry.getEndPointX(), geometry.getEndPointY());
                        }
                    }
                });

        HBox hBoxTop = new HBox(rectangle, triangle, circle, line, text, setText, colorPicker);
        hBoxTop.setAlignment(Pos.CENTER);

        ap.getChildren().addAll(menuBar, canvas, hBoxTop);
        Scene scene = new Scene(ap);
        stage.setScene(scene);
        stage.setTitle("White Board");
        stage.setWidth(screen.getVisualBounds().getWidth());
        stage.setHeight(screen.getVisualBounds().getHeight());
        stage.show();

        //set menubar
        menuBar.setPrefWidth(stage.getWidth());
        menuBar.setPrefHeight(30);

        //set canvas
        ap.setTopAnchor(canvas, menuBar.getHeight());
        canvas.setWidth(stage.getWidth());
        canvas.setHeight(stage.getHeight() - menuBar.getHeight());

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

    public static void main(String[] args) {
        launch();
    }
}