package com.example.mental_health;

import java.util.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;

/**
 * AI-powered wellness analysis service
 */
public class AIWellnessService {
    private MoodAnalyzer moodAnalyzer;

    public AIWellnessService() {
        this.moodAnalyzer = new MoodAnalyzer();
    }

    /**
     * Performs comprehensive AI analysis of user data
     */
    public AIAnalysisResult performAnalysis(UserProfile userProfile) {
        if (userProfile == null || userProfile.getMoodEntries().isEmpty()) {
            throw new IllegalArgumentException("User profile with mood data required for analysis");
        }

        // Analyze mood patterns
        MoodAnalysisResult moodAnalysis = moodAnalyzer.analyzeMoodPatterns(userProfile.getMoodEntries());

        // Generate recommendations
        List<Recommendation> recommendations = generateRecommendations(userProfile, moodAnalysis);

        // Generate mood prediction - Updated method call
        MoodPrediction prediction = createMoodPrediction(userProfile.getMoodEntries());

        // Calculate overall wellness score
        double wellnessScore = calculateOverallWellnessScore(userProfile, moodAnalysis);

        // Create analysis result
        AIAnalysisResult result = new AIAnalysisResult(moodAnalysis, recommendations, prediction);
        result.setOverallWellnessScore(wellnessScore);
        result.setSummary(generateAnalysisSummary(userProfile, moodAnalysis, wellnessScore));

        return result;
    }

    /**
     * Generates trend analysis text
     */
    public String generateTrendAnalysis(List<MoodEntry> recentMoods) {
        if (recentMoods.isEmpty()) {
            return "Aucune donnée d'humeur disponible pour l'analyse de tendance.";
        }

        double averageMood = recentMoods.stream()
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .orElse(3.0);

        // Calculate trend over time
        double trend = calculateMoodTrend(recentMoods);

        String trendDescription;
        if (trend > 0.1) {
            trendDescription = "amélioration notable";
        } else if (trend < -0.1) {
            trendDescription = "déclin observable";
        } else {
            trendDescription = "stabilité";
        }

        return String.format("Sur %d jours d'analyse: Humeur moyenne %.1f/5, tendance de %s (pente: %.3f)",
                recentMoods.size(), averageMood, trendDescription, trend);
    }

    /**
     * Generates personalized recommendations
     */
    public String generatePersonalizedRecommendations(UserProfile userProfile, MoodAnalysisResult moodAnalysis) {
        StringBuilder recommendations = new StringBuilder();
        recommendations.append("Recommandations personnalisées:\\n\\n");

        double avgMood = moodAnalysis.getAverageMood();
        double volatility = moodAnalysis.getVolatilityScore();

        // Based on average mood
        if (avgMood < 2.5) {
            recommendations.append("• Envisagez de consulter un professionnel de la santé mentale\\n");
            recommendations.append("• Pratiquez des activités qui vous apportent de la joie\\n");
        } else if (avgMood < 3.5) {
            recommendations.append("• Intégrez des exercices de relaxation dans votre routine\\n");
            recommendations.append("• Connectez-vous avec vos proches\\n");
        } else {
            recommendations.append("• Maintenez vos habitudes positives actuelles\\n");
            recommendations.append("• Explorez de nouvelles activités enrichissantes\\n");
        }

        // Based on volatility
        if (volatility > 60) {
            recommendations.append("• Travaillez sur la régularité de votre routine\\n");
            recommendations.append("• Pratiquez la méditation pour stabiliser vos émotions\\n");
        }

        // Based on journal entries
        if (userProfile.getJournalEntries().isEmpty()) {
            recommendations.append("• Commencez à tenir un journal pour mieux comprendre vos émotions\\n");
        }

        return recommendations.toString();
    }

    /**
     * Generates mood prediction as formatted text
     */
    public String generateMoodPredictionText(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 7) {
            return "Prédiction: Données insuffisantes (minimum 7 jours requis)";
        }

        // Simple linear regression for prediction
        double trend = calculateMoodTrend(moodEntries);
        double currentMood = moodEntries.get(0).getMoodLevel(); // Most recent
        double predictedMood = Math.max(1, Math.min(5, currentMood + trend * 3)); // 3 days ahead

        String trendDescription = trend > 0 ? "amélioration" : trend < 0 ? "déclin" : "stabilité";
        double confidence = Math.min(0.9, 0.5 + (moodEntries.size() * 0.05)); // More data = higher confidence

        return String.format("Prédiction 3 jours: %.1f/5 (confiance: %.0f%%) - Tendance: %s",
                predictedMood, confidence * 100, trendDescription);
    }

    /**
     * Analyzes journal sentiment
     */
    public String analyzeJournalSentiment(List<JournalEntry> journalEntries) {
        if (journalEntries.isEmpty()) {
            return "Aucune entrée de journal disponible pour l'analyse de sentiment.";
        }

        // Simple sentiment analysis based on keywords
        int positiveCount = 0;
        int negativeCount = 0;
        int totalEntries = journalEntries.size();

        String[] positiveWords = {"heureux", "joie", "bien", "formidable", "excellent", "réussi", "motivation"};
        String[] negativeWords = {"triste", "stress", "difficile", "problème", "fatigue", "inquiet", "anxieux"};

        for (JournalEntry entry : journalEntries) {
            String content = entry.getContent().toLowerCase();

            for (String word : positiveWords) {
                if (content.contains(word)) {
                    positiveCount++;
                    break;
                }
            }

            for (String word : negativeWords) {
                if (content.contains(word)) {
                    negativeCount++;
                    break;
                }
            }
        }

        double positiveRatio = (double) positiveCount / totalEntries;
        double negativeRatio = (double) negativeCount / totalEntries;

        String sentiment;
        if (positiveRatio > negativeRatio + 0.2) {
            sentiment = "principalement positif";
        } else if (negativeRatio > positiveRatio + 0.2) {
            sentiment = "tendance négative";
        } else {
            sentiment = "équilibré";
        }

        return String.format("Analyse de %d entrées: Sentiment %s (%.0f%% positif, %.0f%% négatif)",
                totalEntries, sentiment, positiveRatio * 100, negativeRatio * 100);
    }

    /**
     * Analyzes temporal patterns
     */
    public String analyzeTemporalPatterns(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 7) {
            return "Données insuffisantes pour l'analyse temporelle (minimum 7 jours).";
        }

        Map<DayOfWeek, List<Integer>> dayMoods = new HashMap<>();

        for (MoodEntry entry : moodEntries) {
            DayOfWeek dayOfWeek = entry.getDate().getDayOfWeek();
            dayMoods.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(entry.getMoodLevel());
        }

        // Find best and worst days
        DayOfWeek bestDay = null;
        DayOfWeek worstDay = null;
        double bestAvg = 0;
        double worstAvg = 5;

        for (Map.Entry<DayOfWeek, List<Integer>> entry : dayMoods.entrySet()) {
            double avg = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(3.0);
            if (avg > bestAvg) {
                bestAvg = avg;
                bestDay = entry.getKey();
            }
            if (avg < worstAvg) {
                worstAvg = avg;
                worstDay = entry.getKey();
            }
        }

        return String.format("Patterns détectés: Meilleur jour %s (%.1f/5), jour difficile %s (%.1f/5)",
                bestDay != null ? getDayName(bestDay) : "N/A", bestAvg,
                worstDay != null ? getDayName(worstDay) : "N/A", worstAvg);
    }

    // Helper methods

    private List<Recommendation> generateRecommendations(UserProfile userProfile, MoodAnalysisResult moodAnalysis) {
        List<Recommendation> recommendations = new ArrayList<>();

        double avgMood = moodAnalysis.getAverageMood();
        double volatility = moodAnalysis.getVolatilityScore();

        if (avgMood < 2.5) {
            recommendations.add(new Recommendation("professional", "Consultation professionnelle",
                    "Envisagez de consulter un professionnel de la santé mentale", "high"));
        }

        if (volatility > 60) {
            recommendations.add(new Recommendation("mindfulness", "Stabilisation émotionnelle",
                    "Pratiquez la méditation quotidienne pour réduire la volatilité émotionnelle", "medium"));
        }

        if (userProfile.getJournalEntries().isEmpty()) {
            recommendations.add(new Recommendation("lifestyle", "Tenue de journal",
                    "Commencez à tenir un journal pour mieux comprendre vos patterns émotionnels", "low"));
        }

        if (avgMood >= 4.0) {
            recommendations.add(new Recommendation("activity", "Maintien des habitudes",
                    "Continuez vos habitudes actuelles qui semblent très positives", "low"));
        }

        return recommendations;
    }

    /**
     * Creates mood prediction object (renamed from generateMoodPrediction)
     */
    private MoodPrediction createMoodPrediction(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 3) {
            return new MoodPrediction(3.0, 0.3, "Données insuffisantes", "stable");
        }

        double trend = calculateMoodTrend(moodEntries);
        double currentMood = moodEntries.get(0).getMoodLevel();
        double predictedMood = Math.max(1, Math.min(5, currentMood + trend * 3));
        double confidence = Math.min(0.9, 0.5 + (moodEntries.size() * 0.02));

        String trendType = trend > 0.1 ? "improving" : trend < -0.1 ? "declining" : "stable";

        return new MoodPrediction(predictedMood, confidence, "3 prochains jours", trendType);
    }

    private double calculateOverallWellnessScore(UserProfile userProfile, MoodAnalysisResult moodAnalysis) {
        double moodScore = (moodAnalysis.getAverageMood() / 5.0) * 40; // 40% weight
        double consistencyScore = Math.max(0, (100 - moodAnalysis.getVolatilityScore()) / 100.0) * 30; // 30% weight
        double engagementScore = Math.min(1.0, userProfile.getTotalEntriesCount() / 30.0) * 20; // 20% weight
        double journalScore = Math.min(1.0, userProfile.getJournalEntries().size() / 10.0) * 10; // 10% weight

        return Math.min(100, moodScore + consistencyScore + engagementScore + journalScore);
    }

    private String generateAnalysisSummary(UserProfile userProfile, MoodAnalysisResult moodAnalysis, double wellnessScore) {
        return String.format("Analyse complète: Score de bien-être %.1f/100. " +
                        "Humeur moyenne %.1f/5 avec volatilité de %.1f. " +
                        "Basé sur %d entrées d'humeur et %d entrées de journal.",
                wellnessScore, moodAnalysis.getAverageMood(), moodAnalysis.getVolatilityScore(),
                userProfile.getMoodEntries().size(), userProfile.getJournalEntries().size());
    }

    private double calculateMoodTrend(List<MoodEntry> moodEntries) {
        if (moodEntries.size() < 2) return 0.0;

        // Simple linear regression
        List<MoodEntry> sortedMoods = new ArrayList<>(moodEntries);
        sortedMoods.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        int n = sortedMoods.size();

        for (int i = 0; i < n; i++) {
            double x = i;
            double y = sortedMoods.get(i).getMoodLevel();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
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