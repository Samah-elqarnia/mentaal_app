# AI Integration Guide for Mental Health Application

## Overview
Your MainPage.java has been enhanced with comprehensive AI analysis capabilities and JSON data export functionality. The integration provides real-time mood analysis, personalized recommendations, trend detection, and complete data export.

## 🚀 Key Features Added

### 1. AI Analysis Integration
- **Real-time mood pattern analysis** with trend detection
- **Sentiment analysis** of journal entries
- **Temporal pattern recognition** (best/worst days of week)
- **Personalized recommendations** based on user data
- **Mood prediction** with confidence scores
- **Overall wellness scoring** (0-100 scale)

### 2. Enhanced AI Tab
- **Wellness Score Display** with progress bar
- **Interactive analysis** with detailed breakdowns
- **Visual charts** for weekly mood patterns
- **Comprehensive recommendations** with priority levels
- **Predictive insights** with confidence metrics

### 3. JSON Data Export
- **Complete data export** including all user entries
- **AI analysis results** preservation
- **Metadata and statistics** for data analysis
- **User-friendly file selection** with automatic naming

## 📁 New Files Created

### Core AI Classes
- `AIWellnessService.java` - Main AI analysis service
- `MoodAnalyzer.java` - Mood pattern analysis engine
- `AIAnalysisResult.java` - Complete analysis results container
- `MoodAnalysisResult.java` - Mood-specific analysis results
- `Recommendation.java` - AI-generated recommendations
- `MoodPrediction.java` - Future mood predictions

### Data Classes  
- `UserProfile.java` - Complete user data container
- `MoodEntry.java` - Individual mood entries
- `JournalEntry.java` - Journal entry structure
- `UserDataManager.java` - Data persistence and management

### Utilities
- `JsonExporter.java` - Custom JSON export without external dependencies

## 🔧 How to Use

### 1. Running AI Analysis
1. Click **"Analyser avec IA"** button in the AI tab
2. The system will:
   - Analyze all your mood entries and journal data
   - Calculate wellness score and trends
   - Generate personalized recommendations
   - Create mood predictions
   - Display visual charts and insights

### 2. Exporting Data
1. Click **"Exporter Données JSON"** button
2. Choose save location and filename
3. The export includes:
   - All mood entries with dates and notes
   - Journal entries with sentiment analysis
   - Goals and reminders
   - Complete AI analysis results
   - User statistics and metadata

### 3. Understanding AI Insights

#### Wellness Score (0-100)
- **80-100**: Excellent mental health indicators
- **60-79**: Good emotional stability
- **40-59**: Moderate wellbeing with room for improvement
- **20-39**: Concerning patterns, consider professional support
- **0-19**: Significant concerns, seek immediate help

#### Trend Analysis
- **📈 Positive trend**: Mood improving over time
- **📊 Stable trend**: Consistent emotional state
- **📉 Negative trend**: Declining mood patterns

#### Recommendation Priorities
- **🔴 High Priority**: Immediate action recommended
- **🟡 Medium Priority**: Important but not urgent
- **🟢 Low Priority**: General wellbeing suggestions

## 🎯 AI Analysis Features

### Mood Pattern Analysis
- **Average mood calculation** across different time periods
- **Volatility detection** to identify emotional instability
- **Streak analysis** for positive/negative mood periods
- **Weekly patterns** to identify problematic days

### Journal Sentiment Analysis
- **Keyword-based sentiment detection** in French
- **Positive/negative content ratio** calculation
- **Emotional pattern identification** over time

### Predictive Analytics
- **3-day mood prediction** based on historical patterns
- **Confidence scoring** based on data quality
- **Trend extrapolation** using linear regression

### Personalized Recommendations
- **Activity suggestions** based on mood patterns
- **Mindfulness practices** for emotional stability
- **Professional support** recommendations when needed
- **Lifestyle adjustments** for improved wellbeing

## 🔄 Data Flow

1. **User inputs** mood/journal data through the interface
2. **Data storage** in UserDataManager with automatic statistics
3. **AI analysis** triggered on-demand or automatically
4. **Results display** in enhanced UI with visual feedback
5. **Export capability** for external analysis or backup

## 📊 Sample Data

The system includes sample data for demonstration:
- **21 days of mood entries** with realistic patterns
- **3 journal entries** with varying sentiment
- **Default goals and reminders** for context
- **Realistic mood patterns** including weekend improvements

## 🎨 UI Enhancements

### New Visual Elements
- **Progress bars** for wellness scores
- **Color-coded trends** (green=improving, red=declining)
- **Priority icons** for recommendations
- **Interactive charts** for weekly patterns
- **Status indicators** for analysis progress

### Improved User Experience
- **Real-time feedback** during analysis
- **Clear status messages** and error handling
- **Intuitive button layouts** with icons
- **Responsive design** that adapts to content

## 🚀 Next Steps

To further enhance the system, consider:

1. **Database Integration**: Replace in-memory storage with SQLite/PostgreSQL
2. **Advanced ML**: Implement machine learning models for better predictions
3. **Social Features**: Add anonymous comparison with community averages
4. **Mobile Sync**: Export data for mobile app integration
5. **Professional Reports**: Generate PDF reports for healthcare providers

## 📝 Usage Examples

### Analyzing Trends
```java
// Automatic analysis when clicking "Analyser avec IA"
UserProfile profile = dataManager.getCompleteUserData("user_id");
AIAnalysisResult result = aiService.performAnalysis(profile);
// Results automatically displayed in UI
```

### Exporting Data
```java
// Triggered by "Exporter Données JSON" button
JsonExporter.exportToJson("user_data.json", userProfile, aiAnalysis);
// Creates complete backup with all user data and AI insights
```

Your mental health application now provides comprehensive AI-powered insights that can help users understand their emotional patterns and receive personalized guidance for improved wellbeing!