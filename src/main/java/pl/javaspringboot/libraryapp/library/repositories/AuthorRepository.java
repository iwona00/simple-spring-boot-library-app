package pl.javaspringboot.libraryapp.library.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.javaspringboot.libraryapp.library.model.Author;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
