package com.example.mental_health;

/**
 * AI-generated recommendation for user wellbeing
 */
public class Recommendation {
    private String id;
    private String type; // "activity", "mindfulness", "social", "professional", "lifestyle"
    private String title;
    private String description;
    private String priority; // "low", "medium", "high"
    private String category;
    private boolean actionRequired;
    
    public Recommendation() {}
    
    public Recommendation(String type, String title, String description, String priority) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.actionRequired = "high".equals(priority);
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public boolean isActionRequired() { return actionRequired; }
    public void setActionRequired(boolean actionRequired) { this.actionRequired = actionRequired; }
    
    @Override
    public String toString() {
        return String.format("Recommendation{type='%s', priority='%s', title='%s'}", 
                           type, priority, title);
    }
}