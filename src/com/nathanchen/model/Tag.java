package com.nathanchen.model;

public class Tag implements Comparable<Tag> 
{
	private String tagName;
	private int number;
	
	public Tag()
	{
		
	}
	
	public Tag(String tagName)
	{
		this.tagName = tagName;
	}
	
	public Tag(String tagName, int number)
	{
		this.tagName = tagName;
		this.number = number;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public int compareTo(Tag otherTag)
	{
		return tagName.compareTo(otherTag.tagName);
	}
}
