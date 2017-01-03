package de.lv1871.dms.GameOfLife;

import de.lv1871.dms.GameOfLife.domain.Field;
import de.lv1871.dms.GameOfLife.domain.GameOfLife;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.Consumer;

public class JavaFxGameOfLife extends Application {

    private static final int X_SIZE = 50;
    private static final int Y_SIZE = 50;

    private GameOfLife gameOfLife = new GameOfLife(X_SIZE, Y_SIZE);

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, X_SIZE * 10, Y_SIZE * 10, Color.WHITE);

        primaryStage.setScene(scene);

        Timeline tl = new Timeline();
        tl.setCycleCount(Animation.INDEFINITE);
        KeyFrame fields = new KeyFrame(Duration.seconds(.200),
                event -> {
                    gameOfLife.fieldStream().forEach(renderField(root));
                    gameOfLife.iterateGameboard();
                });

        tl.getKeyFrames().add(fields);
        tl.play();

        primaryStage.show();

    }

    private Consumer<Field> renderField(Group root) {
        return field -> {
            Rectangle rect = new Rectangle(field.getX() * 10, field.getY() * 10, 10, 10);

            if (field.getAlive()) {
                rect.setFill(Color.BURLYWOOD);
            } else {
                rect.setFill(Color.WHITE);
            }

            root.getChildren().add(rect);
        };
    }
}
