package hello.advanced.trace.Template;

import hello.advanced.trace.Template.code.AbstractTemplate;
import hello.advanced.trace.Template.code.SubClassLogic1;
import hello.advanced.trace.Template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        log.info("비즈니스 로직 1 실행");
        long endTime = System.currentTimeMillis();
        log.info("resultTime={}", endTime - startTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        log.info("비즈니스 로직 2 실행");
        long endTime = System.currentTimeMillis();
        log.info("resultTime={}", endTime - startTime);
    }

    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.excute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.excute();
    }

    @Test
    void templateMethodV2() {
        AbstractTemplate template = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 1 ");
            }
        };
        template.excute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 1 ");
            }
        };
        template2.excute();
    }
}
