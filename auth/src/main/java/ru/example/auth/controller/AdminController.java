package ru.example.auth.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.example.auth.entity.User;
import ru.example.auth.service.by_entities.UserService;

@RestController
@RequestMapping("/api/v1")
@Api(value = "UserController")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @ApiOperation(value = "Поиск пользователя по id")
    @GetMapping(
            path = "/user/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final User user = userService.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @ApiOperation(value = "Частичное обновление данных пользователя с указанным id")
    @PatchMapping(
            path = "/user/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<User> patch(
            @PathVariable("id") Long id,
            @RequestBody User user
    ) {
        final HttpHeaders headers = new HttpHeaders();

        if (id == null || user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final User userExist = userService.findById(id).orElse(null);
        if (userExist == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //todo
        userService.patch(user);
        return new ResponseEntity<>(user, headers, HttpStatus.ACCEPTED);
    }
}
