package pl.javaspringboot.libraryapp.library.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Author {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonProperty("id")
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String surname;

    @OneToMany(mappedBy = "author", orphanRemoval = true, cascade=CascadeType.PERSIST)
    @Getter
    private Set<Book> books = new HashSet<>();

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void addBook(Book book){
        books.add(book);
    }
}
