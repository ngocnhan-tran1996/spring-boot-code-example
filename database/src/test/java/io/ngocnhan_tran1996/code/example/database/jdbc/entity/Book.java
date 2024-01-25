package io.ngocnhan_tran1996.code.example.database.jdbc.entity;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table
public class Book {

  @Id
  Integer id;
  String title;
  Set<AuthorRef> authors = new HashSet<>();

  public void addAuthor(Author author) {
    authors.add(createAuthorRef(author));
  }

  private AuthorRef createAuthorRef(Author author) {

    var authorRef = new AuthorRef();
    authorRef.author = author.id;
    return authorRef;
  }

}