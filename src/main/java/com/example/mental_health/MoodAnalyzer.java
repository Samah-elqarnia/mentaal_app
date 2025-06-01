package com.example.mental_health;

import java.util.*;
import java.time.DayOfWeek;

/**
 * Analyzes mood patterns and trends
 */
public class MoodAnalyzer {
    
    /**
     * Analyzes mood patterns from a list of mood entries
     */
    public MoodAnalysisResult analyzeMoodPatterns(List<MoodEntry> moodEntries) {
        if (moodEntries.isEmpty()) {
            return new MoodAnalysisResult(3.0, 0.0, 0.0); // Default neutral result
        }
        
        // Calculate basic statistics
        double averageMood = calculateAverageMood(moodEntries);
        double trendSlope = calculateTrendSlope(moodEntries);
        double volatilityScore = calculateVolatilityScore(moodEntries);
        int moodVariance = calculateMoodVariance(moodEntries);
        
        // Analyze weekly patterns
        Map<String, Double> weeklyPatterns = analyzeWeeklyPatterns(moodEntries);
        
        // Analyze streaks
        StreakAnalysis streakAnalysis = analyzeStreaks(moodEntries);
        
        // Create result
        MoodAnalysisResult result = new MoodAnalysisResult(averageMood, trendSlope, volatilityScore);
        result.setMoodVariance(moodVariance);
        result.setWeeklyPatterns(weeklyPatterns);
        result.setStreakAnalysis(streakAnalysis);
        
        return result;
    }
    
    private double calculateAverageMood(List<MoodEntry> moodEntries) {
        return moodEntries.stream()
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .orElse(3.0);
    }
    
    private double calculateTrendSlope(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 2) return 0.0;
        
        // Sort by date
        List<MoodEntry> sortedMoods = new ArrayList<>(moodEntries);
        sortedMoods.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        
        // Linear regression to find trend
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = sortedMoods.size();
        
        for (int i = 0; i < n; i++) {
            double x = i; // Day index
            double y = sortedMoods.get(i).getMoodLevel();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }
        
        // Calculate slope: (n*sumXY - sumX*sumY) / (n*sumX2 - sumX*sumX)
        double denominator = n * sumX2 - sumX * sumX;
        if (denominator == 0) return 0.0;
        
        return (n * sumXY - sumX * sumY) / denominator;
    }
    
    private double calculateVolatilityScore(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 2) return 0.0;
        
        double averageMood = calculateAverageMood(moodEntries);
        
        // Calculate standard deviation
        double sumSquaredDiffs = moodEntries.stream()
                .mapToDouble(entry -> Math.pow(entry.getMoodLevel() - averageMood, 2))
                .sum();
        
        double standardDeviation = Math.sqrt(sumSquaredDiffs / moodEntries.size());
        
        // Convert to 0-100 scale (max possible std dev for 1-5 scale is 2)
        return Math.min(100, (standardDeviation / 2.0) * 100);
    }
    
    private int calculateMoodVariance(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 2) return 0;
        
        // Count number of mood changes
        List<MoodEntry> sortedMoods = new ArrayList<>(moodEntries);
        sortedMoods.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        
        int changes = 0;
        for (int i = 1; i < sortedMoods.size(); i++) {
            if (sortedMoods.get(i).getMoodLevel() != sortedMoods.get(i-1).getMoodLevel()) {
                changes++;
            }
        }
        
        return changes;
    }
    
    private Map<String, Double> analyzeWeeklyPatterns(List<MoodEntry> moodEntries) {
        Map<DayOfWeek, List<Integer>> dayMoods = new HashMap<>();
        
        // Group moods by day of week
        for (MoodEntry entry : moodEntries) {
            DayOfWeek dayOfWeek = entry.getDate().getDayOfWeek();
            dayMoods.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(entry.getMoodLevel());
        }
        
        // Calculate averages for each day
        Map<String, Double> weeklyPatterns = new HashMap<>();
        for (Map.Entry<DayOfWeek, List<Integer>> entry : dayMoods.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(3.0);
            weeklyPatterns.put(getDayName(entry.getKey()), average);
        }
        
        return weeklyPatterns;
    }
    
    private StreakAnalysis analyzeStreaks(List<MoodEntry> moodEntries) {
        if (moodEntries.isEmpty()) {
            return new StreakAnalysis(0, 0, 0, "neutral");
        }
        
        // Sort by date (most recent first)
        List<MoodEntry> sortedMoods = new ArrayList<>(moodEntries);
        sortedMoods.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        int maxPositiveStreak = 0;
        int maxNegativeStreak = 0;
        int currentStreak = 0;
        String currentStreakType = "neutral";
        
        int tempPositiveStreak = 0;
        int tempNegativeStreak = 0;
        
        for (MoodEntry entry : sortedMoods) {
            int mood = entry.getMoodLevel();
            
            if (mood >= 4) { // Positive mood
                tempPositiveStreak++;
                tempNegativeStreak = 0;
                maxPositiveStreak = Math.max(maxPositiveStreak, tempPositiveStreak);
            } else if (mood <= 2) { // Negative mood
                tempNegativeStreak++;
                tempPositiveStreak = 0;
                maxNegativeStreak = Math.max(maxNegativeStreak, tempNegativeStreak);
            } else { // Neutral mood
                tempPositiveStreak = 0;
                tempNegativeStreak = 0;
            }
        }
        
        // Determine current streak (from most recent entries)
        if (!sortedMoods.isEmpty()) {
            int recentMood = sortedMoods.get(0).getMoodLevel();
            currentStreak = 1;
            
            if (recentMood >= 4) {
                currentStreakType = "positive";
                for (int i = 1; i < sortedMoods.size() && sortedMoods.get(i).getMoodLevel() >= 4; i++) {
                    currentStreak++;
                }
            } else if (recentMood <= 2) {
                currentStreakType = "negative";
                for (int i = 1; i < sortedMoods.size() && sortedMoods.get(i).getMoodLevel() <= 2; i++) {
                    currentStreak++;
                }
            } else {
                currentStreakType = "neutral";
                for (int i = 1; i < sortedMoods.size() && sortedMoods.get(i).getMoodLevel() == 3; i++) {
                    currentStreak++;
                }
            }
        }
        
        return new StreakAnalysis(maxPositiveStreak, maxNegativeStreak, currentStreak, currentStreakType);
    }
    
    private String getDayName(DayOfWeek day) {
        switch (day) {
            case MONDAY: return "Lundi";
            case TUESDAY: return "Mardi";
            case WEDNESDAY: return "Mercredi";
            case THURSDAY: return "Jeudi";
            case FRIDAY: return "Vendredi";
            case SATURDAY: return "Samedi";
            case SUNDAY: return "Dimanche";
            default: return day.toString();
        }
    }
}