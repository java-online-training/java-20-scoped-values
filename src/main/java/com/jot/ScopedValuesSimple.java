package com.jot;

import jdk.incubator.concurrent.ScopedValue;

public class ScopedValuesSimple {

    public final static ScopedValue<String> USERNAME = ScopedValue.newInstance();

    private static  Runnable printMessage = () ->  System.out.println("Username is "+USERNAME.get());

    public static void scopedValueInSimpleAction() throws Exception {

        ScopedValue.where(USERNAME, "Bob").run(printMessage);

        ScopedValue.where(USERNAME, "Chris").run(printMessage);

        System.out.println("Username bound: "+ USERNAME.isBound());
    }

    public static void main(String[] args) throws Exception {
        scopedValueInSimpleAction();
    }
}
