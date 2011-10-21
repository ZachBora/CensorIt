package com.worldcretornica.censorit;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//CensorItAPI.setVerifyWordOnline(true);
		CensorItAPI.addCensoredWord("shit");
		CensorItAPI.addCensoredWord("ass");
		CensorItAPI.addCensoredWord("cock");
		CensorItAPI.addCensoredWord("fuck");
		CensorItAPI.addCensoredWord("cunt");
		CensorItAPI.addCensoredWord("boob");
		CensorItAPI.addCensoredWord("penis");
		CensorItAPI.addHappyWord("bunny");
		CensorItAPI.addHappyWord("flower");
		CensorItAPI.addHappyWord("cloud");
		CensorItAPI.addHappyWord("smurf");
		CensorItAPI.addHappyWord("jell-o");
		//CensorItAPI.setReplaceWithHappyWords(true);
		//CensorItAPI.setReplacementChar('%');
		CensorItAPI.addAllowedWord("assassin");
		CensorItAPI.addAllowedWord("grass");
		CensorItAPI.setUseRandomChar(true);
		
		
		String s = "5hit b00bs p3n1s";
		boolean c = false;
		/*System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "ass asstruck";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "fucktruckass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "assfucktruckass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "ass fuck truckass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "abc abcd truck abcde";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "assfuck";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "fuck";
		System.out.println(s + " -> " + CensorItAPI.censor(s));
		s = "fuckass";
		System.out.println(s + " -> " + CensorItAPI.censor(s));
		s = "fuuuuuuuuckk";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "fuuuu uuuuckk";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "fu uck co ck pe nis";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "assassin";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "an assassin killed me";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "ass assassin ass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "ASS asSassin ass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "ASS grass ass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "ASS Pass ass";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "I WANT AN ASS GRASS";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));
		s = "fuck fuuck fuck fu ck FUck FUCKfu CK";
		System.out.println(s + " -> " + CensorItAPI.censor(s, c));*/
		String msg = "&test &&&555&&44Test";
		System.out.println(msg = msg.replaceAll("(&+([a-f0-9]))", ""));
	}

}
