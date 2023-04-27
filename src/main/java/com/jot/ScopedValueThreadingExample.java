package com.jot;

import jdk.incubator.concurrent.ScopedValue;
import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ScopedValueThreadingExample {

    public final static ScopedValue<String> USERNAME = ScopedValue.newInstance();

    private static class VThreadStarter implements Runnable {
        public void run() {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                Future<String> task1 = scope.fork(revertUsername);
                Future<String> task2 = scope.fork(revertUsername);
                Future<String> task3 = scope.fork(revertUsername);
                try {
                    scope.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(task1.resultNow());
                System.out.println(task2.resultNow());
                System.out.println(task3.resultNow());
            }
        }
    }

    private static Callable<String> revertUsername = () -> {
        if (USERNAME.isBound() && USERNAME.get() != null) {
            return new StringBuilder(USERNAME.get()).reverse().toString();
        };
        return "";
    };

    public static void main(String[] args) throws Exception {
        List<String> names = Arrays.asList("Ernie", "Bert", "Jack");
        for (String username : names) {
            ScopedValue.where(USERNAME, username).run(new VThreadStarter());
        }
    }
}
