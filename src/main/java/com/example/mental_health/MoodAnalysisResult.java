package com.example.mental_health;

import java.util.Map;

/**
 * Result of mood pattern analysis
 */
public class MoodAnalysisResult {
    private double averageMood;
    private double trendSlope;
    private double volatilityScore;
    private int moodVariance;
    private Map<String, Double> weeklyPatterns;
    private StreakAnalysis streakAnalysis;
    private Map<String, Double> correlationFactors;
    
    public MoodAnalysisResult() {}
    
    public MoodAnalysisResult(double averageMood, double trendSlope, double volatilityScore) {
        this.averageMood = averageMood;
        this.trendSlope = trendSlope;
        this.volatilityScore = volatilityScore;
    }
    
    // Getters and setters
    public double getAverageMood() { return averageMood; }
    public void setAverageMood(double averageMood) { this.averageMood = averageMood; }
    
    public double getTrendSlope() { return trendSlope; }
    public void setTrendSlope(double trendSlope) { this.trendSlope = trendSlope; }
    
    public double getVolatilityScore() { return volatilityScore; }
    public void setVolatilityScore(double volatilityScore) { this.volatilityScore = volatilityScore; }
    
    public int getMoodVariance() { return moodVariance; }
    public void setMoodVariance(int moodVariance) { this.moodVariance = moodVariance; }
    
    public Map<String, Double> getWeeklyPatterns() { return weeklyPatterns; }
    public void setWeeklyPatterns(Map<String, Double> weeklyPatterns) { this.weeklyPatterns = weeklyPatterns; }
    
    public StreakAnalysis getStreakAnalysis() { return streakAnalysis; }
    public void setStreakAnalysis(StreakAnalysis streakAnalysis) { this.streakAnalysis = streakAnalysis; }
    
    public Map<String, Double> getCorrelationFactors() { return correlationFactors; }
    public void setCorrelationFactors(Map<String, Double> correlationFactors) { this.correlationFactors = correlationFactors; }
}

/**
 * Analysis of mood streaks
 */
class StreakAnalysis {
    private int maxPositiveStreak;
    private int maxNegativeStreak;
    private int currentStreak;
    private String streakType; // "positive", "negative", "neutral"
    
    public StreakAnalysis() {}
    
    public StreakAnalysis(int maxPositiveStreak, int maxNegativeStreak, int currentStreak, String streakType) {
        this.maxPositiveStreak = maxPositiveStreak;
        this.maxNegativeStreak = maxNegativeStreak;
        this.currentStreak = currentStreak;
        this.streakType = streakType;
    }
    
    // Getters and setters
    public int getMaxPositiveStreak() { return maxPositiveStreak; }
    public void setMaxPositiveStreak(int maxPositiveStreak) { this.maxPositiveStreak = maxPositiveStreak; }
    
    public int getMaxNegativeStreak() { return maxNegativeStreak; }
    public void setMaxNegativeStreak(int maxNegativeStreak) { this.maxNegativeStreak = maxNegativeStreak; }
    
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    
    public String getStreakType() { return streakType; }
    public void setStreakType(String streakType) { this.streakType = streakType; }
}