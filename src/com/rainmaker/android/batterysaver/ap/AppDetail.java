package com.rainmaker.android.batterysaver.ap;

public class AppDetail {

	String _name;
	String _package;
	
	public AppDetail(String Name,String pack){
		this._name=Name;
		this._package=pack;
	}
	
	public String getName()
	{
		return this._name;
	}
	
	public String getPackage()
	{
		return this._package;
	}
	
	public void setName(String Name)
	{
		this._name=Name;
	}
	
	public void setPackage(String pack)
	{
		this._package=pack;
	}
}
