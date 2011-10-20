package com.worldcretornica.censorit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CensorItAPI {
	
	private static boolean _VerifyWordOnline = false;
	//private static boolean _IgnoreSpecialCharacters = true;
	//private static boolean _IgnoreSpaces = true;
	private static boolean _ReplaceWithHappyWords = false;
	private static boolean _RandomReplacementChar = true;
	private static String _OnlineDictionaryURL = "http://dictionary.cambridge.org/search/british/direct/?q=<word>";
	private static char _ReplacementChar = '*';
	
	private static HashSet<String> _CensorList = new HashSet<String>();
	private static HashSet<String> _AllowedList = new HashSet<String>();
	private static HashSet<String> _HappyList = new HashSet<String>();
	
	public static String censor(String text)
	{
		return censor(text, false);
	}
	
	public static String censor(String text, boolean WasCensored)
	{
		return censor(text, WasCensored, new HashSet<String>());
	}
	
	public static String censor(String text, HashSet<String> CensoredWords)
	{
		return censor(text, false, CensoredWords);
	}
	
	public static String censor(String text, boolean WasCensored, HashSet<String> CensoredWords)
	{			
		ArrayList<String> replacedwords = new ArrayList<String>();
		String aftertext = "";
		
		for(String s: text.split(" "))
		{
			if (!_CensorList.contains(s.toLowerCase()))
			{
				if(_AllowedList.contains(s.toLowerCase()))
				{
					aftertext = aftertext + " " + "\n";
					replacedwords.add(s);
				}else{
					if (_VerifyWordOnline && isExistingWord(removeSpecial(s)))
					{
						aftertext = aftertext + " " + "\n";
						replacedwords.add(s);
					}else{
						aftertext = aftertext + " " + s;
					}
				}
			}else{
				aftertext = aftertext + " " + s;
			}
		}
		text = aftertext;
		
		Word word = new Word();
		word.Text = text;
		word.Verified = false;
		word.Curse = false;
		
		if (_VerifyWordOnline)
		{
			if (isExistingWord(removeSpecial(word.Text)))
				word.Verified = true;
		}
		
		//if (!word.Verified)
		//{
			word = removeDoubleLetter(word);
		//}
	
		WasCensored = word.Curse;
			
		text = word.Text;

		if(text.contains("\n"))
		{
			aftertext = "";
			for(String str: text.split("\n"))
			{
				if (replacedwords.size() > 0)
				{
					if(!aftertext.endsWith(" ") && !str.startsWith(" "))
						aftertext += " ";
					aftertext += str;
					if(!aftertext.endsWith(" "))
						aftertext += " ";
					aftertext += replacedwords.remove(0);
				}else{
					if(!aftertext.endsWith(" ") && !str.startsWith(" "))
						aftertext += " ";
					aftertext = aftertext + str;
				}
			}
			text = aftertext;
		}
		
		return text.trim();
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
	
	private static Word removeDoubleLetter(Word word)
	{
		int currentsize;
		int previoussize;
		
		Word initialword = word.clone();
		word = replaceCensoredWord(word);

		do
		{
			previoussize = word.Text.length();
			for(int i = 0; i < 26; i++)
			{
				char a = 'a';
				a += i;
				word.Text = word.Text.replace("" + a + a, "" + a);
				word.Text = word.Text.replace("" + a + " " + a, "" + a);
			}
			currentsize = word.Text.length();
			word = replaceCensoredWord(word);
		}while(currentsize != previoussize);
		
		if(word.Curse)
		{
			if (_ReplaceWithHappyWords && _HappyList.size() > 0)
			{								
				Pattern pattern = Pattern.compile("\t", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(word.Text);

				while(matcher.find())
				{
					Random rand = new Random();
					int i = rand.nextInt(_HappyList.size());
				
					word.Text = matcher.replaceFirst(" " + (String) _HappyList.toArray()[i]);
					matcher = pattern.matcher(word.Text);
				}
				
			}else{
				word.Text = randomizeChar(word.Text.replace('\t', _ReplacementChar));
			}
		}else{
			word = initialword;
		}
		
		word.Text = word.Text.replace("  ",  " ");
		return word;
	}
	
	private static Word replaceCensoredWord(Word w)
	{
		for(String censor: _CensorList)
		{
			Pattern pattern = Pattern.compile(censor, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(strip(w.Text));

			if(matcher.find())
			{
				w.Curse = true;
				
				if (_ReplaceWithHappyWords && _HappyList.size() > 0)
				{
					w.Text = matcher.replaceAll(" \t");
				}else{
					w.Text = matcher.replaceAll(" " + repeat('\t',censor.length()));
				}
			}
		}
		return w;
	}
	
	private static String strip(String word)
	{
		return removeSpecial(replacePseudoLetters(word));
	}
	
	
	public static String randomizeChar(String text)
	{
		String newtext = "";
		if (_RandomReplacementChar)
		{
			HashSet<Character> chararacters = new HashSet<Character>();
			
			for(char c: text.toCharArray())
			{
				if(chararacters.isEmpty())
				{
					chararacters.add('!');
					chararacters.add('$');
					chararacters.add('%');
					chararacters.add('@');
					chararacters.add('#');
					chararacters.add('?');
					chararacters.add('~');
					chararacters.add('&');
				}
				
				if(c == _ReplacementChar)
				{
					Random rand = new Random();
					char n = (Character) (chararacters.toArray()[rand.nextInt(chararacters.size())]);
					
					newtext += n;
					chararacters.remove(n);
				}else{
					newtext += c;
				}
			}
			
			return newtext;
		}else{
			return text;
		}
	}
	
	public static String removeSpecial(String word)
	{
		return word.replaceAll("[^A-Za-z0-9" + _ReplacementChar + "\t\n]", "");
	}
	
	public static String replacePseudoLetters(String word)
	{
		return word.replace("4", "a").replace("1", "i").replace("@", "a")
				.replace("3", "e").replace("!", "i").replace("5", "s")
				.replace("$", "s").replace("0", "o");
	}
		
	public static boolean isExistingWord(String word)
	{
		if (_VerifyWordOnline && word.length() > 3)
		{
			if(!_AllowedList.contains(word.toLowerCase()))
			{
				InputStream result = null;
				BufferedReader reader = null;
				String line = null;
							
				try {
					result = new URL(_OnlineDictionaryURL.replace("<word>", word)).openStream();
					reader = new BufferedReader(new InputStreamReader(result));
					
					while ((line = reader.readLine()) != null){
						if(line.contains("<title>") && line.contains(word.toLowerCase()) && !line.contains("Did you spell it correctly"))
						{
							reader.close();
							result.close();
							return true;
						}
						if(line.contains("</head>"))
						{
							reader.close();
							result.close();
							return false;
						}
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
			}else{
				return true;
			}
		}else
			return false;
	}
	
	
	
	public static void addCensoredWord(String word)
	{
		_CensorList.add(word);
	}
	
	public static void addHappyWord(String word)
	{
		_HappyList.add(word);
	}
	
	public static void addAllowedWord(String word)
	{
		_AllowedList.add(word);
	}
	
	public static void removeCensoredWord(String word)
	{
		_CensorList.remove(word);
	}
	
	public static void removeHappyWord(String word)
	{
		_HappyList.remove(word);
	}
	
	public static void removeAllowedWord(String word)
	{
		_AllowedList.remove(word);
	}
	
	public static void setCensoredWords(HashSet<String> censoredwords)
	{
		_CensorList.addAll(censoredwords);
	}
	
	public static void setHappyWords(HashSet<String> happywords)
	{
		_HappyList.addAll(happywords);
	}
	
	public static void setAllowedWords(HashSet<String> allowedwords)
	{
		_AllowedList.addAll(allowedwords);
	}
	
	public static void setCensoredWords(String[] censoredwords)
	{
		for(String word: censoredwords)
		{
			_CensorList.add(word);
		}
	}
	
	public static void setHappyWords(String[] happywords)
	{
		for(String word: happywords)
		{
			_HappyList.add(word);
		}
	}
	
	public static void setAllowedWords(String[] allowedwords)
	{
		for(String word: allowedwords)
		{
			_AllowedList.add(word);
		}
	}
	
	public static void setCensoredWords(Object[] censoredwords)
	{
		for(Object word: censoredwords)
		{
			_CensorList.add((String) word);
		}
	}
	
	public static void setHappyWords(Object[] happywords)
	{
		for(Object word: happywords)
		{
			_HappyList.add((String) word);
		}
	}
	
	public static void setAllowedWords(Object[] allowedwords)
	{
		for(Object word: allowedwords)
		{
			_AllowedList.add((String) word);
		}
	}
	
	public static void setReplacementChar(char c)
	{
		_ReplacementChar = c;
	}
	
	public static HashSet<String> getCensoredWords()
	{
		return _CensorList;
	}
	
	public static HashSet<String> getHappyWords()
	{
		return _HappyList;
	}
	
	public static HashSet<String> getAllowedWords()
	{
		return _AllowedList;
	}
	
	public static void setVerifyWordOnline(boolean b)
	{
		_VerifyWordOnline = b;
	}
	
	/*public static void setIgnoreSpecialCharacters(boolean b)
	{
		_IgnoreSpecialCharacters = b;
	}
	
	public static void setIgnoreSpaces(boolean b)
	{
		_IgnoreSpaces = b;
	}*/
	
	public static void setReplaceWithHappyWords(boolean b)
	{
		_ReplaceWithHappyWords = b;
	}
	
	public static void setOnlineDictionary(String s)
	{
		_OnlineDictionaryURL = s;
	}
	
	public static void setUseRandomChar(boolean b)
	{
		_RandomReplacementChar = b;
	}
	
	public static boolean getVerifyWordOnline()
	{
		return _VerifyWordOnline; 
	}
		
	/*public static boolean getIgnoreSpecialCharacters()
	{
		return _IgnoreSpecialCharacters;
	}
	
	public static boolean getIgnoreSpaces()
	{
		return _IgnoreSpaces;
	}*/

	public static boolean getReplaceWithHappyWords()
	{
		return _ReplaceWithHappyWords;
	}
	
	public static String getOnlineDictionary()
	{
		return _OnlineDictionaryURL;
	}
	
	public static char getReplacementChar()
	{
		return _ReplacementChar;
	}
	
	public static boolean getUseRandomChar()
	{
		return _RandomReplacementChar;
	}
}
