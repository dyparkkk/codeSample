package example.imageUpload.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class PostForm {
    private String content;
    private List<MultipartFile> imageFiles;
}
