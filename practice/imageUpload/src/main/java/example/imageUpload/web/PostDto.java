package example.imageUpload.web;

import example.imageUpload.domain.ImageUpload;
import example.imageUpload.domain.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private String content;
    private List<ImageUpload> imageUploads;

//    public PostDto(Post post) {
//        this.content =
//    }
}
