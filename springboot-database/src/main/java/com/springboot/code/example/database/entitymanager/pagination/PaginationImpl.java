package com.springboot.code.example.database.entitymanager.pagination;

import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationImpl<T> implements Pagination<T> {

  private final List<T> elements = new ArrayList<>();
  private int page;
  private int size;
  private long totalElements;

  public PaginationImpl(@NonNull List<T> elements) {

    this(elements, 0, elements.size(), elements.size());
  }

  public PaginationImpl(@NonNull List<T> elements, int page, int size, long totalElements) {

    if (page < 0) {

      throw new PaginationException("page");
    }

    if (size < 0) {

      throw new PaginationException("size");
    }

    if (totalElements < 0) {

      throw new PaginationException("totalElements");
    }

    this.elements.addAll(elements);
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
  }

  @Override
  public List<T> getElements() {

    return this.elements;
  }

  @Override
  public long getTotalPages() {

    return Pagination.calculateTotalPages(this.getTotalElements(), this.getSize());
  }

  @Override
  public long getTotalElements() {

    return this.totalElements;
  }

  @Override
  public int getNumberOfElements() {

    return this.getElements().size();
  }

  @Override
  public int getPage() {

    return this.page;
  }

  @Override
  public int getSize() {

    return this.size;
  }

  @Override
  @JsonProperty("isFirst")
  public boolean isFirst() {

    return this.getPage() == 0;
  }

  @Override
  @JsonProperty("isLast")
  public boolean isLast() {

    return this.getPage() + 1 == this.getTotalPages();
  }

  @Override
  @JsonProperty("hasNext")
  public boolean hasNext() {

    return this.getPage() + 1 < this.getTotalPages();
  }

  @Override
  @JsonProperty("hasPrevious")
  public boolean hasPrevious() {

    return this.getPage() - 1 < this.getTotalPages()
        && this.getPage() > 0;
  }

}
