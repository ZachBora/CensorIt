package com.worldcretornica.censorit;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//CensorItAPI.SetVerifyWordOnline(true);
		CensorItAPI.AddWord("shit");
		CensorItAPI.AddWord("ass");
		CensorItAPI.AddWord("cock");
		CensorItAPI.AddWord("fuck");
		CensorItAPI.AddWord("cunt");
		CensorItAPI.AddWord("boob");
		CensorItAPI.AddWord("penis");
		CensorItAPI.AddHappyWord("bunny");
		CensorItAPI.AddHappyWord("flower");
		CensorItAPI.AddHappyWord("cloud");
		CensorItAPI.AddHappyWord("smurf");
		CensorItAPI.AddHappyWord("jell-o");
		CensorItAPI.SetReplaceWithHappyWords(true);
		
		String s = "5hit b00bs p3n1s";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "ass asstruck";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "fucktruckass";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "assfucktruckass";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "ass fuck truckass";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "abc abcd truck abcde";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "assfuck";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "fuck";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
		s = "fuckass";
		System.out.println(s + " -> " + CensorItAPI.Censor(s));
				
	}

}
