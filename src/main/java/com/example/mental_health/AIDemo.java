package com.example.mental_health;

import com.example.mental_health.*;
import com.example.mental_health.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Demonstration of AI capabilities for the Mental Health Application
 */
public class AIDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Mental Health AI Assistant Demo ===\n");
        
        try {
            // Initialize AI services
            AIWellnessService aiService = new AIWellnessService();
            UserDataManager dataManager = new UserDataManager();
            String userId = "demo_user";
            
            // Create sample user data
            createSampleUserData(dataManager, userId);
            
            // Get user profile
            UserProfile userProfile = dataManager.getCompleteUserData(userId);
            
            System.out.println("1. USER PROFILE SUMMARY");
            System.out.println("========================");
            System.out.printf("User ID: %s%n", userProfile.getUserId());
            System.out.printf("Mood entries: %d%n", userProfile.getMoodEntries().size());
            System.out.printf("Journal entries: %d%n", userProfile.getJournalEntries().size());
            System.out.printf("Goals: %d%n", userProfile.getGoals().size());
            System.out.printf("7-day average: %.1f/5%n", userProfile.getAverageMoodLast7Days());
            System.out.printf("30-day average: %.1f/5%n", userProfile.getAverageMoodLast30Days());
            System.out.println();
            
            // Perform comprehensive AI analysis
            System.out.println("2. COMPREHENSIVE AI ANALYSIS");
            System.out.println("=============================");
            AIAnalysisResult analysisResult = aiService.performAnalysis(userProfile);
            
            if (analysisResult.getMoodAnalysis() != null) {
                MoodAnalysisResult moodAnalysis = analysisResult.getMoodAnalysis();
                System.out.printf("Average mood: %.2f/5%n", moodAnalysis.getAverageMood());
                System.out.printf("Trend slope: %.3f%n", moodAnalysis.getTrendSlope());
                System.out.printf("Volatility score: %.1f/100%n", moodAnalysis.getVolatilityScore());
                System.out.printf("Mood variance: %d%n", moodAnalysis.getMoodVariance());
            }
            System.out.println();
            
            // Generate trend analysis
            System.out.println("3. TREND ANALYSIS");
            System.out.println("==================");
            List<MoodEntry> recentMoods = userProfile.getRecentMoodEntries(14);
            String trendAnalysis = aiService.generateTrendAnalysis(recentMoods);
            System.out.println(trendAnalysis);
            System.out.println();
            
            // Generate personalized recommendations
            System.out.println("4. PERSONALIZED RECOMMENDATIONS");
            System.out.println("================================");
            String recommendations = aiService.generatePersonalizedRecommendations(
                userProfile, analysisResult.getMoodAnalysis());
            System.out.println(recommendations);
            
            // Generate mood prediction
            System.out.println("5. MOOD PREDICTION");
            System.out.println("==================");
            String prediction = aiService.generateMoodPredictionText(userProfile.getMoodEntries());
            System.out.println(prediction);
            System.out.println();
            
            // Analyze journal sentiment
            System.out.println("6. JOURNAL SENTIMENT ANALYSIS");
            System.out.println("==============================");
            String sentimentAnalysis = aiService.analyzeJournalSentiment(userProfile.getJournalEntries());
            System.out.println(sentimentAnalysis);
            System.out.println();
            
            // Analyze temporal patterns
            System.out.println("7. TEMPORAL PATTERN ANALYSIS");
            System.out.println("=============================");
            String temporalAnalysis = aiService.analyzeTemporalPatterns(userProfile.getMoodEntries());
            System.out.println(temporalAnalysis);
            
            // Show detailed mood analysis
            System.out.println("8. DETAILED MOOD ANALYSIS");
            System.out.println("==========================");
            MoodAnalyzer moodAnalyzer = new MoodAnalyzer();
            MoodAnalysisResult detailedAnalysis = moodAnalyzer.analyzeMoodPatterns(userProfile.getMoodEntries());
            
            if (detailedAnalysis.getWeeklyPatterns() != null) {
                System.out.println("Weekly patterns:");
                detailedAnalysis.getWeeklyPatterns().forEach((day, avg) -> 
                    System.out.printf("  %s: %.2f/5%n", day, avg));
            }
            
            if (detailedAnalysis.getStreakAnalysis() != null) {
                StreakAnalysis streaks = detailedAnalysis.getStreakAnalysis();
                System.out.printf("Max positive streak: %d days%n", streaks.getMaxPositiveStreak());
                System.out.printf("Max negative streak: %d days%n", streaks.getMaxNegativeStreak());
            }
            
            System.out.println("\n=== Demo completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Demo failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates sample user data for demonstration
     */
    private static void createSampleUserData(UserDataManager dataManager, String userId) {
        // Create sample mood entries for the last 21 days
        LocalDate today = LocalDate.now();
        int[] sampleMoods = {
            3, 4, 2, 3, 4, 5, 4,  // Week 1
            3, 2, 4, 5, 3, 4, 3,  // Week 2
            4, 5, 4, 3, 5, 4, 2   // Week 3
        };
        
        String[] sampleNotes = {
            "Journée normale au travail",
            "Bonne présentation aujourd'hui",
            "Stress avec un projet difficile",
            "Journée calme et productive",
            "Sortie agréable entre amis",
            "Excellent week-end en famille",
            "Motivation élevée pour cette semaine",
            "Un peu fatigué mais ça va",
            "Problème technique au bureau",
            "Très bonne nouvelle personnelle",
            "Weekend très relaxant",
            "Journée d'accomplissement",
            "Réunion stressante mais réussie",
            "Journée moyenne, rien de spécial",
            "Formation intéressante",
            "Très productive et épanouie",
            "Bonne soirée avec des amis",
            "Journée tranquille à la maison",
            "Excellents résultats sur un projet",
            "Journée active et énergique",
            "Petite baisse de moral"
        };
        
        for (int i = 0; i < sampleMoods.length; i++) {
            LocalDate date = today.minusDays(sampleMoods.length - 1 - i);
            MoodEntry entry = new MoodEntry(date, sampleMoods[i], sampleNotes[i]);
            dataManager.saveMoodEntry(userId, entry);
        }
        
        // Add sample journal entries
        dataManager.saveJournalEntry(userId, new JournalEntry(
            today.minusDays(3),
            "Cette semaine a été particulièrement enrichissante. J'ai réussi à présenter mon projet devant l'équipe " +
            "et les retours ont été très positifs. Je me sens plus confiant dans mes capacités professionnelles. " +
            "Le stress que j'avais ressenti la semaine dernière s'est transformé en motivation."
        ));
        
        dataManager.saveJournalEntry(userId, new JournalEntry(
            today.minusDays(8),
            "Week-end fantastique ! Nous avons fait une grande randonnée en montagne avec la famille. " +
            "Ces moments de connexion avec la nature me ressourcent énormément. J'ai réalisé combien " +
            "il est important de prendre du temps pour soi et ses proches."
        ));
        
        dataManager.saveJournalEntry(userId, new JournalEntry(
            today.minusDays(15),
            "Période un peu difficile au travail avec ce nouveau projet. Je me sens parfois dépassé " +
            "par les exigences, mais je sais que c'est temporaire. J'essaie de me concentrer sur " +
            "les aspects positifs et d'apprendre de cette expérience."
        ));
        
        // Add sample goals
        dataManager.addGoal(userId, "Faire du sport 3 fois par semaine");
        dataManager.addGoal(userId, "Méditer 10 minutes chaque matin");
        dataManager.addGoal(userId, "Lire 30 minutes avant de dormir");
        dataManager.addGoal(userId, "Écrire dans mon journal 3 fois par semaine");
        dataManager.addGoal(userId, "Passer plus de temps avec la famille");
        
        System.out.println("Sample data created for user: " + userId);
    }
}