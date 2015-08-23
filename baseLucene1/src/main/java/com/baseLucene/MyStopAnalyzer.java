package com.baseLucene;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

public class MyStopAnalyzer extends Analyzer{
	
	private Set<Object> stops = null;
	
	public MyStopAnalyzer(String[] arr){
		System.out.println(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		stops = StopFilter.makeStopSet(Version.LUCENE_35, arr, true);
		stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new StopFilter(Version.LUCENE_35, 
				new LowerCaseFilter(Version.LUCENE_35, 
				new LetterTokenizer(Version.LUCENE_35, reader)), stops);
	}
	

}
