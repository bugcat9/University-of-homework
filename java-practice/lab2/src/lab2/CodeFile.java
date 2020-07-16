package lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;



/***
 * 
 * @author zhouning
 *denominator 是分母平方和
 *filename是文件名字
 *frequency是Map类型，String代表单词，Integer代表次数
 */
public class CodeFile {
	 Map<String,Integer > frequency;
	 double denominator=0;
	 String filename;
	 
	 /***
	  * 
	  * @param urlString 代表文件名字
	  * 初始化，会计算分母
	  */
		public CodeFile(String urlString ) {
			filename=urlString;
			String word;
			frequency=new HashMap<>();
			Scanner sc;
			try {
				sc = new Scanner(new FileReader(urlString));
		
			while(sc.hasNext())
			{	
				word=sc.next();
				if (frequency.containsKey(word))
				{
					int number=	frequency.get(word)+1;
					frequency.put(word, number);
				}else
					frequency.put(word, 1);
			}
			sc.close();
			sc=null;
			
			//遍历计算分母和
			Iterator<Entry<String, Integer>> iter = frequency.entrySet().iterator();
			while (iter.hasNext()){
				Entry<String, Integer> entry =  iter.next();
				int val = (int) entry.getValue();
				denominator+=val*val;	
			}
			//System.out.println(denominator);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		//返回余弦相似性
		public double cosineSimilarity(CodeFile file) {
				double similarity =0.0;
				double molecular=0.0;
				
				Iterator<Entry<String, Integer>> iter = frequency.entrySet().iterator();
				
				while (iter.hasNext()){
					Entry<String, Integer> entry =  iter.next();
					int val =  entry.getValue();
					String word= entry.getKey();
					
					//计算分子
					if(file.frequency.containsKey(word)) {
						molecular+=val*file.frequency.get(word);
					}
					
				}
				//System.out.println(molecular);
				similarity=molecular/(Math.sqrt(this.denominator)*Math.sqrt(file.denominator));
				return similarity;
		}
		
		
		
		
		@Override
		public String toString() {
			return this.filename;
		}
		
		
		public  static void main(String args[]) throws FileNotFoundException
		{
			
			CodeFile f1=new CodeFile("sample1.code");
			CodeFile f2=new CodeFile("sample2.code");
			System.out.println(f1.cosineSimilarity(f2));
		}
		
}
