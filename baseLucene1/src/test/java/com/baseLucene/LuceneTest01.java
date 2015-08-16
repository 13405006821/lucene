package com.baseLucene;

import org.junit.Test;

public class LuceneTest01 {
	
	@Test
	public void index(){
		LuceneTest1Util.index();
	}
	
	@Test
	public void searcher(){
		LuceneTest1Util.searcher();
	}
}
