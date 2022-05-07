package example.imageUpload.service;

import example.imageUpload.domain.ImageUpload;
import example.imageUpload.domain.ImageUploadRepository;
import example.imageUpload.domain.Post;
import example.imageUpload.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageUploadRepository imageUploadRepository;

    @Transactional
    public Long postSave(List<MultipartFile> multipartFiles, String content){
        Post post = new Post(content);

        List<ImageUpload> imageUploadList = ImageUpload.createImageUploadList(multipartFiles);
        if(imageUploadList != null) {
            // 이미지 저장 로직 (추가 예정)

            post.addImageUploads(imageUploadList);
            imageUploadRepository.saveAll(imageUploadList);
        }

        return postRepository.save(post).getId();
    }
}
