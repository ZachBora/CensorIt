package com.worldcretornica.censorit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public final class CensorItAPI {
	
	private static boolean _VerifyWordOnline = false;
	private static boolean _IgnoreSpecialCharacters = true;
	private static boolean _IgnoreSpaces = true;
	private static boolean _ReplaceWithHappyWords = false;
	private static String _OnlineDictionaryURL = "http://dictionary.cambridge.org/search/british/direct/?q=<word>";
	
	private static HashSet<String> _CensorList = new HashSet<String>();
	private static HashSet<String> _HappyList = new HashSet<String>();
	
	public static String Censor(String text)
	{
		//TODO implement censor
		//Check existing words
		//Replace with happy words or *
		
		ArrayList<Word> wordlist = new ArrayList<Word>();
		int ctr = 0;
		
		for(String word : text.split(" "))
		{
			Word w = new Word();
			w.Text = word;
			w.Position = ctr;
			w.Verified = false;
			w.Curse = false;
			
			wordlist.add(w);
		}
		
		
		
		for(Word w: wordlist)
		{
			//Check if the word exists
			if (_VerifyWordOnline)
			{
				if (IsExistingWord(RemoveSpecial(w.Text)))
					w.Verified = true;
			}
			
			for(String censor: _CensorList)
			{
				//Check if it's in the curse list
				if(RemoveSpecial(ReplaceSpecial(w.Text)).contains(censor))
				{
					w.Curse = true;
					
					if (_ReplaceWithHappyWords && _HappyList.size() > 0)
					{
						Random rand = new Random();
						int i = rand.nextInt(_HappyList.size());
					
						w.Text = RemoveSpecial(ReplaceSpecial(w.Text)).replace(censor, (String) _HappyList.toArray()[i]);
					}else{
						w.Text = RemoveSpecial(ReplaceSpecial(w.Text)).replace(censor, repeat('*',censor.length()));
					}
					
				}
			}
		}
		
		//Rebuild the string
		String str = "";
		for(Word w: wordlist)
		{
			str = str + " " + w.Text;
		}
		
		return str.trim();
	}
	
	
	private static String repeat(char c,int i)
    {
	    String s = "";
	    for(int j = 0; j < i; j++)
	    {
	        s = s + c;
	    }
	    return s;
	}
	
	
	public static String RemoveSpecial(String word)
	{
		return word.replaceAll("[^A-Za-z0-9*]", "");
	}
	
	public static String ReplaceSpecial(String word)
	{
		return word.replace("4", "a").replace("1", "i").replace("@", "a")
				.replace("3", "e").replace("!", "i").replace("5", "s")
				.replace("$", "s").replace("0", "o");
	}
		
	public static boolean IsExistingWord(String word)
	{
		if (_VerifyWordOnline && word.length() > 3)
		{
			InputStream result = null;
			BufferedReader reader = null;
			String line = null;
						
			try {
				result = new URL(_OnlineDictionaryURL.replace("<word>", word)).openStream();
				reader = new BufferedReader(new InputStreamReader(result));
				
				while ((line = reader.readLine()) != null){
					if(line.contains("<title>") && line.contains(word) && !line.contains("Did you spell it correctly"))
						return true;
					if(line.contains("</head>"))
						return false;
				}
				
				reader.close();
			} catch (IOException e) {
				return false;
			}finally{
				try {
					if (reader != null)
						reader.close();
				} catch (IOException e) {}
				try {
					if (result != null)
						result.close();
				} catch (IOException e) {}
			}
			
			return false;
		}else
			return false;
	}
	
	
	
	public static void AddWord(String word)
	{
		_CensorList.add(word);
	}
	
	public static void AddHappyWord(String word)
	{
		_HappyList.add(word);
	}
	
	public static void RemoveWord(String word)
	{
		_CensorList.remove(word);
	}
	
	public static void RemoveHappyWord(String word)
	{
		_HappyList.remove(word);
	}
	
	public static void SetWords(HashSet<String> censoredwords)
	{
		_CensorList.addAll(censoredwords);
	}
	
	public static void SetHappyWords(HashSet<String> happywords)
	{
		_HappyList.addAll(happywords);
	}
	
	public static void SetWords(String[] censoredwords)
	{
		for(String word: censoredwords)
		{
			_CensorList.add(word);
		}
	}
	
	public static void SetHappyWords(String[] happywords)
	{
		for(String word: happywords)
		{
			_HappyList.add(word);
		}
	}
	
	public static void SetWords(Object[] censoredwords)
	{
		for(Object word: censoredwords)
		{
			_CensorList.add((String) word);
		}
	}
	
	public static void SetHappyWords(Object[] happywords)
	{
		for(Object word: happywords)
		{
			_HappyList.add((String) word);
		}
	}
	
	public static HashSet<String> GetWords()
	{
		return _CensorList;
	}
	
	public static HashSet<String> GetHappyWords()
	{
		return _HappyList;
	}
	
	public static void SetVerifyWordOnline(boolean b)
	{
		_VerifyWordOnline = b;
	}
	
	public static void SetIgnoreSpecialCharacters(boolean b)
	{
		_IgnoreSpecialCharacters = b;
	}
	
	public static void SetIgnoreSpaces(boolean b)
	{
		_IgnoreSpaces = b;
	}
	
	public static void SetReplaceWithHappyWords(boolean b)
	{
		_ReplaceWithHappyWords = b;
	}
	
	public static void SetOnlineDictionary(String s)
	{
		_OnlineDictionaryURL = s;
	}
	
	public static boolean GetVerifyWordOnline()
	{
		return _VerifyWordOnline; 
	}
		
	public static boolean GetIgnoreSpecialCharacters()
	{
		return _IgnoreSpecialCharacters;
	}
	
	public static boolean GetIgnoreSpaces()
	{
		return _IgnoreSpaces;
	}

	public static boolean GetReplaceWithHappyWords()
	{
		return _ReplaceWithHappyWords;
	}
	
	public static String GetOnlineDictionary()
	{
		return _OnlineDictionaryURL;
	}
}
