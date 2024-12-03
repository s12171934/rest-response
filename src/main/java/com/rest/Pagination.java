package com.rest;

public record Pagination(
    int currentPage,
    int totalPages,
    int totalItems,
    int itemsPerPage,
    boolean isEnd
) {

  public Pagination(int currentPage, int itemsPerPage, int totalItems) {

    this(
        currentPage,
        (totalItems - 1) / itemsPerPage + 1,
        totalItems,
        itemsPerPage,
        currentPage >= (totalItems - 1) / itemsPerPage + 1
    );
  }
}
