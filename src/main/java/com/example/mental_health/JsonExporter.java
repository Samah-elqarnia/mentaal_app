package com.example.mental_health;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Simple JSON exporter for user data (without external dependencies)
 */
public class JsonExporter {
    
    /**
     * Exports user profile and AI analysis to JSON format
     */
    public static void exportToJson(String filePath, UserProfile userProfile, AIAnalysisResult aiAnalysis) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        // Metadata
        json.append("  \"metadata\": {\n");
        json.append("    \"exportTimestamp\": \"").append(LocalDateTime.now().toString()).append("\",\n");
        json.append("    \"version\": \"1.0\",\n");
        json.append("    \"source\": \"Mental Health Assistant\",\n");
        json.append("    \"userId\": \"").append(escapeJson(userProfile.getUserId())).append("\"\n");
        json.append("  },\n");
        
        // User profile
        json.append("  \"userProfile\": {\n");
        json.append("    \"userId\": \"").append(escapeJson(userProfile.getUserId())).append("\",\n");
        json.append("    \"averageMoodLast7Days\": ").append(userProfile.getAverageMoodLast7Days()).append(",\n");
        json.append("    \"averageMoodLast30Days\": ").append(userProfile.getAverageMoodLast30Days()).append(",\n");
        json.append("    \"totalEntriesCount\": ").append(userProfile.getTotalEntriesCount()).append(",\n");
        json.append("    \"streakDays\": ").append(userProfile.getStreakDays()).append(",\n");
        
        // Mood entries
        json.append("    \"moodEntries\": [\n");
        List<MoodEntry> moodEntries = userProfile.getMoodEntries();
        for (int i = 0; i < moodEntries.size(); i++) {
            MoodEntry entry = moodEntries.get(i);
            json.append("      {\n");
            json.append("        \"date\": \"").append(entry.getDate().toString()).append("\",\n");
            json.append("        \"moodLevel\": ").append(entry.getMoodLevel()).append(",\n");
            json.append("        \"note\": \"").append(escapeJson(entry.getNote())).append("\"\n");
            json.append("      }");
            if (i < moodEntries.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("    ],\n");
        
        // Journal entries
        json.append("    \"journalEntries\": [\n");
        List<JournalEntry> journalEntries = userProfile.getJournalEntries();
        for (int i = 0; i < journalEntries.size(); i++) {
            JournalEntry entry = journalEntries.get(i);
            json.append("      {\n");
            json.append("        \"date\": \"").append(entry.getDate().toString()).append("\",\n");
            json.append("        \"content\": \"").append(escapeJson(entry.getContent())).append("\",\n");
            json.append("        \"sentimentScore\": ").append(entry.getSentimentScore()).append("\n");
            json.append("      }");
            if (i < journalEntries.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("    ],\n");
        
        // Goals
        json.append("    \"goals\": [\n");
        List<String> goals = userProfile.getGoals();
        for (int i = 0; i < goals.size(); i++) {
            json.append("      \"").append(escapeJson(goals.get(i))).append("\"");
            if (i < goals.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("    ],\n");
        
        // Reminders
        json.append("    \"reminders\": [\n");
        List<String> reminders = userProfile.getReminders();
        for (int i = 0; i < reminders.size(); i++) {
            json.append("      \"").append(escapeJson(reminders.get(i))).append("\"");
            if (i < reminders.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("    ]\n");
        json.append("  }");
        
        // AI Analysis (if available)
        if (aiAnalysis != null) {
            json.append(",\n");
            json.append("  \"aiAnalysis\": {\n");
            json.append("    \"analysisTimestamp\": \"").append(aiAnalysis.getAnalysisTimestamp().toString()).append("\",\n");
            json.append("    \"overallWellnessScore\": ").append(aiAnalysis.getOverallWellnessScore()).append(",\n");
            json.append("    \"summary\": \"").append(escapeJson(aiAnalysis.getSummary())).append("\",\n");
            
            // Mood analysis
            if (aiAnalysis.getMoodAnalysis() != null) {
                MoodAnalysisResult moodAnalysis = aiAnalysis.getMoodAnalysis();
                json.append("    \"moodAnalysis\": {\n");
                json.append("      \"averageMood\": ").append(moodAnalysis.getAverageMood()).append(",\n");
                json.append("      \"trendSlope\": ").append(moodAnalysis.getTrendSlope()).append(",\n");
                json.append("      \"volatilityScore\": ").append(moodAnalysis.getVolatilityScore()).append(",\n");
                json.append("      \"moodVariance\": ").append(moodAnalysis.getMoodVariance()).append("\n");
                json.append("    },\n");
            }
            
            // Recommendations
            json.append("    \"recommendations\": [\n");
            if (aiAnalysis.getRecommendations() != null) {
                List<Recommendation> recommendations = aiAnalysis.getRecommendations();
                for (int i = 0; i < recommendations.size(); i++) {
                    Recommendation rec = recommendations.get(i);
                    json.append("      {\n");
                    json.append("        \"type\": \"").append(escapeJson(rec.getType())).append("\",\n");
                    json.append("        \"title\": \"").append(escapeJson(rec.getTitle())).append("\",\n");
                    json.append("        \"description\": \"").append(escapeJson(rec.getDescription())).append("\",\n");
                    json.append("        \"priority\": \"").append(escapeJson(rec.getPriority())).append("\"\n");
                    json.append("      }");
                    if (i < recommendations.size() - 1) json.append(",");
                    json.append("\n");
                }
            }
            json.append("    ],\n");
            
            // Prediction
            if (aiAnalysis.getPrediction() != null) {
                MoodPrediction prediction = aiAnalysis.getPrediction();
                json.append("    \"prediction\": {\n");
                json.append("      \"predictedMood\": ").append(prediction.getPredictedMood()).append(",\n");
                json.append("      \"confidence\": ").append(prediction.getConfidence()).append(",\n");
                json.append("      \"timeframe\": \"").append(escapeJson(prediction.getTimeframe())).append("\",\n");
                json.append("      \"trend\": \"").append(escapeJson(prediction.getTrend())).append("\"\n");
                json.append("    }\n");
            }
            
            json.append("  }\n");
        }
        
        json.append("}\n");
        
        // Write to file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
    }
    
    /**
     * Escapes special characters for JSON
     */
    private static String escapeJson(String input) {
        if (input == null) return "";
        
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}