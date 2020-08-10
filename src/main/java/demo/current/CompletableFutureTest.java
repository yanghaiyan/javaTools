package demo.current;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    public static void successFuture() throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "-task: Start");;
            return "task Success";
        }).handleAsync((result, exception) -> {
            if (exception != null) {
                System.out.println(Thread.currentThread().getName() + "-current task fail");
                return exception.getCause();
            } else {
                return result;
            }
        }).thenApplyAsync((returnStr) -> {
            System.out.println(Thread.currentThread().getName() + "-" + returnStr);
            return returnStr;
        });
        System.out.println(Thread.currentThread().getName() + "- do other thing");
        Thread.currentThread().join();
    }

    public static void failFuture() throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "-task: Start");
            throw new RuntimeException("do error");
        }).handleAsync((result, exception) -> {
            if (exception != null) {
                System.out.println(Thread.currentThread().getName() + "-current task fail");
                return exception.getCause();
            } else {
                return result;
            }
        }).thenApplyAsync((returnStr) -> {
            System.out.println(Thread.currentThread().getName() + "-" + returnStr);
            return returnStr;
        });
        System.out.println(Thread.currentThread().getName() + "-do other thing");
        Thread.currentThread().join();
    }

    public static void main(String[] args) throws Exception {
        failFuture();
    }
}
