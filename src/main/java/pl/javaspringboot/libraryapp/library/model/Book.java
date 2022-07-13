package pl.javaspringboot.libraryapp.library.model;

import java.util.Objects;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Setter
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="author_id")
    @Getter
    @Setter
    private Author author;

    public Book(String title, Author author) {
        this.title  = title;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
