package exercise.controller;

import exercise.model.Comment;
import exercise.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import exercise.model.Post;
import exercise.repository.PostRepository;
import exercise.exception.ResourceNotFoundException;
import exercise.dto.PostDTO;
import exercise.dto.CommentDTO;

// BEGIN
@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping(path = "")
    public List<PostDTO> show() {
        var posts = postRepository.findAll();
        var result = posts.stream()
                .map(this::toDTO)
                .toList();
        return result;
    }

    @GetMapping(path = "/{id}")
    public PostDTO show(@PathVariable long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return toDTO(post);
    }

    private PostDTO toDTO (Post post) {
        var dto = new PostDTO();
        dto.setBody(post.getBody());
        dto.setId(post.getId());
        dto.setComments(commentRepository.findByPostId(post.getId()).stream().map(this::toDTO).toList());
        return dto;
    }

    private CommentDTO toDTO(Comment comment) {
        var dto = new CommentDTO();
        dto.setBody(comment.getBody());
        dto.setId(comment.getId());
        return dto;
    }


}
// END
