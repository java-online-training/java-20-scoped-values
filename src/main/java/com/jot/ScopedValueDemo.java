package com.jot;

import jdk.incubator.concurrent.ScopedValue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class ScopedValueDemo {

	public final static ScopedValue<String> USERNAME = ScopedValue.newInstance();

    /* Simple example for the use of ScopedValues  */

    private static  Runnable printMessage = () -> {
        String message = "Username is "+USERNAME.get();
        System.out.println(message);
    };

	public static void scopedValueInSimpleAction(List<String> usernames) throws Exception {
		for (String username : usernames) {
			ScopedValue.where(USERNAME, username).run(printMessage);
		}
	}

    /* Example for isBound Method  */
    private static  Runnable printMessageWithCheck = () -> {
        if (USERNAME.isBound()){
            String message = "Username is "+USERNAME.get();
            System.out.println(message);
        } else {
            System.out.println("Username not bound");
        }
    };

    public static void scopedValueWithCheck(List<String> usernames) throws Exception {
        for (String username : usernames) {
            ScopedValue.where(USERNAME, username).run(printMessageWithCheck);
        }
    }


    /* Example for ScopedValues withCallable  */
    private static  Callable<String> revertUsername = () ->  {
        if (USERNAME.isBound() && USERNAME.get() != null) {
            return new StringBuilder(USERNAME.get()).reverse().toString();
        };
        return "";
    };


    public static void reverseUsernames(List<String> usernames) throws Exception {
        for (String username : usernames) {
            System.out.println(ScopedValue.where(USERNAME, username).call(revertUsername));
        }
    }


	public static void main(String[] args) throws Exception {

        List<String> names = Arrays.asList("Ernie", "Bert", "Jack", null);

		scopedValueInSimpleAction(names);

        System.out.println("---------------------------------------");

        scopedValueWithCheck(names);

        System.out.println("---------------------------------------");

        reverseUsernames(names);
	}

}
