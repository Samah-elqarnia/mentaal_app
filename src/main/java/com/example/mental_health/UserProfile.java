package com.example.mental_health;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * Complete user profile containing all user data
 */
public class UserProfile {
    private String userId;
    private List<MoodEntry> moodEntries;
    private List<JournalEntry> journalEntries;
    private List<String> goals;
    private List<String> reminders;
    private double averageMoodLast7Days;
    private double averageMoodLast30Days;
    private int totalEntriesCount;
    private int streakDays;
    
    public UserProfile() {
        this.moodEntries = new ArrayList<>();
        this.journalEntries = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.reminders = new ArrayList<>();
    }
    
    public UserProfile(String userId) {
        this();
        this.userId = userId;
    }
    
    // Helper method to get recent mood entries
    public List<MoodEntry> getRecentMoodEntries(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return moodEntries.stream()
            .filter(entry -> entry.getDate().isAfter(cutoffDate) || entry.getDate().equals(cutoffDate))
            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
            .toList();
    }
    
    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public List<MoodEntry> getMoodEntries() { return moodEntries; }
    public void setMoodEntries(List<MoodEntry> moodEntries) { this.moodEntries = moodEntries; }
    
    public List<JournalEntry> getJournalEntries() { return journalEntries; }
    public void setJournalEntries(List<JournalEntry> journalEntries) { this.journalEntries = journalEntries; }
    
    public List<String> getGoals() { return goals; }
    public void setGoals(List<String> goals) { this.goals = goals; }
    
    public List<String> getReminders() { return reminders; }
    public void setReminders(List<String> reminders) { this.reminders = reminders; }
    
    public double getAverageMoodLast7Days() { return averageMoodLast7Days; }
    public void setAverageMoodLast7Days(double averageMoodLast7Days) { this.averageMoodLast7Days = averageMoodLast7Days; }
    
    public double getAverageMoodLast30Days() { return averageMoodLast30Days; }
    public void setAverageMoodLast30Days(double averageMoodLast30Days) { this.averageMoodLast30Days = averageMoodLast30Days; }
    
    public int getTotalEntriesCount() { return totalEntriesCount; }
    public void setTotalEntriesCount(int totalEntriesCount) { this.totalEntriesCount = totalEntriesCount; }
    
    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }
    
    @Override
    public String toString() {
        return String.format("UserProfile{userId='%s', moods=%d, journals=%d, goals=%d}", 
                           userId, moodEntries.size(), journalEntries.size(), goals.size());
    }
}