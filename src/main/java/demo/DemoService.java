package demo;

import java.util.concurrent.CompletableFuture;

public class DemoService {
    public CompletableFuture<String> hello() {
        return CompletableFuture.supplyAsync(() -> "hello");
    }
}
