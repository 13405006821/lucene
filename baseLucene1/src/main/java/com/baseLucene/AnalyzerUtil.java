package com.baseLucene;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 分词器
 * @author Liup
 *
 */
public class AnalyzerUtil {

	public static void displayToken(String str, Analyzer analyzer){
		try {
			TokenStream stream = analyzer.tokenStream("content", new StringReader(str));
			CharTermAttribute attribute = stream.addAttribute(CharTermAttribute.class);
			while(stream.incrementToken()){
				System.out.print("[" + attribute + "]");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}