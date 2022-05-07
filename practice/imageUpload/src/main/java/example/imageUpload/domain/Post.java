package example.imageUpload.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String content;

    @OneToMany(mappedBy = "post")
    private Set<ImageUpload> imageUploads = new HashSet<>();

    public Post(String content) {
        this.content = content;
    }

    // 연관관계 초기화 메서드
    public void addImageUploads(List<ImageUpload> imageUploads) {
        if( imageUploads == null){
            throw new IllegalArgumentException("[Post.addImageUpload]: List<ImageUpload> is null");
        }
        for (ImageUpload imageUpload : imageUploads) {
            imageUpload.setPost(this);
            this.imageUploads.add(imageUpload);
        }
    }

    public Long getId() {
        return id;
    }
}
