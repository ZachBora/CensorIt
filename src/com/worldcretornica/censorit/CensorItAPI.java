package com.worldcretornica.censorit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

public final class CensorItAPI {
	
	private static boolean _VerifyWordOnline = false;
	private static boolean _IgnoreSpecialCharacters = true;
	private static boolean _IgnoreSpaces = true;
	private static boolean _ReplaceWithHappyWords = false;
	
	private static HashSet<String> _CensorList = new HashSet<String>();
	private static HashSet<String> _HappyList = new HashSet<String>();
	
	public static String Censor(String text)
	{
		//TODO implement censor
		//Check existing words
		//Replace with happy words or *
		return "";
	}

	
	public static boolean IsExistingWord(String word)
	{
		if (_VerifyWordOnline)
		{
			InputStream result = null;
			BufferedReader reader = null;
			String line = null;
						
			try {
				//result = new URL("http://dictionary.cambridge.org/dictionary/british/" + word + "?q=" + word).openStream();
				//utm_source=widget_searchbox_source&utm_medium=widget_searchbox&utm_campaign=widget_tracking&
				result = new URL("http://dictionary.cambridge.org/search/british/direct/?q=" + word).openStream();
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
			
			
						
			//TODO implement online word verification
		
			//http://dictionary.cambridge.org/learnenglish/results.asp?searchword='+text+ '&dict=L
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
}
