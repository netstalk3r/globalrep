package com.akvelon.ets.verifier;

public class App {

	public static void main(String[] args) throws Exception {

		if (args.length != 2)
			throw new IllegalArgumentException("Invalid number of params: " + args.length + " Must be 2 params: login and password");
		
		Verifier verifier = new Verifier();
		verifier.verify(args[0], args[1]);
	}
}
