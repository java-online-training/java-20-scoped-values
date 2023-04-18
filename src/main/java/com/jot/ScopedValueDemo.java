package com.jot;

import jdk.incubator.concurrent.ScopedValue;

import java.util.List;

public class ScopedValueDemo {

	final static ScopedValue<String> USERNAME = ScopedValue.newInstance();


	public static void scopedValueInSimpleAction(List<String> usernames){

		for (String username : usernames) {
			ScopedValue.where(USERNAME, username).call( () -> {
				String message = "Username is "+new USERNAME.get();
				System.out.println(compound);
				return message;
			});
		}


	}
	public static void main(String[] args) {
		scopedValueInSimpleAction(List.of("Ernie", "Bert", "Jack"));
	}

}
