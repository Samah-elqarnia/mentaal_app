package com.example.mental_health;

import javafx.application.Application;
import java.net.URL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainPage extends Application {

    // Composants principaux
    private TabPane tabPane;
    private Stage primaryStage;

    // Onglet Humeur du jour
    private Slider moodSlider;
    private Label moodLabel;
    private HBox moodEmojis;
    private TextArea dailyNote;
    private Button saveMoodButton;
    private Label dateLabel;

    // Onglet Journal
    private TextArea journalTextArea;
    private Button saveJournalButton;
    private ComboBox<String> journalDatePicker;
    private Button loadJournalButton;

    // Onglet Objectifs
    private ListView<String> goalsList;
    private TextField newGoalField;
    private Button addGoalButton;
    private Button deleteGoalButton;
    private ProgressBar goalProgressBar;
    private Label progressLabel;

    // Onglet Rappels
    private ListView<String> remindersList;
    private TextField reminderTextField;
    private ComboBox<String> reminderTimeCombo;
    private ComboBox<String> reminderFrequencyCombo;
    private Button addReminderButton;
    private Button deleteReminderButton;
    private CheckBox enableRemindersCheckBox;

    // Onglet Statistiques
    private LineChart<Number, Number> moodChart;
    private ComboBox<String> periodComboBox;
    private Label averageMoodLabel;
    private Label mostFrequentMoodLabel;
    private Label missedDaysLabel;
    private Button refreshStatsButton;

    // Onglet IA
    private TextArea aiAnalysisArea;
    private Button analyzeButton;
    private Label trendLabel;
    private TextArea recommendationsArea;
    private Label predictionLabel;
    private HBox metricsBox;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Assistant Bien-être Mental");

        // Création du TabPane principal
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Création des onglets
        Tab moodTab = createMoodTab();
        Tab journalTab = createJournalTab();
        Tab goalsTab = createGoalsTab();
        Tab remindersTab = createRemindersTab();
        Tab statsTab = createStatsTab();
        Tab aiTab = createAITab();

        tabPane.getTabs().addAll(moodTab, journalTab, goalsTab, remindersTab, statsTab, aiTab);

        Scene scene = new Scene(tabPane, 1000, 1000);
        Font.loadFont(getClass().getResourceAsStream("/fonts/JacquesFrancois-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Agbalumo-Regular.ttf"), 14);

        // Ajout du fichier CSS
        URL cssUrl = this.getClass().getResource("/mental_health_styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            System.out.println("CSS chargé avec succès");
        } else {
            System.err.println("Fichier CSS non trouvé !");
        }

        // Style de base pour la racine
        scene.getRoot().getStyleClass().add("root");

        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialisation des données par défaut
        initializeDefaultData();
    }

    private Tab createMoodTab() {
        Tab tab = new Tab("Humeur du Jour");
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("container-responsive");

        // Date du jour
        dateLabel = new Label("Aujourd'hui : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.getStyleClass().addAll("date-label", "text-center");

        // Titre
        Label titleLabel = new Label("Comment vous sentez-vous aujourd'hui ?");
        titleLabel.getStyleClass().addAll("title-main", "text-center");




        // Curseur d'humeur
        moodSlider = new Slider(1, 5, 3);
        moodSlider.setShowTickLabels(true);
        moodSlider.setShowTickMarks(true);
        moodSlider.setMajorTickUnit(1);
        moodSlider.setSnapToTicks(true);
        moodSlider.setPrefWidth(300);

        moodLabel = new Label("Neutre (3/5)");
        moodLabel.getStyleClass().addAll("mood-label-neutral", "text-center");
        moodLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Emojis d'humeur
        moodEmojis = new HBox(10);
        moodEmojis.setAlignment(Pos.CENTER);
        createMoodEmojis();

        // Zone de commentaire
        Label noteLabel = new Label("Ajoutez une note personnelle (optionnel) :");
        noteLabel.getStyleClass().add("title-secondary");

        dailyNote = new TextArea();
        dailyNote.setPrefRowCount(4);
        dailyNote.setPrefWidth(400);
        dailyNote.setPromptText("Qu'est-ce qui a influencé votre humeur aujourd'hui ?");

        // Bouton sauvegarder
        saveMoodButton = new Button("Enregistrer");
        saveMoodButton.getStyleClass().addAll("button", "button-primary");
        saveMoodButton.setPrefWidth(200);

        // Listeners
        moodSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateMoodLabel(newVal.intValue());
        });

        saveMoodButton.setOnAction(e -> saveMoodData());

        content.getChildren().addAll(dateLabel, titleLabel, moodSlider, moodLabel, moodEmojis,
                noteLabel, dailyNote, saveMoodButton);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("scroll-pane");
        tab.setContent(scrollPane);
        return tab;
    }

    private Tab createJournalTab() {
        Tab tab = new Tab("Journal Personnel");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("container-responsive");

        // Titre
        // Création du label avec le titre
        Label titleLabel = new Label("Mon Journal Personnel");
        titleLabel.getStyleClass().addAll("title-main", "text-center");

// Chargement de l'image
        URL labelPhoto = getClass().getResource("/images/diary (1).png");
        ImageView photoView = new ImageView();

        if (labelPhoto == null) {
            System.err.println("Image not found: /images/diary (1).png");
        } else {
            Image photoImage = new Image(labelPhoto.toExternalForm());
            photoView.setImage(photoImage);
            photoView.setFitWidth(50);
            photoView.setPreserveRatio(true);
            HBox.setMargin(photoView, new Insets(0, 5, 0, 0)); // marge entre l'image et le texte
        }

// Création du conteneur HBox
        HBox container = new HBox();
        container.getChildren().addAll(photoView, titleLabel);
        container.setAlignment(Pos.CENTER_LEFT); // Alignement horizontal


        // Sélection de date
        HBox dateSelection = new HBox(10);
        dateSelection.setAlignment(Pos.CENTER_LEFT);
        dateSelection.getStyleClass().add("info-box");

        Label dateSelectionLabel = new Label("filtre des dates");
        dateSelectionLabel.getStyleClass().add("text-bold");

        journalDatePicker = new ComboBox<>();
        journalDatePicker.setPrefWidth(150);

        loadJournalButton = new Button("Charger");
        loadJournalButton.getStyleClass().addAll("button", "button-secondary");

        dateSelection.getChildren().addAll(dateSelectionLabel, journalDatePicker, loadJournalButton);

        // Zone de texte principale
        Label textLabel = new Label("Écriture libre - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        textLabel.getStyleClass().addAll("title-secondary", "margin-top-20");

        journalTextArea = new TextArea();
        journalTextArea.setPrefRowCount(11);
        journalTextArea.setPromptText("Écrivez vos pensées, réflexions, événements marquants de la journée...");

        // Bouton sauvegarde
        saveJournalButton = new Button("Sauvegarder ");
        saveJournalButton.getStyleClass().addAll("button", "button-primary");
        saveJournalButton.setPrefWidth(200);

        // Listeners
        loadJournalButton.setOnAction(e -> loadJournalEntry());
        saveJournalButton.setOnAction(e -> saveJournalEntry());

        content.getChildren().addAll(titleLabel, dateSelection, textLabel, journalTextArea, saveJournalButton);

        tab.setContent(content);
        return tab;
    }

    private Tab createGoalsTab() {
        Tab tab = new Tab("Objectifs");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("container-responsive");

        // Titre
        Label titleLabel = new Label("Mes Objectifs Personnels");
        titleLabel.getStyleClass().addAll("title-main", "text-center");

        // Ajout d'objectif
        HBox addGoalBox = new HBox(5);
        addGoalBox.setAlignment(Pos.CENTER_LEFT);
        addGoalBox.getStyleClass().add("info-box");
        addGoalBox.setPrefWidth(300);
        addGoalBox.setMaxWidth(600);
        addGoalBox.setMinWidth(100);

        Label addGoalLabel = new Label("Nouvel objectif :");
        addGoalLabel.getStyleClass().add("text-bold");

        newGoalField = new TextField();
        newGoalField.setPrefWidth(300);
        newGoalField.setPromptText("Ex: Faire du sport 3 fois par semaine");

        addGoalButton = new Button("Ajouter");
        addGoalButton.getStyleClass().addAll("button", "button-secondary");

        addGoalBox.getChildren().addAll(addGoalLabel, newGoalField, addGoalButton);

        // Liste des objectifs
        Label goalsListLabel = new Label("Mes objectifs actuels :");
        goalsListLabel.getStyleClass().addAll("title-secondary", "margin-top-20");

        goalsList = new ListView<>();
        goalsList.setPrefHeight(170);
        goalsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Boutons de gestion
        HBox goalsButtons = new HBox(10);
        goalsButtons.setAlignment(Pos.CENTER);

        deleteGoalButton = new Button("Supprimer");
        deleteGoalButton.getStyleClass().addAll("button", "button-danger");

        Button markCompleteButton = new Button("Marquer accompli");
        markCompleteButton.getStyleClass().addAll("button", "button-success");

        goalsButtons.getChildren().addAll(deleteGoalButton, markCompleteButton);

        // Progression
        VBox progressBox = new VBox(10);
        progressBox.getStyleClass().add("stats-box");

        Label progressTitle = new Label("Progression hebdomadaire :");
        progressTitle.getStyleClass().addAll("title-secondary", "text-bold");

        goalProgressBar = new ProgressBar(0);
        goalProgressBar.setPrefWidth(300);

        progressLabel = new Label("0% complété cette semaine");
        progressLabel.getStyleClass().add("text-bold");

        progressBox.getChildren().addAll(progressTitle, goalProgressBar, progressLabel);

        // Listeners
        addGoalButton.setOnAction(e -> addGoal());
        deleteGoalButton.setOnAction(e -> deleteGoal());
        markCompleteButton.setOnAction(e -> markGoalComplete());

        content.getChildren().addAll(titleLabel, addGoalBox, goalsListLabel, goalsList,
                goalsButtons, progressBox);

        tab.setContent(content);
        return tab;
    }

    private Tab createRemindersTab() {
        Tab tab = new Tab("Rappels");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("container-responsive");

        // Titre
        Label titleLabel = new Label("Rappels Bien-être");
        titleLabel.getStyleClass().addAll("title-main", "text-center");

        // Activation des rappels
        enableRemindersCheckBox = new CheckBox("Activer les rappels");
        enableRemindersCheckBox.setSelected(true);
        enableRemindersCheckBox.getStyleClass().addAll("check-box", "text-bold");

        // Ajout de rappel
        Label addReminderLabel = new Label("Créer un nouveau rappel :");
        addReminderLabel.getStyleClass().addAll("title-secondary", "margin-top-20");

        GridPane reminderForm = new GridPane();
        reminderForm.setHgap(10);
        reminderForm.setVgap(10);
        reminderForm.getStyleClass().add("info-box");

        Label messageLabel = new Label("Message :");
        messageLabel.getStyleClass().add("text-bold");
        reminderForm.add(messageLabel, 0, 0);

        reminderTextField = new TextField();
        reminderTextField.setPrefWidth(250);
        reminderTextField.setPromptText("Ex: Pensez à faire une pause !");
        reminderForm.add(reminderTextField, 1, 0);

        Label timeLabel = new Label("Heure :");
        timeLabel.getStyleClass().add("text-bold");
        reminderForm.add(timeLabel, 0, 1);

        reminderTimeCombo = new ComboBox<>();
        fillTimeComboBox();
        reminderForm.add(reminderTimeCombo, 1, 1);

        Label frequencyLabel = new Label("Fréquence :");
        frequencyLabel.getStyleClass().add("text-bold");
        reminderForm.add(frequencyLabel, 0, 2);

        reminderFrequencyCombo = new ComboBox<>();
        reminderFrequencyCombo.getItems().addAll("Quotidien", "Lundi-Vendredi", "Week-end", "Personnalisé");
        reminderFrequencyCombo.setValue("Quotidien");
        reminderForm.add(reminderFrequencyCombo, 1, 2);

        addReminderButton = new Button("Ajouter Rappel");
        addReminderButton.getStyleClass().addAll("button", "button-success");
        reminderForm.add(addReminderButton, 1, 3);

        // Liste des rappels
        Label remindersListLabel = new Label("Rappels configurés :");
        remindersListLabel.getStyleClass().addAll("title-secondary", "margin-top-20");

        remindersList = new ListView<>();
        remindersList.setPrefHeight(150);

        deleteReminderButton = new Button("Supprimer le rappel sélectionné");
        deleteReminderButton.getStyleClass().addAll("button", "button-danger");

        // Listeners
        addReminderButton.setOnAction(e -> addReminder());
        deleteReminderButton.setOnAction(e -> deleteReminder());

        content.getChildren().addAll(titleLabel, enableRemindersCheckBox, addReminderLabel,
                reminderForm, remindersListLabel, remindersList, deleteReminderButton);

        tab.setContent(content);
        return tab;
    }

    private Tab createStatsTab() {
        Tab tab = new Tab("Progression");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("container-responsive");

        // Titre
        Label titleLabel = new Label("Évolution de mon Bien-être");
        titleLabel.getStyleClass().addAll("title-main", "text-center");

        // Sélection de période
        HBox periodSelection = new HBox(10);
        periodSelection.setAlignment(Pos.CENTER_LEFT);

        periodComboBox = new ComboBox<>();
        periodComboBox.getItems().addAll("7 derniers jours", "14 derniers jours", "30 derniers jours", "3 derniers mois");
        periodComboBox.setValue("7 derniers jours");

        refreshStatsButton = new Button("Actualiser");
        refreshStatsButton.getStyleClass().addAll("button", "button-secondary");

        periodSelection.getChildren().addAll(periodComboBox, refreshStatsButton);

        // Graphique
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Jours");
        NumberAxis yAxis = new NumberAxis(1, 5, 1);
        yAxis.setLabel("Niveau d'humeur");

        moodChart = new LineChart<>(xAxis, yAxis);
        moodChart.setPrefHeight(300);
        moodChart.setCreateSymbols(true);
        moodChart.getStyleClass().add("chart");

        // Zone des cercles d'information (initialisée vide)
        metricsBox = new HBox(50);
        metricsBox.setStyle("-fx-background-color: #FFFFFF;");
        metricsBox.setAlignment(Pos.CENTER);
        metricsBox.setPadding(new Insets(10));

        // Listeners
        refreshStatsButton.setOnAction(e -> refreshStatistics());
        periodComboBox.setOnAction(e -> refreshStatistics());

        // Contenu global
        content.getChildren().addAll(titleLabel, periodSelection, moodChart, metricsBox);

        // Appel initial
        refreshStatistics();

        tab.setContent(content);
        return tab;
    }


    private Tab createAITab() {
        Tab tab = new Tab("Assistant IA");
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("container-responsive");

        // Titre
        Label titleLabel = new Label("Assistant Intelligent de Bien-être");
        titleLabel.getStyleClass().addAll("title-main", "text-center");

        // Bouton d'analyse
        analyzeButton = new Button("Analyser mon état émotionnel");
        analyzeButton.getStyleClass().addAll("button", "button-primary", "pulse-animation");
        analyzeButton.setPrefWidth(250);

        // Zone d'analyse générale
        Label analysisLabel = new Label("Analyse de votre bien-être :");
        analysisLabel.getStyleClass().addAll("title-secondary", "margin-top-20");

        aiAnalysisArea = new TextArea();
        aiAnalysisArea.setPrefRowCount(4);
        aiAnalysisArea.setEditable(false);
        aiAnalysisArea.setText("Cliquez sur 'Analyser' pour obtenir une évaluation de votre état émotionnel basée sur vos données.");

        // Tendance détectée
        Label trendTitle = new Label("Tendance émotionnelle détectée :");
        trendTitle.getStyleClass().addAll("title-secondary", "text-bold");

        trendLabel = new Label("Aucune analyse effectuée");
        trendLabel.getStyleClass().add("info-box");

        // Recommandations personnalisées
        Label recommendationsTitle = new Label("Recommandations personnalisées :");
        recommendationsTitle.getStyleClass().addAll("title-secondary", "text-bold");

        recommendationsArea = new TextArea();
        recommendationsArea.setPrefRowCount(6);
        recommendationsArea.setEditable(false);
        recommendationsArea.setPromptText("Les recommandations apparaîtront ici après l'analyse...");

        // Prédiction
        Label predictionTitle = new Label("Prédiction d'humeur :");
        predictionTitle.getStyleClass().addAll("title-secondary", "text-bold");

        predictionLabel = new Label("Prédiction disponible après analyse");
        predictionLabel.getStyleClass().add("prediction-box");

        // Note d'information
        Label infoLabel = new Label("⚠️ L'IA analyse vos données pour vous donner des conseils. Ces suggestions ne remplacent pas un avis médical professionnel.");
        infoLabel.getStyleClass().add("info-box");
        infoLabel.setWrapText(true);

        // Listeners
        analyzeButton.setOnAction(e -> performAIAnalysis());

        content.getChildren().addAll(titleLabel, analyzeButton, analysisLabel, aiAnalysisArea,
                trendTitle, trendLabel, recommendationsTitle, recommendationsArea,
                predictionTitle, predictionLabel, infoLabel);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("scroll-pane");
        tab.setContent(scrollPane);
        return tab;
    }

    // Méthodes utilitaires pour l'interface

    private void createMoodEmojis() {
        String[] emojis = {"😢", "😟", "😐", "😊", "😄"};
        String[] descriptions = {"Très triste", "Triste", "Neutre", "Heureux", "Très heureux"};

        for (int i = 0; i < emojis.length; i++) {
            VBox emojiBox = new VBox(5);
            emojiBox.setAlignment(Pos.CENTER);
            emojiBox.getStyleClass().add("mood-emoji-container");

            Label emoji = new Label(emojis[i]);
            emoji.setFont(Font.font(30));

            Label desc = new Label(descriptions[i]);
            desc.setFont(Font.font(10));

            emojiBox.getChildren().addAll(emoji, desc);

            final int moodValue = i + 1;
            emojiBox.setOnMouseClicked(e -> {
                moodSlider.setValue(moodValue);
                updateMoodLabel(moodValue);
            });

            moodEmojis.getChildren().add(emojiBox);
        }
    }

    private void updateMoodLabel(int value) {
        String[] moodTexts = {"Très triste", "Triste", "Neutre", "Heureux", "Très heureux"};
        String[] moodClasses = {"mood-label-very-sad", "mood-label-sad", "mood-label-neutral",
                "mood-label-happy", "mood-label-very-happy"};

        moodLabel.setText(moodTexts[value - 1] + " (" + value + "/5)");

        // Réinitialiser les classes CSS
        moodLabel.getStyleClass().removeIf(styleClass -> styleClass.startsWith("mood-label-"));
        moodLabel.getStyleClass().addAll(moodClasses[value - 1], "text-center", "text-bold");
    }

    private void fillTimeComboBox() {
        for (int hour = 6; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                reminderTimeCombo.getItems().add(time);
            }
        }
        reminderTimeCombo.setValue("09:00");
    }

    private void initializeDefaultData() {
        // Initialisation des données d'exemple
        goalsList.getItems().addAll(
                "Faire du sport 3 fois par semaine",
                "Méditer 10 minutes par jour",
                "Lire 30 minutes avant de dormir"
        );

        remindersList.getItems().addAll(
                "09:00 - Pensez à faire une pause ! (Quotidien)",
                "12:00 - Temps de méditation (Quotidien)",
                "18:00 - Écrivez dans votre journal (Lundi-Vendredi)"
        );

        // Données d'exemple pour le graphique
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Humeur");
        series.getData().add(new XYChart.Data<>(1, 3));
        series.getData().add(new XYChart.Data<>(2, 4));
        series.getData().add(new XYChart.Data<>(3, 2));
        series.getData().add(new XYChart.Data<>(4, 4));
        series.getData().add(new XYChart.Data<>(5, 5));
        series.getData().add(new XYChart.Data<>(6, 3));
        series.getData().add(new XYChart.Data<>(7, 4));
        moodChart.getData().add(series);

        // Initialisation des dates pour le journal
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            journalDatePicker.getItems().add(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

    // Méthodes d'événements (à implémenter avec le backend)

    private void saveMoodData() {
        // TODO: Implémenter la sauvegarde de l'humeur
        int mood = (int) moodSlider.getValue();
        String note = dailyNote.getText();
        String date = LocalDate.now().toString();

        showAlert("Succès", "Humeur enregistrée avec succès !", Alert.AlertType.INFORMATION);
    }

    private void saveJournalEntry() {
        // TODO: Implémenter la sauvegarde du journal
        String content = journalTextArea.getText();
        String date = LocalDate.now().toString();

        showAlert("Succès", "Entrée de journal sauvegardée !", Alert.AlertType.INFORMATION);
    }

    private void loadJournalEntry() {
        // TODO: Implémenter le chargement du journal
        String selectedDate = journalDatePicker.getValue();
        if (selectedDate != null) {
            journalTextArea.setText("Contenu du journal pour le " + selectedDate);
        }
    }

    private void addGoal() {
        String goal = newGoalField.getText().trim();
        if (!goal.isEmpty()) {
            goalsList.getItems().add(goal);
            newGoalField.clear();
        }
    }

    private void deleteGoal() {
        String selected = goalsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            goalsList.getItems().remove(selected);
        }
    }

    private void markGoalComplete() {
        // TODO: Implémenter la logique de progression des objectifs
        goalProgressBar.setProgress(goalProgressBar.getProgress() + 0.1);
        progressLabel.setText((int)(goalProgressBar.getProgress() * 100) + "% complété cette semaine");
    }

    private void addReminder() {
        String message = reminderTextField.getText().trim();
        String time = reminderTimeCombo.getValue();
        String frequency = reminderFrequencyCombo.getValue();

        if (!message.isEmpty() && time != null && frequency != null) {
            String reminder = time + " - " + message + " (" + frequency + ")";
            remindersList.getItems().add(reminder);
            reminderTextField.clear();
        }
    }

    private void deleteReminder() {
        String selected = remindersList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            remindersList.getItems().remove(selected);
        }
    }
    private void refreshStatistics() {
        String period = periodComboBox.getValue();

        // Données fictives pour l'exemple
        String averageMood = "4.3";
        String frequentMood = "Très heureux";
        String missedDays = "1";

        // Mise à jour du contenu des cercles
        metricsBox.getChildren().clear();
        metricsBox.getChildren().addAll(
                createCircleMetric("MOYENNE", averageMood, "#5F4630"),
                createCircleMetric("HUMEUR FRÉQUENTE", frequentMood, "#FD58CB"),
                createCircleMetric("JOURS SANS SAISIE", missedDays, "#28B0AD")
        );
    }



    private void performAIAnalysis() {
        // TODO: Implémenter l'analyse IA
        aiAnalysisArea.setText("Analyse en cours...\n\nBasé sur vos 7 derniers jours de données, votre état émotionnel général semble stable avec une tendance légèrement positive.");

        trendLabel.setText("📈 Tendance stable avec amélioration légère (+0.3 points sur 7 jours)");
        trendLabel.getStyleClass().removeAll("info-box");
        trendLabel.getStyleClass().add("trend-positive");

        recommendationsArea.setText("Recommandations basées sur votre profil :\n\n" +
                "• Continuez vos habitudes actuelles, elles semblent positives\n" +
                "• Considérez ajouter 10 minutes de méditation le matin\n" +
                "• Vos objectifs sportifs semblent avoir un impact positif\n" +
                "• Essayez d'écrire dans votre journal plus régulièrement");

        predictionLabel.setText("🔮 Prédiction : Humeur stable à légèrement positive pour les 3 prochains jours");
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStyleClass().add("alert");
        alert.showAndWait();
    }
    private VBox createCircleMetric(String title, String value, String hexColor) {
        Color color = Color.web(hexColor);

        Circle circle = new Circle(40, color);
        circle.getStyleClass().add("circle-shape");

        Label labelValue = new Label(value);
        labelValue.getStyleClass().add("circle-metric-value");

        StackPane stack = new StackPane(circle, labelValue);

        Label labelTitle = new Label(title);
        labelTitle.getStyleClass().add("circle-metric-title");

        VBox box = new VBox(labelTitle, stack);
        box.getStyleClass().add("circle-metric-box");
        return box;
    }




    public static void main(String[] args) {
        launch(args);
    }
}