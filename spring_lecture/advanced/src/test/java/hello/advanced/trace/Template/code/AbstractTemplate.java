package hello.advanced.trace.Template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

    public void excute() {
        long startTime = System.currentTimeMillis();
        call(); // 비즈니스 로직 실행
        long endTime = System.currentTimeMillis();
        log.info("resultTime={}", endTime - startTime);
    }

    protected abstract void call();

}
