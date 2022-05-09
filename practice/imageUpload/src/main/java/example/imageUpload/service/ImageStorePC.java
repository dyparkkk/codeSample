package example.imageUpload.service;

import example.imageUpload.domain.ImageUpload;
import example.imageUpload.domain.ImageUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageStorePC implements ImageStore{

    private final ImageUploadRepository imageUploadRepository;

    @Value("${file.dir}")
    private String fileDir;

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Override
    public List<ImageUpload> store(List<MultipartFile> multipartFiles) throws IOException {
        if( multipartFiles.isEmpty()) {
            return null;
        }
        List<ImageUpload> storeImageList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if( !multipartFile.isEmpty()){
                ImageUpload imageUpload = ImageUpload.createImageUpload(multipartFile);
                multipartFile.transferTo(new File(getFullPath(imageUpload.getStorageImageName())));
                storeImageList.add(imageUpload);
            }
        }

        imageUploadRepository.saveAll(storeImageList);
        return storeImageList;
    }
}
