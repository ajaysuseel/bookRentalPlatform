package bookrental.model;

public class User {
    private String name;
    private String email;
    private String phone;
    private String address;
    private int readCoins;

    public User(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.readCoins = 10; // initial coins set to 10
    }

    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public int getReadCoins() {
        return readCoins;
    }
    
    public void addReadCoins(int coins) {
        this.readCoins += coins;
    }
    
    public boolean deductReadCoins(int coins) {
        if (this.readCoins >= coins) {
            this.readCoins -= coins;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
