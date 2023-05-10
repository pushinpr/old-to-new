package com.example.lab3;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    public static Group group= new Group();

    public static Image imgrabbit;
    public static Image imgcarrot;
    public static Image imgcornField;
    public static Image imgRabbitHole;

//    public static int counter=0;

    public static World world;

    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException, CloneNotSupportedException {

        HelloApplication.stage = stage;
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        //stage.setTitle("Hello!");

        //rabbit.png
        imgrabbit= new Image( HelloApplication.class.getResource("rabbit.png").toString(),
                50,50,false,false);

        imgcarrot= new Image( HelloApplication.class.getResource("carrot.png").toString(),
                30,30,false,false);
        imgcornField = new Image( HelloApplication.class.getResource("CornField.png").toString(),
                400,300,false,false);
        imgRabbitHole = new Image( HelloApplication.class.getResource("RabbitHole.png").toString(),
                400,250,false,false);

        Scene scene = new Scene(group, 1500, 800);
        //Scene scene = new Scene(group, stage.getWidth(), stage.getHeight() );

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                Rabbit r= new Rabbit(   Integer.toString(++counter), 100.0,  mouseEvent.getX(), mouseEvent.getY() );
//                herd.add(r);

                if( mouseEvent.getButton().equals(MouseButton.SECONDARY) ){
                    ChooseRabbitToChangeParamsDlg.display(mouseEvent.getX(), mouseEvent.getY());
                }
                else{
                    boolean flg=world.mousePrimaryActivate(mouseEvent.getX(), mouseEvent.getY());


                    if( flg==false)
                        RabbitParamsDlg.display(mouseEvent.getX(), mouseEvent.getY() );
                }
                //System.out.println("Got control back!");
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

                if(keyEvent.isControlDown()) {
                    if (keyEvent.getCode().equals(KeyCode.V) ){
                            try {
                                world.copyActive();
                            }
                            catch(Exception e){
                                System.out.println("Cloning failed!");
                            }
                    }

                    if (keyEvent.getCode().equals(KeyCode.I) ){
                        world.installActivated();
                    }

                }


                    if (keyEvent.getCode().equals(KeyCode.DELETE) )
                {
                    world.deleteActive();
                }

                boolean flg=false;
                double dx=0.0;
                double dy=0.0;

                if (keyEvent.getCode().equals(KeyCode.W) )
                {
                    dy=-10.0; flg= true;
                }
                if (keyEvent.getCode().equals(KeyCode.A) )
                {
                    dx=-10.0; flg= true;
                }
                if (keyEvent.getCode().equals(KeyCode.S) )
                {
                    dy=10.0; flg= true;
                }
                if (keyEvent.getCode().equals(KeyCode.D) )
                {
                    dx=10.0; flg= true;
                }

                if( flg ){
                    world.moveActive(dx, dy);
                }

            }
        });

        stage.setScene(scene);
        stage.show();

        world= new World(this, group, imgrabbit, imgcarrot, imgcornField, imgRabbitHole );

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                    world.lifeCycle();
            }
        };
        timer.start();

        AddingCLI cli = new AddingCLI(world);
        Thread cliThread = new Thread(cli::processCommandLine);
        cliThread.setDaemon(true);
        cliThread.start();

        world.CLIinitialize();

    }
    public double getScreenWX(){
        return HelloApplication.stage.getWidth();
    }

    public double getScreenWY(){
        return HelloApplication.stage.getHeight();
    }
    public static void main(String[] args) {

        launch();
//        String str;
//        int count=0;
//
//        Scanner in = new Scanner(System.in);
//
//        do{
//            System.out.print("Enter string N"+(count+1)+":");
//            str= in.nextLine();
//            System.out.println(str);
//            count++;
//            if(count==3)launch();
//
//        }while(count<5);
//

    }
}
