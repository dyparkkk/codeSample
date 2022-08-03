package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 변하는 부분은 strategy, 변하지 않는 부분은 context
 * 선 조립, 후 실행
 * -> 어플리케이션 로딩 시점에 의존관계를 통해 조립하고, 후에 요청을 처리함
 *
 * 단점 : 한 번 조립한 후에는 변경하기가 까다로움 ...
 *  변경을 원한다면 새로운 컨텍스트를 하나 더 생성해서 사용하는 것이 편함
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        strategy.call();
        long endTime = System.currentTimeMillis();
        log.info("resultTime={}", endTime - startTime);
    }


}
