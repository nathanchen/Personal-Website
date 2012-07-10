package com.nathanchen.utils;


public class StringParser
{
	public String removeHTMLTags(String rawString)
	{
		rawString = rawString.replaceAll("<[^>]+>", " ");
		return rawString;
	}

	// public static void main(String args[])
	// {
	// String str = "<p class=\"p1\">The model has a " +
	// "central position in a Play! application. It is the domain-specific representation of the "
	// +
	// "information on which the application operates.</p><p class=\"p1\"><strong>Martin fowler "
	// +
	// "defines it as:</strong></p><p class=\"p1\">Responsible for representing concepts of the business, "
	// +
	// "information about the business situation, and business rules. " +
	// "State that reflects the business situation is controlled and used here, "
	// +
	// "even though the technical details of storing it are delegated to the infrastructure. "
	// +
	// "This layer is the heart of business software.<br /></p><p><br /></p>";
	// StringParser stringParser = new StringParser();
	// System.out.println(stringParser.removeHTMLTags(str));
	//
	// }
}
