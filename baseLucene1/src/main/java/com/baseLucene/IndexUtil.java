package com.baseLucene;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
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
	private static String[] contents = {"cssccscscscscsc", "asdadasdadasd", "ryryrytryrtyrtyry",
		"hjkhkhkhjkhkhj", "iiiiiiiiiiii", "ooooooooooo"};
	// private static int[] attaches = {3, 4, 5, 8, 9, 10};
	private static String[] names = {"hesutin", "liupeng", "zhangyu", "wangwu", "lisi", "zhangsan"};
	private static Map<String, Float> scores = new HashMap<String, Float>();
	
	public IndexUtil(){
		scores.put("161.com", 1.5f);
		scores.put("163.com", 2.0f);
	}
	
	/**
	 * 创建索引
	 */
	public static void index(boolean isBoost){
		IndexWriter wirter = null;
		try {
			// 1 创建Directory
			// Directory directory = new RAMDirectory();// 建立内存中
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			// 2 创建IndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			wirter = new IndexWriter(directory, iwc);
			// 3 创建Document
			Document document = null;
			String et = null;
			// 4 为Document添加Field
			for(int i = 0; i < ids.length; i ++){
				document = new Document();
				document.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				document.add(new Field("email", emails[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("content", contents[i], Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
				document.add(new Field("content", contents[i], Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
				document.add(new Field("name", names[i], Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
				if(isBoost){
					et = emails[i].substring(emails[i].lastIndexOf("@") + 1);
					if(scores.containsKey(et)){
						document.setBoost(scores.get(et));
					}else{
						document.setBoost(0.5f);
					}
				}
				wirter.addDocument(document);
			}
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
	 * 搜索
	 */
	@SuppressWarnings("resource")
	public static void search(){
		IndexReader reader = null;
		try {
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index02")); // 创建在硬盘上
			reader = IndexReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(reader);
			TermQuery query = new TermQuery(new Term("content", "oooooo"));
			TopDocs docs = searcher.search(query, 10);
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
}
