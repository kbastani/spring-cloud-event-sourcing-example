package demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(UserServiceApplication.class)
@ActiveProfiles(profiles = "test")
public class UserServiceApplicationTests {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        flush();
    }

    private void flush() {
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushDb();
            return "OK";
        });
        userRepository.deleteAll();
    }

    @Test
    public void createUser() throws Exception {
        // Setup test data
        User expectedUser = new User("Jane", "Doe");

        // Test create user success
        User actualUser = objectMapper.readValue(this.mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(expectedUser))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), User.class);

        // Test create user conflict
        this.mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(expectedUser))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isConflict());

        // Clean up
        userService.deleteUser(actualUser.getId());
    }

    @Test
    public void getUser() throws Exception {
        // Setup test data
        User expectedUser = new User("John", "Doe");

        expectedUser = userService.createUser(expectedUser);

        // Test get user success
        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

        // Delete user
        userService.deleteUser(expectedUser.getId());

        // Test get user not found
        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isNotFound());

        // Clean up
        userService.deleteUser(expectedUser.getId());
    }

    @Test
    public void updateUser() throws Exception {
        // Setup test data
        User expectedUser = new User("Johnny", "Appleseed");

        // Test get user not found
        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isNotFound());

        // Test re-create user for cache invalidation
        expectedUser = userService.createUser(expectedUser);

        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

        // Change first name
        expectedUser.setFirstName("John");

        // Test update user for cache invalidation
        this.mvc.perform(put("/users/{id}", expectedUser.getId())
                .content(objectMapper.writeValueAsString(expectedUser))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent());

        // Test that user was updated
        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

        // Clean up
        userService.deleteUser(expectedUser.getId());
    }

    @Test
    public void deleteUser() throws Exception {
        // Setup test data
        User expectedUser = new User("Sally", "Ride");
        expectedUser = userService.createUser(expectedUser);

        // Test getting the user to put into cache
        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));

        // Delete the user and remove from cache
        this.mvc.perform(delete("/users/{id}", expectedUser.getId()))
                .andExpect(status().isNoContent());

        // Test get user not found
        this.mvc.perform(get("/users/{id}", expectedUser.getId()))
                .andExpect(status().isNotFound());
    }
}
