package example.imageUpload.web;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostForm {
    private String content;
    private List<MultipartFile> imagefiles;
}
