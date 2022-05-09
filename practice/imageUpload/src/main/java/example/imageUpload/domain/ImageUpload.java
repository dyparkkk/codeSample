package example.imageUpload.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUpload {

    @Id @GeneratedValue
    private Long id;

    private String originFilename;
    private String storageImageName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private ImageUpload(String originFilename, String storageImageName) {
        this.originFilename = originFilename;
        this.storageImageName = storageImageName;
    }

    public static List<ImageUpload> createImageUploadList(List<MultipartFile> multipartFiles) {
        if( multipartFiles.isEmpty()) {
            return null;
        }
        List<ImageUpload> storeImageList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            storeImageList.add(ImageUpload.createImageUpload(multipartFile));
        }
        return storeImageList;
    }

    public static ImageUpload createImageUpload(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeImageName = createStoreImageName(originalFilename);

        return new ImageUpload(originalFilename, storeImageName);
    }

    private static String createStoreImageName(String originalFilename) {
        String ext = extractExt(originalFilename);
        return UUID.randomUUID() + "." + ext;
    }

    private static String extractExt(String originalFilename) {
        // test.png ->
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    protected void setPost(Post post) {
        this.post = post;
    }

    public String getStorageImageName() {
        return storageImageName;
    }
}
