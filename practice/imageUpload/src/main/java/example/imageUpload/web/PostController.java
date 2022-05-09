package example.imageUpload.web;

import example.imageUpload.domain.Post;
import example.imageUpload.domain.PostRepository;
import example.imageUpload.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/posts/new")
    public String newPost(@ModelAttribute PostForm postForm) {
        return "post-form";
    }

    @PostMapping("/posts/new")
    public String savePost(@ModelAttribute PostForm postForm, RedirectAttributes redirectAttributes) throws IOException {
        Long postId = postService.postSave(postForm.getImageFiles(), postForm.getContent());

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/post/{postId}";
    }

    @GetMapping("/posts/{postId}")
    public String posts(@PathVariable Long postId, Model model) {
        Post post = postRepository.findById(postId).get();
        return "success";
    }
}
