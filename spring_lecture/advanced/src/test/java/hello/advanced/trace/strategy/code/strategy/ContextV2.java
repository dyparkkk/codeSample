package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 전략을 파라미터로 전달받는 방식
 *
 * 실행할 때 마다 유연하게 전략을 변경할 수 있음
 *
 * 단점 : 실행할 때마다 전략을 지정해야함 ...
 */
@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        strategy.call();
        long endTime = System.currentTimeMillis();
        log.info("resultTime={}", endTime - startTime);
    }
}
