package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 특정 api의 종속어 상당히 복잡한 쿼리의 경우 굳이 custom에 넣지 않고 따로 빼서 작성하는 것도 방법이다. !!
 *  -> 다른 쿼리들과 수정의 라이프 사이클이 달라서 설계적으로 더 이점이 있음
 */
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MemberQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<MemberTeamDto> hardSearch(MemberSearchCondition condition) {
        // 구현 ...
        return null;
    }
}
