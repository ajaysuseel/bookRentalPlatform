package bookrental.service;

import bookrental.model.Book;
import bookrental.model.User;
import java.util.ArrayList;
import java.util.List;

public class BookRentalPlatform {
    private List<User> users;
    private List<Book> books;

    public BookRentalPlatform() {
        users = new ArrayList<>();
        books = new ArrayList<>();
    }

    public void registerUser(User user) {
        users.add(user);
    }
    
    public void addBook(Book book) {
        books.add(book);
    }
    
    public boolean rentBook(User user, Book book, int rentalCost) {
        if(book.isRented()) {
            return false;
        }
        if(user.deductReadCoins(rentalCost)) {
            book.setRented(true);
            return true;
        }
        return false;
    }
    
    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for(Book book : books) {
            if(!book.isRented()) {
                available.add(book);
            }
        }
        return available;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public List<Book> getBooks() {
        return books;
    }
}
