package com.baseLucene;

import org.junit.Test;

public class LuceneTest03 {
	
	@Test
	public void search(){
		SearcherUtil.termQuery();
	}
	
	@Test
	public void searchPageByAfter(){
		SearcherUtil.querySearcherPageByAfter(2, 10);
	}
}
