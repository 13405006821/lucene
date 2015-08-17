package com.baseLucene;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest1Util {
	
	/**
	 * 创建索引
	 */
	public static void index(){
		IndexWriter wirter = null;
		try {
			// 1 创建Directory
			// Directory directory = new RAMDirectory();// 建立内存中
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index01")); // 创建在硬盘上
			// 2 创建IndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			wirter = new IndexWriter(directory, iwc);
			// 3 创建Document
			Document document = null;
			// 4 为Document添加Field
			File f = new File("d:/baseLucene/file01");
			for(File file : f.listFiles()){
				document = new Document();
				System.out.println(FileUtils.readFileToString(file));
				document.add(new Field("content", new FileReader(file)));
				document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
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
	
	/**
	 * 创建索引
	 */
	@SuppressWarnings("resource")
	public static void searcher(){
		try {
			// 1 创建Directory
			// Directory directory = new RAMDirectory();// 建立内存中
			Directory directory = FSDirectory.open(new File("d:/baseLucene/index01")); // 创建在硬盘上
			// 2 创建IndexReader
			IndexReader reader = IndexReader.open(directory);
			// 3 根据IndexReader创建IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			// 4 创建搜索QueryParser和Query
			QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
			Query query = parser.parse("22");
			// 5 创建searcher搜索并返回TopDocs
			TopDocs topDocs = searcher.search(query, 10);
			// 6 根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds = topDocs.scoreDocs;
			for(ScoreDoc sd : sds){
				// 7 根据searcher和ScoreDoc获取具体Document对象
				Document document = searcher.doc(sd.doc);
				// 8 根据Document对象获取需要的值
				System.out.println(document.get("filename") + "[" + document.get("path") + "]");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
