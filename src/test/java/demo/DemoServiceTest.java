package demo;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class DemoServiceTest {
    @Test
    public void exceptionWontBeCaptured() {
        DemoService demoService = new DemoService();

        demoService.hello()
                .whenComplete((result, e) -> {
                    Assert.assertEquals("wrongValue", result);
                });
    }

    @Test
    public void blockMainThread() throws ExecutionException, InterruptedException {
        DemoService demoService = new DemoService();

        Assert.assertEquals("hello", demoService.hello().get());
    }

    @Test
    public void waitOnCountDown() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        DemoService demoService = new DemoService();
        AtomicReference<String> actualValue = new AtomicReference<>("");

        demoService.hello()
                .whenComplete((result, e) -> {
                    actualValue.set(result);
                    countDownLatch.countDown();
                });

        countDownLatch.await();

        Assert.assertEquals("hello", actualValue.get());
    }

    @Test
    public void poweredByAwaitility() {
        DemoService demoService = new DemoService();
        AtomicReference<String> actualValue = new AtomicReference<>("");

        demoService.hello()
                .whenComplete((result, e) -> {
                    actualValue.set(result);
                });

        await().atMost(5, SECONDS).untilAsserted(() -> {
            Assert.assertEquals("hello", actualValue.get());
        });
    }
}
