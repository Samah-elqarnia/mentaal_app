package com.example.mental_health;

import java.time.LocalDate;

/**
 * Demonstration of AI integration capabilities
 * This class shows how the AI analysis works programmatically
 */
public class AIIntegrationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Mental Health AI Integration Demo ===\n");
        
        try {
            // Initialize services
            AIWellnessService aiService = new AIWellnessService();
            UserDataManager dataManager = new UserDataManager();
            String userId = "demo_user";
            
            System.out.println("1. RETRIEVING USER DATA");
            System.out.println("========================");
            UserProfile userProfile = dataManager.getCompleteUserData(userId);
            
            System.out.printf("User: %s%n", userProfile.getUserId());
            System.out.printf("Mood entries: %d%n", userProfile.getMoodEntries().size());
            System.out.printf("Journal entries: %d%n", userProfile.getJournalEntries().size());
            System.out.printf("Goals: %d%n", userProfile.getGoals().size());
            System.out.printf("7-day average: %.1f/5%n", userProfile.getAverageMoodLast7Days());
            System.out.printf("30-day average: %.1f/5%n", userProfile.getAverageMoodLast30Days());
            System.out.println();
            
            System.out.println("2. AI ANALYSIS EXECUTION");
            System.out.println("=========================");
            AIAnalysisResult analysis = aiService.performAnalysis(userProfile);
            
            System.out.printf("Overall wellness score: %.1f/100%n", analysis.getOverallWellnessScore());
            System.out.printf("Analysis timestamp: %s%n", analysis.getAnalysisTimestamp());
            System.out.printf("Summary: %s%n", analysis.getSummary());
            System.out.println();
            
            if (analysis.getMoodAnalysis() != null) {
                System.out.println("3. MOOD ANALYSIS DETAILS");
                System.out.println("=========================");
                MoodAnalysisResult moodAnalysis = analysis.getMoodAnalysis();
                System.out.printf("Average mood: %.2f/5%n", moodAnalysis.getAverageMood());
                System.out.printf("Trend slope: %.4f%n", moodAnalysis.getTrendSlope());
                System.out.printf("Volatility score: %.1f/100%n", moodAnalysis.getVolatilityScore());
                System.out.printf("Mood variance: %d changes%n", moodAnalysis.getMoodVariance());
                
                if (moodAnalysis.getWeeklyPatterns() != null) {
                    System.out.println("\\nWeekly patterns:");
                    moodAnalysis.getWeeklyPatterns().forEach((day, avg) -> 
                        System.out.printf("  %s: %.2f/5%n", day, avg));
                }
                
                if (moodAnalysis.getStreakAnalysis() != null) {
                    System.out.println("\\nStreak analysis:");
                    var streaks = moodAnalysis.getStreakAnalysis();
                    System.out.printf("  Max positive streak: %d days%n", streaks.getMaxPositiveStreak());
                    System.out.printf("  Max negative streak: %d days%n", streaks.getMaxNegativeStreak());
                    System.out.printf("  Current streak: %d days (%s)%n", 
                        streaks.getCurrentStreak(), streaks.getStreakType());
                }
                System.out.println();
            }
            
            System.out.println("4. AI RECOMMENDATIONS");
            System.out.println("======================");
            if (analysis.getRecommendations() != null && !analysis.getRecommendations().isEmpty()) {
                for (Recommendation rec : analysis.getRecommendations()) {
                    String priority = getPriorityIcon(rec.getPriority());
                    System.out.printf("%s [%s] %s%n", priority, rec.getType().toUpperCase(), rec.getTitle());
                    System.out.printf("   %s%n", rec.getDescription());
                    System.out.println();
                }
            } else {
                System.out.println("No specific recommendations generated.");
            }
            
            System.out.println("5. MOOD PREDICTION");
            System.out.println("==================");
            if (analysis.getPrediction() != null) {
                MoodPrediction prediction = analysis.getPrediction();
                System.out.printf("Predicted mood: %.1f/5%n", prediction.getPredictedMood());
                System.out.printf("Confidence: %.0f%%%n", prediction.getConfidence() * 100);
                System.out.printf("Timeframe: %s%n", prediction.getTimeframe());
                System.out.printf("Trend: %s%n", prediction.getTrend());
            } else {
                System.out.println("Prediction not available.");
            }
            System.out.println();
            
            System.out.println("6. ADDITIONAL AI INSIGHTS");
            System.out.println("==========================");
            
            // Trend analysis
            String trendAnalysis = aiService.generateTrendAnalysis(userProfile.getRecentMoodEntries(14));
            System.out.println("Trend analysis:");
            System.out.println("  " + trendAnalysis);
            System.out.println();
            
            // Sentiment analysis
            String sentimentAnalysis = aiService.analyzeJournalSentiment(userProfile.getJournalEntries());
            System.out.println("Journal sentiment:");
            System.out.println("  " + sentimentAnalysis);
            System.out.println();
            
            // Temporal patterns
            String temporalAnalysis = aiService.analyzeTemporalPatterns(userProfile.getMoodEntries());
            System.out.println("Temporal patterns:");
            System.out.println("  " + temporalAnalysis);
            System.out.println();
            
            System.out.println("7. DATA EXPORT SIMULATION");
            System.out.println("==========================");
            String exportPath = "demo_export_" + LocalDate.now() + ".json";
            JsonExporter.exportToJson(exportPath, userProfile, analysis);
            System.out.printf("Data exported to: %s%n", exportPath);
            System.out.printf("Export includes: %d mood entries, %d journal entries, AI analysis%n", 
                userProfile.getMoodEntries().size(), userProfile.getJournalEntries().size());
            
            System.out.println();
            System.out.println("=== Demo completed successfully! ===");
            System.out.println("\\nHow to integrate with MainPage.java:");
            System.out.println("1. Click 'Analyser avec IA' button to trigger performAIAnalysis()");
            System.out.println("2. AI results will be displayed in the enhanced UI");
            System.out.println("3. Click 'Exporter Données JSON' to save all data");
            System.out.println("4. View wellness score, trends, and recommendations");
            
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String getPriorityIcon(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return "🔴";
            case "medium": return "🟡";
            case "low": return "🟢";
            default: return "⚪";
        }
    }
}