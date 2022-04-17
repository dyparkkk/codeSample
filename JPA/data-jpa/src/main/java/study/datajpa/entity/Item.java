package study.datajpa.entity;

import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item extends BaseTimeEntity implements Persistable<String> {

//    @Id @GeneratedValue
//    private Long id;

    @Id
    private String id;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return super.getCreateDate() == null;
    }
}
