package com.example.mental_health;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Manages user data storage and retrieval
 */
public class UserDataManager {
    
    // In-memory storage for demo purposes
    // In a real application, this would connect to a database
    private Map<String, UserProfile> userProfiles;
    
    public UserDataManager() {
        this.userProfiles = new HashMap<>();
        initializeWithSampleData();
    }
    
    /**
     * Gets complete user data for a given user ID
     */
    public UserProfile getCompleteUserData(String userId) {
        UserProfile profile = userProfiles.get(userId);
        if (profile == null) {
            profile = new UserProfile(userId);
            userProfiles.put(userId, profile);
        }
        
        // Calculate dynamic statistics
        updateUserStatistics(profile);
        
        return profile;
    }
    
    /**
     * Saves a mood entry for a user
     */
    public void saveMoodEntry(String userId, MoodEntry moodEntry) {
        UserProfile profile = getCompleteUserData(userId);
        
        // Remove existing entry for the same date
        profile.getMoodEntries().removeIf(entry -> entry.getDate().equals(moodEntry.getDate()));
        
        // Add new entry
        profile.getMoodEntries().add(moodEntry);
        
        // Sort by date (most recent first)
        profile.getMoodEntries().sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        updateUserStatistics(profile);
    }
    
    /**
     * Saves a journal entry for a user
     */
    public void saveJournalEntry(String userId, JournalEntry journalEntry) {
        UserProfile profile = getCompleteUserData(userId);
        
        // Remove existing entry for the same date
        profile.getJournalEntries().removeIf(entry -> entry.getDate().equals(journalEntry.getDate()));
        
        // Add new entry
        profile.getJournalEntries().add(journalEntry);
        
        // Sort by date (most recent first)
        profile.getJournalEntries().sort((a, b) -> b.getDate().compareTo(a.getDate()));
    }
    
    /**
     * Adds a goal for a user
     */
    public void addGoal(String userId, String goal) {
        UserProfile profile = getCompleteUserData(userId);
        if (!profile.getGoals().contains(goal)) {
            profile.getGoals().add(goal);
        }
    }
    
    /**
     * Removes a goal for a user
     */
    public void removeGoal(String userId, String goal) {
        UserProfile profile = getCompleteUserData(userId);
        profile.getGoals().remove(goal);
    }
    
    /**
     * Adds a reminder for a user
     */
    public void addReminder(String userId, String reminder) {
        UserProfile profile = getCompleteUserData(userId);
        if (!profile.getReminders().contains(reminder)) {
            profile.getReminders().add(reminder);
        }
    }
    
    /**
     * Gets mood entries for a specific date range
     */
    public List<MoodEntry> getMoodEntriesInRange(String userId, LocalDate startDate, LocalDate endDate) {
        UserProfile profile = getCompleteUserData(userId);
        
        return profile.getMoodEntries().stream()
                .filter(entry -> !entry.getDate().isBefore(startDate) && !entry.getDate().isAfter(endDate))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .toList();
    }
    
    /**
     * Gets journal entries for a specific date range
     */
    public List<JournalEntry> getJournalEntriesInRange(String userId, LocalDate startDate, LocalDate endDate) {
        UserProfile profile = getCompleteUserData(userId);
        
        return profile.getJournalEntries().stream()
                .filter(entry -> !entry.getDate().isBefore(startDate) && !entry.getDate().isAfter(endDate))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .toList();
    }
    
    // Private helper methods
    
    private void updateUserStatistics(UserProfile profile) {
        List<MoodEntry> moodEntries = profile.getMoodEntries();
        
        if (moodEntries.isEmpty()) {
            profile.setAverageMoodLast7Days(3.0);
            profile.setAverageMoodLast30Days(3.0);
            profile.setTotalEntriesCount(0);
            profile.setStreakDays(0);
            return;
        }
        
        LocalDate now = LocalDate.now();
        
        // Calculate 7-day average
        double avg7Days = moodEntries.stream()
                .filter(entry -> ChronoUnit.DAYS.between(entry.getDate(), now) <= 7)
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .orElse(3.0);
        profile.setAverageMoodLast7Days(avg7Days);
        
        // Calculate 30-day average
        double avg30Days = moodEntries.stream()
                .filter(entry -> ChronoUnit.DAYS.between(entry.getDate(), now) <= 30)
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .orElse(3.0);
        profile.setAverageMoodLast30Days(avg30Days);
        
        // Set total entries count
        profile.setTotalEntriesCount(moodEntries.size());
        
        // Calculate current streak
        profile.setStreakDays(calculateCurrentStreak(moodEntries));
    }
    
    private int calculateCurrentStreak(List<MoodEntry> moodEntries) {
        if (moodEntries.isEmpty()) return 0;
        
        // Sort by date (most recent first)
        List<MoodEntry> sortedEntries = new ArrayList<>(moodEntries);
        sortedEntries.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        LocalDate today = LocalDate.now();
        int streak = 0;
        
        for (MoodEntry entry : sortedEntries) {
            long daysDiff = ChronoUnit.DAYS.between(entry.getDate(), today);
            
            if (daysDiff == streak) {
                streak++;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    /**
     * Initialize with sample data for demonstration
     */
    private void initializeWithSampleData() {
        String demoUserId = "main_user";
        UserProfile demoProfile = new UserProfile(demoUserId);
        
        // Add sample mood entries for the last 30 days
        LocalDate today = LocalDate.now();
        Random random = new Random(42); // Fixed seed for consistent demo data
        
        String[] sampleNotes = {
            "Journée productive au travail",
            "Un peu stressé mais ça va",
            "Excellente soirée entre amis",
            "Journée calme et reposante",
            "Défi intéressant au bureau",
            "Week-end relaxant en famille",
            "Bonne séance de sport",
            "Lecture d'un livre passionnant",
            "Réunion stressante mais réussie",
            "Promenade agréable dans le parc",
            "Cuisine avec des amis",
            "Journée de télétravail efficace",
            "Concert fantastique hier soir",
            "Apprentissage d'une nouvelle compétence",
            "Temps pluvieux mais cosy à la maison"
        };
        
        for (int i = 0; i < 21; i++) {
            LocalDate date = today.minusDays(i);
            
            // Generate mood with some pattern (slightly better on weekends)
            int baseMood = 3;
            if (date.getDayOfWeek().getValue() >= 6) { // Weekend
                baseMood = 4;
            }
            
            int mood = Math.max(1, Math.min(5, baseMood + random.nextInt(3) - 1));
            String note = sampleNotes[random.nextInt(sampleNotes.length)];
            
            demoProfile.getMoodEntries().add(new MoodEntry(date, mood, note));
        }
        
        // Add sample journal entries
        demoProfile.getJournalEntries().add(new JournalEntry(
            today.minusDays(1),
            "Cette semaine a été particulièrement enrichissante. Le projet au travail avance bien et l'équipe est motivée. J'ai aussi eu l'occasion de passer du temps de qualité avec ma famille ce week-end."
        ));
        
        demoProfile.getJournalEntries().add(new JournalEntry(
            today.minusDays(5),
            "Journée un peu difficile aujourd'hui. Le stress du travail commence à se faire sentir, mais je sais que c'est temporaire. J'ai prévu une séance de méditation ce soir pour me recentrer."
        ));
        
        demoProfile.getJournalEntries().add(new JournalEntry(
            today.minusDays(10),
            "Excellente découverte aujourd'hui ! J'ai commencé un nouveau livre sur la psychologie positive. Les concepts sont fascinants et je pense qu'ils pourraient vraiment m'aider dans ma vie quotidienne."
        ));
        
        // Add sample goals
        demoProfile.getGoals().addAll(Arrays.asList(
            "Faire du sport 3 fois par semaine",
            "Méditer 10 minutes chaque matin",
            "Lire 30 minutes avant de dormir",
            "Écrire dans mon journal 3 fois par semaine"
        ));
        
        // Add sample reminders
        demoProfile.getReminders().addAll(Arrays.asList(
            "09:00 - Pensez à faire une pause ! (Quotidien)",
            "12:00 - Temps de méditation (Quotidien)",
            "18:00 - Écrivez dans votre journal (Lundi-Vendredi)"
        ));
        
        userProfiles.put(demoUserId, demoProfile);
        System.out.println("Sample data initialized for user: " + demoUserId);
    }
}