package com.example.mental_health;

import java.util.List;

/**
 * AI prediction for future mood trends
 */
public class MoodPrediction {
    private double predictedMood;
    private double confidence; // 0.0 to 1.0
    private String timeframe;
    private List<String> factors;
    private String trend; // "improving", "stable", "declining"
    
    public MoodPrediction() {}
    
    public MoodPrediction(double predictedMood, double confidence, String timeframe, String trend) {
        this.predictedMood = predictedMood;
        this.confidence = Math.max(0.0, Math.min(1.0, confidence));
        this.timeframe = timeframe;
        this.trend = trend;
    }
    
    // Getters and setters
    public double getPredictedMood() { return predictedMood; }
    public void setPredictedMood(double predictedMood) { this.predictedMood = predictedMood; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { 
        this.confidence = Math.max(0.0, Math.min(1.0, confidence)); 
    }
    
    public String getTimeframe() { return timeframe; }
    public void setTimeframe(String timeframe) { this.timeframe = timeframe; }
    
    public List<String> getFactors() { return factors; }
    public void setFactors(List<String> factors) { this.factors = factors; }
    
    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }
    
    @Override
    public String toString() {
        return String.format("MoodPrediction{mood=%.1f, confidence=%.0f%%, trend=%s}", 
                           predictedMood, confidence * 100, trend);
    }
}