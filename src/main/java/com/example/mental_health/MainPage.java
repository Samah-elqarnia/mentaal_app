package com.example.mental_health;

import javafx.application.Application;
import java.net.URL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
    private AIWellnessService aiService;
    private AIAnalysisResult lastAnalysisResult;
    private Button analyzeButton;
    private Button exportDataButton;
    private Label trendLabel;
    private TextArea recommendationsArea;
    private Label predictionLabel;
    private Label wellnessScoreLabel;
    private UserDataManager dataManager;
    private ProgressBar wellnessProgressBar;
    private HBox metricsBox;
    private VBox aiChartsContainer;
    private BarChart<String, Number> weeklyPatternsChart;
    private Label sentimentAnalysisLabel;
    private Label temporalPatternsLabel;
    private String currentUserId;

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

        // Initialize AI services
        initializeAIServices();

        // Initialisation des données par défaut
        initializeDefaultData();
    }

    // AI Services initialization
    private void initializeAIServices() {
        try {
            // Try to initialize AI services
            aiService = new AIWellnessService();
            dataManager = new UserDataManager();
            currentUserId = "main_user";

            System.out.println("AI services initialized successfully");
        } catch (Exception e) {
            System.err.println("AI services not available: " + e.getMessage());
            // Initialize with null - we'll handle this in performAIAnalysis()
            aiService = null;
            dataManager = null;
            currentUserId = "main_user";
        }
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
            HBox.setMargin(photoView, new Insets(0, 5, 0, 0));
        }

        // Création du conteneur HBox
        HBox container = new HBox();
        container.getChildren().addAll(photoView, titleLabel);
        container.setAlignment(Pos.CENTER_LEFT);

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
        addReminderLabel.getStyleClass().addAll("title-secondary", "margin-top-10");

        HBox reminderForm = new HBox();
        reminderForm.setSpacing(10);
        reminderForm.setAlignment(Pos.CENTER_LEFT);

        Label messageLabel = new Label("Message :");
        messageLabel.getStyleClass().add("text-bold");

        reminderTextField = new TextField();
        reminderTextField.setPrefWidth(200);
        reminderTextField.setPromptText("Ex: Pensez à faire une pause !");

        Label timeLabel = new Label("Heure :");
        timeLabel.getStyleClass().add("text-bold");

        reminderTimeCombo = new ComboBox<>();
        fillTimeComboBox();

        Label frequencyLabel = new Label("Fréquence :");
        frequencyLabel.getStyleClass().add("text-bold");

        reminderFrequencyCombo = new ComboBox<>();
        reminderFrequencyCombo.getItems().addAll("Quotidien", "Lundi-Vendredi", "Week-end", "Personnalisé");
        reminderFrequencyCombo.setValue("Quotidien");

        addReminderButton = new Button("Ajouter");
        addReminderButton.getStyleClass().addAll("button", "button-success");

        // Ajouter tous les éléments à la HBox
        reminderForm.getChildren().addAll(
                messageLabel, reminderTextField,
                timeLabel, reminderTimeCombo,
                frequencyLabel, reminderFrequencyCombo, addReminderButton);
        reminderForm.getStyleClass().add("info-box");

        // Liste des rappels
        Label remindersListLabel = new Label("Rappels configurés :");
        remindersListLabel.getStyleClass().addAll("title-secondary", "margin-top-20");

        remindersList = new ListView<>();
        remindersList.setPrefHeight(200);

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

        // Buttons row
        HBox buttonsRow = new HBox(10);
        buttonsRow.setAlignment(Pos.CENTER);

        analyzeButton = new Button("Analyser avec IA");
        analyzeButton.getStyleClass().addAll("button", "button-primary", "pulse-animation");
        analyzeButton.setPrefWidth(200);

        exportDataButton = new Button("Exporter Données JSON");
        exportDataButton.getStyleClass().addAll("button", "button-secondary");
        exportDataButton.setPrefWidth(200);

        buttonsRow.getChildren().addAll(analyzeButton, exportDataButton);

        // Wellness Score Section
        VBox wellnessSection = new VBox(10);
        wellnessSection.getStyleClass().add("stats-box");

        Label wellnessTitle = new Label("Score de Bien-être Global");
        wellnessTitle.getStyleClass().addAll("title-secondary", "text-bold");

        wellnessProgressBar = new ProgressBar(0);
        wellnessProgressBar.setPrefWidth(300);
        wellnessProgressBar.getStyleClass().add("wellness-progress");

        wellnessScoreLabel = new Label("Non calculé");
        wellnessScoreLabel.getStyleClass().addAll("text-bold", "text-center");

        wellnessSection.getChildren().addAll(wellnessTitle, wellnessProgressBar, wellnessScoreLabel);

        // Tendance détectée
        Label trendTitle = new Label("Tendance émotionnelle détectée :");
        trendTitle.getStyleClass().addAll("title-secondary", "text-bold");

        trendLabel = new Label("Aucune analyse effectuée");
        trendLabel.getStyleClass().add("info-box");

        // Sentiment Analysis
        Label sentimentTitle = new Label("Analyse des sentiments (Journal) :");
        sentimentTitle.getStyleClass().addAll("title-secondary", "text-bold");

        sentimentAnalysisLabel = new Label("Aucune donnée de journal disponible");
        sentimentAnalysisLabel.getStyleClass().add("info-box");

        // Temporal Patterns
        Label temporalTitle = new Label("Patterns temporels :");
        temporalTitle.getStyleClass().addAll("title-secondary", "text-bold");

        temporalPatternsLabel = new Label("Analyse temporelle en attente");
        temporalPatternsLabel.getStyleClass().add("info-box");

        // Charts container
        aiChartsContainer = new VBox(15);
        aiChartsContainer.getStyleClass().add("charts-container");

        // Weekly patterns chart
        CategoryAxis weeklyXAxis = new CategoryAxis();
        weeklyXAxis.setLabel("Jour de la semaine");
        NumberAxis weeklyYAxis = new NumberAxis(1, 5, 1);
        weeklyYAxis.setLabel("Humeur moyenne");

        weeklyPatternsChart = new BarChart<>(weeklyXAxis, weeklyYAxis);
        weeklyPatternsChart.setTitle("Patterns Hebdomadaires");
        weeklyPatternsChart.setPrefHeight(250);
        weeklyPatternsChart.setLegendVisible(false);

        aiChartsContainer.getChildren().add(weeklyPatternsChart);

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

        // Listeners
        analyzeButton.setOnAction(e -> performAIAnalysis());
        exportDataButton.setOnAction(e -> exportUserDataToJSON());

        content.getChildren().addAll(titleLabel, buttonsRow, wellnessSection,
                trendTitle, trendLabel, sentimentTitle, sentimentAnalysisLabel,
                temporalTitle, temporalPatternsLabel, aiChartsContainer,
                recommendationsTitle, recommendationsArea,
                predictionTitle, predictionLabel);

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
                StringBuilder sb = new StringBuilder();
                if (hour < 10) sb.append("0");
                sb.append(hour).append(":");
                if (minute < 10) sb.append("0");
                sb.append(minute);

                reminderTimeCombo.getItems().add(sb.toString());
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

    // Basic event methods
    private void saveMoodData() {
        try {
            int mood = (int) moodSlider.getValue();
            String note = dailyNote.getText();
            LocalDate date = LocalDate.now();

            // If dataManager is available, save to it
            if (dataManager != null) {
                try {
                    MoodEntry moodEntry = new MoodEntry(date, mood, note);
                    dataManager.saveMoodEntry(currentUserId, moodEntry);
                    refreshStatistics();
                } catch (Exception e) {
                    System.err.println("Could not save to dataManager: " + e.getMessage());
                }
            }

            showAlert("Succès", "Humeur enregistrée avec succès !", Alert.AlertType.INFORMATION);

            // Clear form
            dailyNote.clear();
            moodSlider.setValue(3);
            updateMoodLabel(3);

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la sauvegarde: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void saveJournalEntry() {
        try {
            String content = journalTextArea.getText().trim();
            if (content.isEmpty()) {
                showAlert("Attention", "Veuillez saisir du contenu avant de sauvegarder.", Alert.AlertType.WARNING);
                return;
            }

            LocalDate date = LocalDate.now();

            // If dataManager is available, save to it
            if (dataManager != null) {
                try {
                    JournalEntry journalEntry = new JournalEntry(date, content);
                    dataManager.saveJournalEntry(currentUserId, journalEntry);
                } catch (Exception e) {
                    System.err.println("Could not save to dataManager: " + e.getMessage());
                }
            }

            showAlert("Succès", "Entrée de journal sauvegardée !", Alert.AlertType.INFORMATION);

            // Clear form
            journalTextArea.clear();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la sauvegarde: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void performAIAnalysis() {
        try {
            analyzeButton.setDisable(true);
            analyzeButton.setText("Analyse en cours...");

            // Check if AI services are available
            if (dataManager == null || aiService == null) {
                performSimplifiedAnalysis();
                return;
            }

            // Get complete user data
            UserProfile userProfile = dataManager.getCompleteUserData(currentUserId);

            if (userProfile == null || userProfile.getMoodEntries().isEmpty()) {
                showAlert("Attention", "Aucune donnée utilisateur disponible pour l'analyse. Ajoutez d'abord quelques entrées d'humeur.", Alert.AlertType.WARNING);
                return;
            }

            // Perform comprehensive AI analysis
            lastAnalysisResult = aiService.performAnalysis(userProfile);

            // Update wellness score
            double wellnessScore = lastAnalysisResult.getOverallWellnessScore();
            wellnessProgressBar.setProgress(wellnessScore / 100.0);
            wellnessScoreLabel.setText(String.format("%.1f/100", wellnessScore));

            // Update trend analysis
            String trendIcon = getTrendIcon(lastAnalysisResult.getMoodAnalysis().getTrendSlope());
            String trendText = String.format("%s Tendance: %.3f (volatilité: %.1f)",
                    trendIcon,
                    lastAnalysisResult.getMoodAnalysis().getTrendSlope(),
                    lastAnalysisResult.getMoodAnalysis().getVolatilityScore());
            trendLabel.setText(trendText);
            updateTrendStyle(lastAnalysisResult.getMoodAnalysis().getTrendSlope());

            // Update sentiment analysis
            String sentimentAnalysis = aiService.analyzeJournalSentiment(userProfile.getJournalEntries());
            sentimentAnalysisLabel.setText(sentimentAnalysis);

            // Update temporal patterns
            String temporalAnalysis = aiService.analyzeTemporalPatterns(userProfile.getMoodEntries());
            temporalPatternsLabel.setText(temporalAnalysis);

            // Update weekly patterns chart
            updateWeeklyPatternsChart(lastAnalysisResult.getMoodAnalysis());

            // Update recommendations
            StringBuilder recommendationsText = new StringBuilder();
            recommendationsText.append("Recommandations personnalisées basées sur l'IA:\\n\\n");

            if (lastAnalysisResult.getRecommendations() != null) {
                for (Recommendation rec : lastAnalysisResult.getRecommendations()) {
                    String priorityIcon = getPriorityIcon(rec.getPriority());
                    recommendationsText.append(String.format("%s [%s] %s\\n   %s\\n\\n",
                            priorityIcon,
                            rec.getType().toUpperCase(),
                            rec.getTitle(),
                            rec.getDescription()));
                }
            } else {
                recommendationsText.append("• Continuez vos habitudes actuelles\\n");
                recommendationsText.append("• Maintenez une routine régulière\\n");
                recommendationsText.append("• Consultez un professionnel si nécessaire");
            }
            recommendationsArea.setText(recommendationsText.toString());

            // Update prediction
            if (lastAnalysisResult.getPrediction() != null) {
                MoodPrediction prediction = lastAnalysisResult.getPrediction();
                String predictionIcon = getPredictionIcon(prediction.getTrend());
                predictionLabel.setText(String.format("%s Prédiction: %.1f/5 (confiance: %.0f%%) - %s",
                        predictionIcon,
                        prediction.getPredictedMood(),
                        prediction.getConfidence() * 100,
                        prediction.getTimeframe()));
            } else {
                predictionLabel.setText("🔮 Prédiction: Données insuffisantes pour une prédiction fiable");
            }

            showAlert("Succès", "Analyse IA terminée avec succès!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'analyse IA: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            // Fallback to simplified analysis
            performSimplifiedAnalysis();
        } finally {
            analyzeButton.setDisable(false);
            analyzeButton.setText("Analyser avec IA");
        }
    }

    /**
     * Simplified analysis when AI services are not available
     */
    private void performSimplifiedAnalysis() {
        try {
            // Simulate AI analysis with static data
            wellnessProgressBar.setProgress(0.75);
            wellnessScoreLabel.setText("75.0/100");

            trendLabel.setText("📈 Tendance stable avec amélioration légère (+0.3 points sur 7 jours)");
            trendLabel.getStyleClass().removeAll("info-box");
            trendLabel.getStyleClass().add("trend-positive");

            sentimentAnalysisLabel.setText("Analyse des sentiments: Globalement positif basé sur les entrées récentes");
            temporalPatternsLabel.setText("Patterns temporels: Meilleur jour Samedi, jour difficile Lundi");

            recommendationsArea.setText("Recommandations basées sur votre profil :\\n\\n" +
                    "🟡 [MINDFULNESS] Méditation quotidienne\\n" +
                    "   Considérez ajouter 10 minutes de méditation le matin\\n\\n" +
                    "🟢 [ACTIVITY] Activité physique\\n" +
                    "   Vos objectifs sportifs semblent avoir un impact positif\\n\\n" +
                    "🟢 [LIFESTYLE] Journal personnel\\n" +
                    "   Essayez d'écrire dans votre journal plus régulièrement");

            predictionLabel.setText("🔮 Prédiction : Humeur stable à légèrement positive pour les 3 prochains jours");

            // Update weekly patterns chart with sample data
            updateWeeklyPatternsChartSimplified();

            showAlert("Info", "Analyse simplifiée effectuée (services IA non disponibles)", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'analyse simplifiée: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void exportUserDataToJSON() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter les données utilisateur");
            fileChooser.setInitialFileName("donnees_bien_etre_" + LocalDate.now().toString() + ".json");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                exportDataButton.setDisable(true);
                exportDataButton.setText("Export en cours...");

                if (dataManager != null) {
                    // Full export with AI services
                    UserProfile userProfile = dataManager.getCompleteUserData(currentUserId);
                    JsonExporter.exportToJson(file.getAbsolutePath(), userProfile, lastAnalysisResult);
                } else {
                    // Simplified export without AI services
                    exportSimplifiedData(file.getAbsolutePath());
                }

                showAlert("Succès",
                        String.format("Données exportées avec succès vers:\\n%s",
                                file.getAbsolutePath()),
                        Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'export: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        } finally {
            exportDataButton.setDisable(false);
            exportDataButton.setText("Exporter Données JSON");
        }
    }
    private void exportSimplifiedData(String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"metadata\": {\n");
            json.append("    \"exportTimestamp\": \"").append(LocalDateTime.now().toString()).append("\",\n");
            json.append("    \"version\": \"1.0\",\n");
            json.append("    \"source\": \"Mental Health Assistant (Simplified)\",\n");
            json.append("    \"note\": \"Export without AI services\"\n");
            json.append("  },\n");
            json.append("  \"ui_data\": {\n");
            json.append("    \"current_mood\": ").append((int)moodSlider.getValue()).append(",\n");
            json.append("    \"current_note\": \"").append(escapeJson(dailyNote.getText())).append("\",\n");
            json.append("    \"journal_content\": \"").append(escapeJson(journalTextArea.getText())).append("\",\n");
            json.append("    \"goals\": [\n");

            for (int i = 0; i < goalsList.getItems().size(); i++) {
                json.append("      \"").append(escapeJson(goalsList.getItems().get(i))).append("\"");
                if (i < goalsList.getItems().size() - 1) json.append(",");
                json.append("\n");
            }

            json.append("    ],\n");
            json.append("    \"reminders\": [\n");

            for (int i = 0; i < remindersList.getItems().size(); i++) {
                json.append("      \"").append(escapeJson(remindersList.getItems().get(i))).append("\"");
                if (i < remindersList.getItems().size() - 1) json.append(",");
                json.append("\n");
            }

            json.append("    ]\n");
            json.append("  }\n");
            json.append("}\n");

            writer.write(json.toString());
        }
    }

    // Helper method for escaping JSON strings
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }




    // Other event methods
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

    private void loadJournalEntry() {
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
        goalProgressBar.setProgress(goalProgressBar.getProgress() + 0.1);
        progressLabel.setText((int) (goalProgressBar.getProgress() * 100) + "% complété cette semaine");
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStyleClass().add("alert");
        alert.showAndWait();
    }

    // Helper methods for AI analysis visualization (only used if AI services available)
    private String getTrendIcon(double trendSlope) {
        if (trendSlope > 0.1) return "📈";
        else if (trendSlope < -0.1) return "📉";
        else return "📊";
    }

    private void updateTrendStyle(double trendSlope) {
        trendLabel.getStyleClass().removeIf(styleClass ->
                styleClass.equals("trend-positive") ||
                        styleClass.equals("trend-negative") ||
                        styleClass.equals("info-box"));

        if (trendSlope > 0.1) {
            trendLabel.getStyleClass().add("trend-positive");
        } else if (trendSlope < -0.1) {
            trendLabel.getStyleClass().add("trend-negative");
        } else {
            trendLabel.getStyleClass().add("info-box");
        }
    }

    private String getPriorityIcon(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return "🔴";
            case "medium": return "🟡";
            case "low": return "🟢";
            default: return "⚪";
        }
    }

    private String getPredictionIcon(String trend) {
        switch (trend.toLowerCase()) {
            case "improving": return "📈";
            case "declining": return "📉";
            case "stable": return "📊";
            default: return "🔮";
        }
    }

    private void updateWeeklyPatternsChart(MoodAnalysisResult moodAnalysis) {
        weeklyPatternsChart.getData().clear();

        if (moodAnalysis.getWeeklyPatterns() != null) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Humeur moyenne");

            String[] daysOrder = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

            for (String day : daysOrder) {
                Double avgMood = moodAnalysis.getWeeklyPatterns().get(day);
                if (avgMood != null) {
                    series.getData().add(new XYChart.Data<>(day, avgMood));
                }
            }

            weeklyPatternsChart.getData().add(series);
        }
    }

    private void updateWeeklyPatternsChartSimplified() {
        try {
            weeklyPatternsChart.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Humeur moyenne");

            // Sample data for weekly patterns
            series.getData().add(new XYChart.Data<>("Lundi", 3.2));
            series.getData().add(new XYChart.Data<>("Mardi", 3.5));
            series.getData().add(new XYChart.Data<>("Mercredi", 3.8));
            series.getData().add(new XYChart.Data<>("Jeudi", 4.0));
            series.getData().add(new XYChart.Data<>("Vendredi", 4.2));
            series.getData().add(new XYChart.Data<>("Samedi", 4.5));
            series.getData().add(new XYChart.Data<>("Dimanche", 4.1));

            weeklyPatternsChart.getData().add(series);
        } catch (Exception e) {
            System.err.println("Error updating weekly patterns chart: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
