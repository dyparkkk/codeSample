package com.example.dddstart.catalog.product;

import javax.persistence.*;
import java.util.Date;

/**
 * 이미지 업로드 방식에 따라서 경로와 썸네일 이미지 제공 여부가 달라짐
 * 엔티티 상속으로 구현
 * DB는 상속이란 개념이 없음 -> 여러 구현 전략 가능 ( single table || join table)
 * @Inheritance 로 전략 선택 가능
 * @DiscriminatorColumn로 상속 되는 엔티티 구분해야함 (안하면 클래스 명으로 구분됨)
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
public abstract class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_path")
    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "upload_time")
    private Date uploadTime;

    protected Image() {
    }

    public Image(String path) {
        this.path = path;
        this.uploadTime = new Date();
    }

    protected String getPath() {
        return path;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public abstract String getUrl();

    public abstract boolean hasThumbnail();

    public abstract String getThumbnailUrl();
}
