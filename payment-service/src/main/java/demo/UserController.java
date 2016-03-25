package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * This controller acts as a REST resource repository for users
 * and uses Redis for caching.
 *
 * @author Kenny Bastani
 * @author Josh String
 */
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "users", method = RequestMethod.POST,
            name = "createUser")
    public ResponseEntity createUser(@RequestBody User user) {
        Assert.notNull(user);
        return Optional.ofNullable(userService.createUser(user))
                .map(result -> new ResponseEntity<>(result, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @RequestMapping(path = "users/{id}", method = RequestMethod.GET,
            name = "getUser")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") String id) {
        return Optional.ofNullable(userService.getUser(id))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(path = "users/{id}", method = RequestMethod.PUT,
            name = "updateUser")
    public ResponseEntity updateUser(@PathVariable(value = "id") String id,
                                     @RequestBody User user) {
        Assert.notNull(user);
        user.setId(id);
        return Optional.ofNullable(userService.updateUser(id, user))
                .map(result -> new ResponseEntity(HttpStatus.NO_CONTENT))
                .orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(path = "users/{id}", method = RequestMethod.DELETE,
            name = "deleteUser")
    public ResponseEntity deleteUser(@PathVariable(value = "id") String id) {
        return Optional.ofNullable(userService.deleteUser(id))
                .map(result -> new ResponseEntity<>(result, HttpStatus.NO_CONTENT))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
