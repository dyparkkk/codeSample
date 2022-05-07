package example.imageUpload.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureMockMvc
class ImageUploadTest {

    @Test
    void createImageUploadTest() throws IOException {
        // given
        URL resource = getClass().getResource("/img/404.png");
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                "test.png", "image/png",
                new FileInputStream(resource.getFile()));

        // when
        ImageUpload imageUpload = ImageUpload.createImageUpload(multipartFile);

        // then
        String originFilename = (String) ReflectionTestUtils.getField(imageUpload, "originFilename");

        assertThat(originFilename).isEqualTo("test.png");
        assertThat(imageUpload).extracting("storageImageName").isNotNull();
    }

    @Test
    void pathTest() throws FileNotFoundException {
        URL resource = getClass().getResource("/img/404.png");
        System.out.println("URL : " + resource );
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());
        System.out.println("fileInputStream : " + fileInputStream);
    }

    @Test
    void createImageUploadListTest() throws IOException {
        // given
        URL resource = getClass().getResource("/img/404.png");
        MockMultipartFile multipartFile1 = new MockMultipartFile("image",
                "404.png", "image/png",
                new FileInputStream(resource.getFile()));

        URL resource2 = getClass().getResource("/img/raspberry.png");
        MockMultipartFile multipartFile2 = new MockMultipartFile("image2",
                "raspberry.png", "image/png",
                new FileInputStream(resource2.getFile()));

        List<MultipartFile> arrayList = new ArrayList<>();
        arrayList.add(multipartFile1);
        arrayList.add(multipartFile2);

        // when
        List<ImageUpload> imageUploadList = ImageUpload.createImageUploadList(arrayList);

        // then
        assertThat(imageUploadList).extracting("originFilename")
                .contains("raspberry.png", "404.png");
        assertThat(imageUploadList).extracting("storageImageName")
                .isNotNull();
    }
}