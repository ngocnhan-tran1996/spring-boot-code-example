package io.ngocnhan_tran1996.code.example.database.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.database.jdbc.entity.Author;
import io.ngocnhan_tran1996.code.example.database.jdbc.entity.AuthorRepository;
import io.ngocnhan_tran1996.code.example.database.jdbc.entity.Book;
import io.ngocnhan_tran1996.code.example.database.jdbc.entity.BookRepository;

@ActiveProfiles("postgres")
@SpringBootTest
class BookAndAuthorsTests {

  @Autowired
  BookRepository books;

  @Autowired
  AuthorRepository authors;

  @Test
  @Transactional
  void createAuthorsAndBooks() {

    var tolkien = new Author();
    tolkien.setName("J.R.R. Tolkien");

    tolkien = authors.save(tolkien);

    var lordOfTheRings = new Book();
    lordOfTheRings.setTitle("Lord of the Rings");
    lordOfTheRings.addAuthor(tolkien);

    var simarillion = new Book();
    simarillion.setTitle("Simarillion");
    simarillion.addAuthor(tolkien);

    books.saveAll(List.of(lordOfTheRings, simarillion));

    assertThat(books.count()).isEqualTo(2);
    assertThat(authors.count()).isEqualTo(1);

    books.delete(simarillion);

    assertThat(books.count()).isEqualTo(1);
    assertThat(authors.count()).isEqualTo(1);


    Iterable<Book> allBooks = books.findAll();
    assertThat(allBooks)
        .extracting(Book::getTitle)
        .containsExactly("Lord of the Rings");
  }

}