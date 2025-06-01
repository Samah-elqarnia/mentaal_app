package com.example.mental_health;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;
public class UiRating extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        content.setPrefSize(320, 320);
        content.setStyle("-fx-background-color: #262E38; -fx-background-radius: 30;");

        // Star Icon
        StackPane starContainer = new StackPane();
        starContainer.setPrefSize(40, 40);
        starContainer.setStyle("-fx-background-color: rgba(125, 136, 158, 0.4); -fx-background-radius: 50%;");

        SVGPath star = new SVGPath();
        star.setContent("M9.067.43l1.99 4.031c.112.228.33.386.58.422l4.45.647a.772.772 0 0 1 .427 1.316l-3.22 3.138a.773.773 0 0 0-.222.683l.76 4.431a.772.772 0 0 1-1.12.813l-3.98-2.092a.773.773 0 0 0-.718 0l-3.98 2.092a.772.772 0 0 1-1.119-.813l.76-4.431a.77.77 0 0 0-.222-.683L.233 6.846A.772.772 0 0 1 .661 5.53l4.449-.647a.772.772 0 0 0 .58-.422L7.68.43a.774.774 0 0 1 1.387 0Z");
        star.setFill(Color.web("#FC7614"));
        star.setScaleX(0.9);
        star.setScaleY(0.9);
        starContainer.getChildren().add(star);

        Label title = new Label("How did we do?");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.WHITE);

        Label text = new Label("Please let us know how we did with your support request. All feedback is appreciated to help us improve our offering!");
        text.setWrapText(true);
        text.setFont(Font.font("Arial", 13));
        text.setTextFill(Color.web("#7C8798"));

        // Rating buttons
        HBox ratingBox = new HBox(10);
        ratingBox.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 1; i <= 5; i++) {
            ToggleButton button = new ToggleButton(String.valueOf(i));
            button.setToggleGroup(toggleGroup);
            button.setPrefSize(40, 40);
            button.setStyle("-fx-background-color: rgba(125, 136, 158, 0.4); -fx-background-radius: 50%; -fx-text-fill: white;");

            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: white; -fx-background-radius: 50%; -fx-text-fill: #262E38;"));
            button.setOnMouseExited(e -> {
                if (!button.isSelected()) {
                    button.setStyle("-fx-background-color: rgba(125, 136, 158, 0.4); -fx-background-radius: 50%; -fx-text-fill: white;");
                }
            });
            button.setOnAction(e -> {
                for (Toggle toggle : toggleGroup.getToggles()) {
                    if (toggle != button) {
                        ((ToggleButton) toggle).setStyle("-fx-background-color: rgba(125, 136, 158, 0.4); -fx-background-radius: 50%; -fx-text-fill: white;");
                    }
                }
                button.setStyle("-fx-background-color: #FC7614; -fx-background-radius: 50%; -fx-text-fill: white;");
            });
            ratingBox.getChildren().add(button);
        }

        // Submit Button
        Button submit = new Button("Submit");
        submit.setPrefWidth(280);
        submit.setStyle("-fx-background-color: #FC7614; -fx-text-fill: #262E38; -fx-font-weight: bold; -fx-background-radius: 30; -fx-text-transform: uppercase;");

        content.getChildren().addAll(starContainer, title, text, ratingBox, submit);

        Scene scene = new Scene(content);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rating Component");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
