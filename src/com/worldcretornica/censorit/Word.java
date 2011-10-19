package com.worldcretornica.censorit;

public class Word
{
	public String Text = "";
	public boolean Verified = false;
	public boolean Curse = false;
	public Word(){}
	
	public Word clone()
	{
		Word T = new Word();
		T.Text = this.Text;
		T.Curse = this.Curse;
		T.Verified = this.Verified;
		
		return T;
	}
}
