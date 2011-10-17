package com.worldcretornica.censorit;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CensorItAPI.SetVerifyWordOnline(true);
		System.out.println("Bob : " + CensorItAPI.IsExistingWord("bob"));
		System.out.println("5a3yhe : " + CensorItAPI.IsExistingWord("5a3yhe"));
		System.out.println("ass : " + CensorItAPI.IsExistingWord("ass"));
		System.out.println("assassin : " + CensorItAPI.IsExistingWord("assassin"));
		
	}

}
