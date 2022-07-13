package pl.javaspringboot.libraryapp.library.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.javaspringboot.libraryapp.library.model.Book;

import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
}
