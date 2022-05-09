package example.imageUpload.service;

import example.imageUpload.domain.ImageUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageStore {

    public List<ImageUpload>  store(List<MultipartFile> multipartFiles) throws IOException;
}
