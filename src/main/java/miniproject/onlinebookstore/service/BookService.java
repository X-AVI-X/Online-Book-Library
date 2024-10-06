package miniproject.onlinebookstore.service;

import miniproject.onlinebookstore.dto.ActionResponse;
import miniproject.onlinebookstore.dto.BookDto;
import miniproject.onlinebookstore.entity.Book;
import miniproject.onlinebookstore.entity.BookStatus;
import miniproject.onlinebookstore.exception.IdNotFoundException;
import miniproject.onlinebookstore.repository.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    public final BookRepository repository;
    public final ModelMapper modelMapper;

    public BookService(BookRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public BookDto createBook (BookDto bookDto){
        Book book = modelMapper.map(bookDto, Book.class);
        book.setStatus(BookStatus.AVAILABLE);
        return modelMapper.map(
                repository.save(book), BookDto.class
        );
    }

    public List<Book> getAll (){
        return repository.findAll();
    }
    public ActionResponse delete (Long id){
        if (repository.existsById(id)){
            repository.delete(repository.findById(id).get());
            return new ActionResponse("Book Deleted.");
        }
        else return new ActionResponse("Not found. Unable to Delete.");
    }

    public BookDto update (BookDto editedBook) throws Exception {
        if (repository.existsById(editedBook.getId())){
            return modelMapper.map(
                    repository.save(
                            modelMapper.map(editedBook, Book.class)
                    ),
                    BookDto.class
            );
        }
        else throw new IdNotFoundException("Book not found by given id. Failed to update!");
    }

}
