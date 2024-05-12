package exercise.service;

import exercise.dto.*;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.mapper.BookMapper;
import exercise.model.Book;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    public List<BookDTO> getAll() {
        var Books = bookRepository.findAll();
        var result = Books.stream()
                .map(bookMapper::map)
                .toList();
        return result;
    }

    public ResponseEntity<BookDTO> create(BookCreateDTO bookData) {
        if (!authorRepository.existsById(bookData.getAuthorId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var book = bookMapper.map(bookData);
        bookRepository.save(book);
        var bookDTO = bookMapper.map(book);
        return new ResponseEntity<>(bookDTO , HttpStatus.CREATED);
    }
    public BookDTO findById(Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        var author = book.getAuthor();
        var bookDTO = bookMapper.map(book);
        bookDTO.setAuthorFirstName(author.getFirstName());
        bookDTO.setAuthorLastName(author.getLastName());
        return bookDTO;
    }

    public ResponseEntity<BookDTO> update(BookUpdateDTO bookData, Long id) {
        if (bookData.getAuthorId().isPresent() && !authorRepository.existsById(bookData.getAuthorId().get())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        bookMapper.update(bookData, book);
        bookRepository.save(book);
        var bookDTO = bookMapper.map(book);
        return new ResponseEntity<>(bookDTO, HttpStatus.OK);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
    // END
}
