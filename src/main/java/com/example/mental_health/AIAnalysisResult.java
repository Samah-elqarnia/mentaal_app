package com.example.mental_health;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Complete AI analysis result containing all insights and recommendations
 */
public class AIAnalysisResult {
    private MoodAnalysisResult moodAnalysis;
    private List<Recommendation> recommendations;
    private MoodPrediction prediction;
    private LocalDateTime analysisTimestamp;
    private String summary;
    private double overallWellnessScore; // 0-100
    
    public AIAnalysisResult() {
        this.analysisTimestamp = LocalDateTime.now();
    }
    
    public AIAnalysisResult(MoodAnalysisResult moodAnalysis, List<Recommendation> recommendations, MoodPrediction prediction) {
        this();
        this.moodAnalysis = moodAnalysis;
        this.recommendations = recommendations;
        this.prediction = prediction;
    }
    
    // Getters and setters
    public MoodAnalysisResult getMoodAnalysis() { return moodAnalysis; }
    public void setMoodAnalysis(MoodAnalysisResult moodAnalysis) { this.moodAnalysis = moodAnalysis; }
    
    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }
    
    public MoodPrediction getPrediction() { return prediction; }
    public void setPrediction(MoodPrediction prediction) { this.prediction = prediction; }
    
    public LocalDateTime getAnalysisTimestamp() { return analysisTimestamp; }
    public void setAnalysisTimestamp(LocalDateTime analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }
    
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public double getOverallWellnessScore() { return overallWellnessScore; }
    public void setOverallWellnessScore(double overallWellnessScore) { 
        this.overallWellnessScore = Math.max(0, Math.min(100, overallWellnessScore)); 
    }
    
    @Override
    public String toString() {
        return String.format("AIAnalysisResult{timestamp=%s, wellnessScore=%.1f, recommendations=%d}", 
                           analysisTimestamp, overallWellnessScore, recommendations != null ? recommendations.size() : 0);
    }
}