package com.baseLucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

public class LuceneTest04 {
	
	@Test
	public void displayToken(){
		String str = "i am liupeng,i am from yancheng,i like java very much"
				+ ",my email is liupeng@139.com,my QQ is 503387164,我是中国人";
		Analyzer a1 = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer a2 = new StopAnalyzer(Version.LUCENE_35);
		Analyzer a3 = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer a4 = new WhitespaceAnalyzer(Version.LUCENE_35);
		Analyzer a5 = new MyStopAnalyzer(new String[]{"i", "java"});
		// 中文分词器
		Analyzer a6 = new MMSegAnalyzer();
		AnalyzerUtil.displayToken(str, a1);
		AnalyzerUtil.displayToken(str, a2);
		AnalyzerUtil.displayToken(str, a3);
		AnalyzerUtil.displayToken(str, a4);
		AnalyzerUtil.displayToken(str, a5);
		AnalyzerUtil.displayToken(str, a6);
	}
}
