package com.example.mental_health;

import java.time.LocalDate;

/**
 * Represents a single mood entry from a user
 */
public class MoodEntry {
    private LocalDate date;
    private int moodLevel; // 1-5 scale
    private String note;
    
    public MoodEntry() {}
    
    public MoodEntry(LocalDate date, int moodLevel, String note) {
        this.date = date;
        this.moodLevel = Math.max(1, Math.min(5, moodLevel)); // Clamp to 1-5
        this.note = note;
    }
    
    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public int getMoodLevel() { return moodLevel; }
    public void setMoodLevel(int moodLevel) { 
        this.moodLevel = Math.max(1, Math.min(5, moodLevel)); 
    }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    @Override
    public String toString() {
        return String.format("MoodEntry{date=%s, mood=%d, note='%s'}", 
                           date, moodLevel, note != null ? note.substring(0, Math.min(20, note.length())) + "..." : "");
    }
}