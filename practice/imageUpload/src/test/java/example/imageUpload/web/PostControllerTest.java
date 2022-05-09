package example.imageUpload.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void 이미지업로드와_함께_포스트생성() throws Exception {

        List<MultipartFile> multipartFileList = List.of(
                new MockMultipartFile("image", "404.png", "image/png",
                        new FileInputStream(getClass().getResource("/img/404.png").getFile())),
                new MockMultipartFile("image2","raspberry.png", "image/png",
                        new FileInputStream(getClass().getResource("/img/raspberry.png").getFile()))
        );

        //https://kukekyakya.tistory.com/549
        mvc.perform(multipart("/posts/new")
                .file("imageFiles", multipartFileList.get(0).getBytes())
                .file("imageFiles", multipartFileList.get(1).getBytes())
                .param("content", "test content"))
                .andExpect(status().is3xxRedirection());
    }
}