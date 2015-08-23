package com.baseLucene;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexUtil {
	
	private static String[] ids = {"1", "2", "3", "4", "5", "6"};
	private static String[] emails = {"aa1@161.com", "aa2@162.com", "aa3@163.com", 
		"aa4@164.com", "aa5@165.com", "aa6@166.com"};
	private static String[] contents = {"hello liupeng", "hello zhangshan", "hello lisi",
		"hello wangwu", "hello zhaoliu", "hello heqi"};
	private static int[] attaches = {3, 4, 5, 8, 9, 10};
	private static Date[] dates = null;
	private static String[] names = {"hesutin", "liupeng", "zhangyu", "wangwu", "lisi", "zhangsan"};
	private static Map<String, Float> scores = new HashMap<String, Float>();
	
	static{
		scores.put("161.com", 1.5f);
		scores.put("163.com", 2.0f);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dates = new Date[ids.length];
		try {
			dates[0] = dateFormat.parse("2011-10-11");
			dates[1] = dateFormat.parse("2012-10-11");
			dates[2] = dateFormat.parse("2013-10-11");
			dates[3] = dateFormat.parse("2014-10-11");
			dates[4] = dateFormat.parse("2015-10-11");
			dates[5] = dateFormat.parse("2016-10-11");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建索引
	 */
	public static void index(boolean isBoost){
		IndexWriter writer = null;
		try {
			// 1 创建Directory
			// Directory directory = new RAMDirectory();// 建立内存中
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			// 2 创建IndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			writer = new IndexWriter(directory, iwc);
			// 3 创建Document
			Document document = null;
			String et = null;
			// 4 为Document添加Field
			for(int i = 0; i < ids.length; i ++){
				document = new Document();
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("email", emails[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("content", contents[i], Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
				document.add(new Field("name", names[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new NumericField("attach", Field.Store.YES, true).setIntValue(attaches[i]));
				document.add(new NumericField("date", Field.Store.YES, true).setLongValue(dates[i].getTime()));
				if(isBoost){
					et = emails[i].substring(emails[i].lastIndexOf("@") + 1);
					if(scores.containsKey(et)){
						document.setBoost(scores.get(et));
					}else{
						document.setBoost(0.5f);
					}
				}
				writer.addDocument(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null){
				try {
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void query(){
		IndexReader reader = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			reader = IndexReader.open(directory);
			System.out.println("numDocs: " + reader.numDocs());
			System.out.println("maxDoc: " + reader.maxDoc());
			System.out.println("numDeletedDocs: " + reader.numDeletedDocs());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 删除索引
	public static void delete(){
		IndexWriter wirter = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			wirter = new IndexWriter(directory, iwc);
			//wirter.deleteAll();
			// 精确删除
			wirter.deleteDocuments(new Term("id", "1"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(wirter != null){
				try {
					wirter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// 删除索引
	public static void deleteAll(){
		IndexWriter wirter = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			wirter = new IndexWriter(directory, iwc);
			wirter.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(wirter != null){
				try {
					wirter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	// 恢复索引
	public static void undelete(){
		IndexReader reader = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			reader = IndexReader.open(directory, false);
			reader.undeleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void forceMerge(){
		IndexWriter wirter = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			wirter = new IndexWriter(directory, iwc);
			wirter.forceMergeDeletes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(wirter != null){
				try {
					wirter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void update(){
		IndexWriter wirter = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			wirter = new IndexWriter(directory, iwc);
			Document document = new Document();
			document.add(new Field("id", "11", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			document.add(new Field("email", emails[0], Field.Store.YES, Field.Index.NOT_ANALYZED));
			document.add(new Field("content", contents[0], Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
			document.add(new Field("name", names[0], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			wirter.updateDocument(new Term("id", "1"), document);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(wirter != null){
				try {
					wirter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * term搜索
	 */
	@SuppressWarnings("resource")
	public static void termQuery(){
		IndexReader reader = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TermQuery query = new TermQuery(new Term("content", "hello"));
			TopDocs docs = searcher.search(query, 40);
			for(ScoreDoc sd : docs.scoreDocs){
				Document document = searcher.doc(sd.doc);
				System.out.println("(" + sd.doc + ")"+document.get("name") + "[" + document.get("email") + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * query搜索
	 */
	@SuppressWarnings("resource")
	public static void querySearcher(){
		try {
			// 1 创建Directory
			// Directory directory = new RAMDirectory();// 建立内存中
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			// 2 创建IndexReader
			IndexReader reader = IndexReader.open(directory);
			// 3 根据IndexReader创建IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			// 4 创建搜索QueryParser和Query
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse("hello");
			// 5 创建searcher搜索并返回TopDocs
			TopDocs topDocs = searcher.search(query, 10);
			// 6 根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds = topDocs.scoreDocs;
			for(ScoreDoc sd : sds){
				// 7 根据searcher和ScoreDoc获取具体Document对象
				Document document = searcher.doc(sd.doc);
				// 8 根据Document对象获取需要的值
				System.out.println("(" + sd.doc + ")"+document.get("name") + "[" + document.get("email") + "]");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页搜索
	 * @param page
	 * @param pageSize
	 */
	@SuppressWarnings("resource")
	public static void querySearcherPageByAfter(int page, int pageSize){
		try {
			// 1 创建Directory
			// Directory directory = new RAMDirectory();// 建立内存中
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			// 2 创建IndexReader
			IndexReader reader = IndexReader.open(directory);
			// 3 根据IndexReader创建IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			// 4 创建搜索QueryParser和Query
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse("hello");
			// 5 创建searcher搜索并返回TopDocs
			TopDocs topDocs = searcher.search(query, pageSize * page);
			// 6 根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds = topDocs.scoreDocs;
			ScoreDoc lastDoc = pageSize * (page - 1) - 1 > 0 ? sds[pageSize * (page - 1) - 1] : null;
			topDocs = searcher.searchAfter(lastDoc, query, pageSize);
			for(ScoreDoc sd : topDocs.scoreDocs){
				// 7 根据searcher和ScoreDoc获取具体Document对象
				Document document = searcher.doc(sd.doc);
				// 8 根据Document对象获取需要的值
				System.out.println("(" + sd.doc + ")"+document.get("name") + "[" + document.get("email") + "]");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
