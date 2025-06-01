package com.example.mental_health;

import java.time.LocalDate;

/**
 * Represents a journal entry from a user
 */
public class JournalEntry {
    private LocalDate date;
    private String content;
    private double sentimentScore; // -1.0 to 1.0
    
    public JournalEntry() {}
    
    public JournalEntry(LocalDate date, String content) {
        this.date = date;
        this.content = content;
        this.sentimentScore = 0.0; // Neutral by default
    }
    
    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public double getSentimentScore() { return sentimentScore; }
    public void setSentimentScore(double sentimentScore) { 
        this.sentimentScore = Math.max(-1.0, Math.min(1.0, sentimentScore)); 
    }
    
    @Override
    public String toString() {
        return String.format("JournalEntry{date=%s, length=%d, sentiment=%.2f}", 
                           date, content != null ? content.length() : 0, sentimentScore);
    }
}