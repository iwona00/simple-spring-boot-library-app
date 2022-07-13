package pl.javaspringboot.libraryapp.library.database.manager;

import pl.javaspringboot.libraryapp.library.model.Author;
import pl.javaspringboot.libraryapp.library.model.Book;
import pl.javaspringboot.libraryapp.library.repositories.AuthorRepository;
import pl.javaspringboot.libraryapp.library.repositories.BookRepository;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LibraryDatabaseManager {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public void addBookToDatabase(Book book) {
        bookRepository.save(book);
    }

    public void removeBookFromDatabase(Book book) {
        bookRepository.delete(book);
    }

    public Iterable<Book> getAllBooksFromDatabase() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseGet(() -> null);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseGet(() -> null);
    }

}
