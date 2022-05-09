package example.imageUpload.service;

import example.imageUpload.domain.ImageUpload;
import example.imageUpload.domain.ImageUploadRepository;
import example.imageUpload.domain.Post;
import example.imageUpload.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageStore imageStore;

    @Transactional
    public Long postSave(List<MultipartFile> multipartFiles, String content) throws IOException {
        Post post = new Post(content);
        List<ImageUpload> imageUploadList = imageStore.store(multipartFiles);
        post.addImageUploads(imageUploadList);

        return postRepository.save(post).getId();
    }
}
