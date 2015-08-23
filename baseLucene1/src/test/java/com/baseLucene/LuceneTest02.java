package com.baseLucene;

import org.junit.Test;

public class LuceneTest02 {
	
	@Test
	public void index(){
		IndexUtil.index(true);
	}
	
	@Test
	public void query(){
		IndexUtil.query();
	}
	
	@Test
	public void delete(){
		IndexUtil.delete();
	}
	
	@Test
	public void deleteAll(){
		IndexUtil.deleteAll();
	}
	
	@Test
	public void undelete(){
		IndexUtil.undelete();
	}
}
