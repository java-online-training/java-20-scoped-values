package com.jot;

import jdk.incubator.concurrent.ScopedValue;
import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ScopedValueDemo {

	public final static ScopedValue<String> USERNAME = ScopedValue.newInstance();

    /* Simple example for the use of ScopedValues  */

    private static  Runnable printMessage = () ->  System.out.println("Username is "+USERNAME.get());

	public static void scopedValueInSimpleAction() throws Exception {

        ScopedValue.where(USERNAME, "Bob").run(printMessage);

        ScopedValue.where(USERNAME, "Chris").run(printMessage);

        System.out.println("Username bound: "+ USERNAME.isBound());
	}

    /* Example for isBound Method  */
    private static class MyRunnable implements Runnable {
        public void run() {
            System.out.println(Thread.currentThread());
            if (USERNAME.isBound()) {
                String message = "Username is " + USERNAME.get();
                System.out.println(message);
            } else {
                System.out.println("Username not bound");
            }
        }
    }

    private static class VThreadStarter implements Runnable {
        public void run() {
            System.out.println(Thread.currentThread());
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                Future<String> task1 =  scope.fork(revertUsername);
                Future<String> task2 =scope.fork(revertUsername);
                Future<String> task3 =scope.fork(revertUsername);
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


    public static void scopedValueWithCheck(List<String> usernames) throws Exception {
        for (String username : usernames) {
            ScopedValue.where(USERNAME, username).run(new MyRunnable());
        }
    }

    public static void startVthreads(List<String> usernames) throws Exception {
        for (String username : usernames) {
            ScopedValue.where(USERNAME, username).run(new VThreadStarter());
        }
    }


    /* Example for ScopedValues withCallable  */
    private static  Callable<String> revertUsername = () ->  {
        System.out.println(Thread.currentThread());
        if (USERNAME.isBound() && USERNAME.get() != null) {
            return new StringBuilder(USERNAME.get()).reverse().toString();
        };
        return "";
    };


    public static void reverseUsernames(List<String> usernames) throws Exception {
        System.out.println(Thread.currentThread());
        for (String username : usernames) {
            System.out.println("Reversed Username is "+ScopedValue.where(USERNAME, username).call(revertUsername));
        }
    }


	public static void main(String[] args) throws Exception {

        List<String> names = Arrays.asList("Ernie", "Bert", "Jack");

		//scopedValueInSimpleAction(names);

        //System.out.println("---------------------------------------");

        //new MyRunnable().run();
        //scopedValueWithCheck(names);

        //System.out.println("---------------------------------------");

        //reverseUsernames(names);

        System.out.println("---------------------------------------");

        startVthreads(names);
	}

}
