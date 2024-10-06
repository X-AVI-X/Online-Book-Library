package miniproject.onlinebookstore.controller;

import miniproject.onlinebookstore.dto.AuthRequest;
import miniproject.onlinebookstore.dto.TokenResponse;
import miniproject.onlinebookstore.entity.User;
import miniproject.onlinebookstore.exception.CustomException;
import miniproject.onlinebookstore.exception.IdNotFoundException;
import miniproject.onlinebookstore.service.BookOperationService;
import miniproject.onlinebookstore.service.JwtService;
import miniproject.onlinebookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService service;
    private final BookOperationService bookOperationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(UserService service, BookOperationService bookOperationService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.service = service;
        this.bookOperationService = bookOperationService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("user/register")
    public ResponseEntity<?> register (@RequestBody User user) throws CustomException {
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("users/{userId}")
    public ResponseEntity<?> getById (@PathVariable Long userId) throws IdNotFoundException {
        return ResponseEntity.ok(service.getUser(userId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_CUSTOMER')")
    @GetMapping("users/{userId}/history")
    public ResponseEntity<?> getUserHistory(@PathVariable Long userId){
        return ResponseEntity.ok(bookOperationService.getHistoryByUserId(userId));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("users/{userId}/books")
    public ResponseEntity<?> getBooksByUser (@PathVariable Long userId){
        return new ResponseEntity<>(service.getPreviouslyBorrowedBooksByUser(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("users/{userId}/borrowed-book")
    public ResponseEntity<?> getCurrentlyBorrowedBooksByUser (@PathVariable Long userId){
        return new ResponseEntity<>(service.getCurrentlyBorrowedBooksByUser(userId), HttpStatus.OK);
    }
    @PostMapping("user/login")
    public ResponseEntity<TokenResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(new TokenResponse(jwtService.generateToken(authRequest.getEmail())),HttpStatus.ACCEPTED);
        } else {
            throw new UsernameNotFoundException("invalid email/password!");
        }
    }

}
