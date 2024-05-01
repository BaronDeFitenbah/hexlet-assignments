package exercise.controller.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

import javax.swing.text.html.Option;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
    public List<Post> posts = Data.getPosts();
    @GetMapping("users/{id}/posts")
    public List<Post> show(@PathVariable String id) {
        var postFiltered = posts.stream()
                .filter(p -> p.getUserId() == Integer.parseInt(id))
                .toList();
        return postFiltered;
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> create(@RequestBody Post post,@PathVariable Integer id) {
        post.setUserId(id);
        posts.add(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }
}
// END
