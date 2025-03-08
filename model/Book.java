package bookrental.model;

public class Book {
    private String name;
    private String author;
    private String summary;
    private int readCoinsGenerated;
    private boolean isRented;

    public Book(String name, String author, String summary) {
        this.name = name;
        this.author = author;
        this.summary = summary;
        this.readCoinsGenerated = 5; // default coins generated when listed
        this.isRented = false;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getSummary() {
        return summary;
    }
    
    public int getReadCoinsGenerated() {
        return readCoinsGenerated;
    }
    
    public boolean isRented() {
        return isRented;
    }
    
    public void setRented(boolean rented) {
        isRented = rented;
    }
    
    @Override
    public String toString() {
        return name + " by " + author + (isRented ? " (Rented)" : " (Available)");
    }
}
