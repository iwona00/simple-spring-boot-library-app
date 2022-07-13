package pl.javaspringboot.libraryapp.library.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.javaspringboot.libraryapp.library.database.manager.LibraryDatabaseManager;
import pl.javaspringboot.libraryapp.library.model.Author;
import pl.javaspringboot.libraryapp.library.model.Book;

import java.net.URI;
import java.util.Optional;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BookRestController {

   private LibraryDatabaseManager libraryDatabaseManager;

    @GetMapping("/findAllBooksInLibrary")
    public ResponseEntity<Iterable<Book>> findAllBooksInLibrary() {
        return ResponseEntity.ok(libraryDatabaseManager.getAllBooksFromDatabase());
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryDatabaseManager.getBookById(id));
    }

    @PostMapping(path = "/addNewBook")
    public ResponseEntity addNewBook(@RequestBody Book book) {

        Optional<Long> authorId = Optional.ofNullable(book.getAuthor().getId());

        // if author id exist check if author exist in database and then if it is true add book to property author's book set if not save new book with new author
        if(authorId.isPresent()) {
            Optional<Author> optionalAuthor = isAuthorExistInDatabase(authorId.get());
            if(optionalAuthor.isPresent()){
                Author author = optionalAuthor.get();

                // create Bidirectional relationship
                author.addBook(book);
                book.setAuthor(author);
            }
            else{
                Author newAuthor = createNewAuthor(book.getAuthor().getName(), book.getAuthor().getSurname());
                book.setAuthor(newAuthor);
            }
        }

        libraryDatabaseManager.addBookToDatabase(book);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/getBookById")
                .path("/{id}")
                .buildAndExpand(book.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity updateBookById(@RequestBody Book book, @PathVariable Long id) {

        Optional<Book> bookOptional = Optional.ofNullable(libraryDatabaseManager.getBookById(id));
        if (bookOptional.isPresent()) {
            Book updatedBook = bookOptional.get();
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setTitle(book.getTitle());
            libraryDatabaseManager.addBookToDatabase(updatedBook);
        } else {
            libraryDatabaseManager.addBookToDatabase(book);
        }

        return ResponseEntity.ok("The book has been successfully saved");
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        libraryDatabaseManager.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    private Optional<Author> isAuthorExistInDatabase(Long authorId) {
        return Optional.ofNullable(libraryDatabaseManager.getAuthorById(authorId));
    }

    private Author createNewAuthor(String name, String surname) {
        return new Author(name, surname);
    }

}
