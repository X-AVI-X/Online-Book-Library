package miniproject.onlinebookstore.service;

import miniproject.onlinebookstore.dto.HistoryResponse;
import miniproject.onlinebookstore.dto.UserDto;
import miniproject.onlinebookstore.entity.Role;
import miniproject.onlinebookstore.entity.User;
import miniproject.onlinebookstore.entity.UserHistory;
import miniproject.onlinebookstore.exception.CustomException;
import miniproject.onlinebookstore.exception.IdNotFoundException;
import miniproject.onlinebookstore.repository.UserHistoryRepository;
import miniproject.onlinebookstore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserHistoryRepository userHistoryRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, UserHistoryRepository userHistoryRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userHistoryRepository = userHistoryRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto createUser (User user) throws CustomException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser;
        try {
            createdUser = repository.save(user);
        }catch (DataIntegrityViolationException ex){
            createdUser=null;
            throw new CustomException("Email is already associated. Try with different one.");
        }
        return modelMapper.map(createdUser, UserDto.class);
    }

    public UserDto getUser (Long userId) throws IdNotFoundException {
        return modelMapper.map(
                repository.findById(userId).orElseThrow(()
                        -> new IdNotFoundException("User not found with given ID"))
                , UserDto.class
        );
    }


    public List<HistoryResponse> getPreviouslyBorrowedBooksByUser(Long userId) {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        for (UserHistory userHistory : userHistoryRepository.findPreviouslyBorrowedByUserId(userId)){
            HistoryResponse historyResponse = modelMapper.map(userHistory, HistoryResponse.class);
            historyResponse.setUserDto(modelMapper.map(userHistory.getUser(), UserDto.class));
            historyResponses.add(historyResponse);
        }
        return historyResponses;
    }

    public List<HistoryResponse> getCurrentlyBorrowedBooksByUser(Long userId) {
        List<HistoryResponse> historyResponses = new ArrayList<>();
        for (UserHistory userHistory : userHistoryRepository.findCurrentlyBorrowedByUserId(userId)){
            HistoryResponse historyResponse = modelMapper.map(userHistory, HistoryResponse.class);
            historyResponse.setUserDto(modelMapper.map(userHistory.getUser(), UserDto.class));
            historyResponses.add(historyResponse);
        }
        return historyResponses;
    }
}
