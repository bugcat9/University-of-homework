/**
 * 
 */
package lab2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/**
 * @author 20171001234 xxx
 *
 *假设在 codes/lab1/目录下存在以下结构的文件组织：
*├─Java课内实习-201710001234-xxx-实习1
*│  ├─Java课内实习-20171000123-xxx-实习1
*│  │  └─lab1_code
*│  │      ├─rules
*│  │      └─turtle
*│  └─lab1_code
*│      ├─rules
*│      └─turtle
*├─Java课内实习-20171001235-xxx-实习一
*│  └─lab1
*│      └─lab1_code
*│          └─lab1_code
*│              ├─bin
*│              │  ├─rules
*│              │  └─turtle
*│              ├─rules
*│              └─turtle
*├─Java课内实习-20171001236-xxxx-实习一
*│  ├─rules
*│  └─turtle
*└─Java课内实习20171001237-xxxx-实习一
*    └─Java课内实习20171001237-xxx-实习一
*       └─Java课内实习20171001237-xxxx-实习一
*            └─lab1_code
*               ├─123
*                ├─rules
*                │  └─bin
*               └─turtle
*                    └─bin
*
*/
public class LabClosestMatches {
		
	/**
	 * 用于评价各相关目录下，指定文件的相似性。
	 * Similarity   子目录1                        子目录2
	 * 100%        Java课内实习-201710001234-xxx-实习1     Java课内实习-201710001235-xxx-实习1
	 * 89%         Java课内实习-201710001234-xxx-实习1     Java课内实习-201710001236-xxx-实习1
	 * ....
	 * @param path 作业文件所在的目录，比如这里是：codes/lab1
	 * @param fileNameMatches：用来过滤进行比较的文件名的文件名或者正则表达式.
	 * 如 "DrawableTurtle.java"，"*.java","turtle/*.java"
	 * 如果一个子目录下有多个符合条件的文件，将多个文件合并成一个文件。
	 * 
	 * @param topRate:取值范围从[0,100],输出控制的阈值
	 * 	从高往低输出高于topRate%相似列表，如
	 * @param removeComments:是否移除注释内容	
	 * @throws Exception 
	 * @throws IOException 
	 * 	 */
	public static void closestCodes(String path, String fileNameMatches,double topRate,boolean removeComments) throws IOException, Exception
	{
		// 计算文件间的相似性，输出
		File file=new File(path);
		File[] files = file.listFiles();
		//获得子目录,以及生成的新的文件名
		ArrayList<String> subdirectory=new ArrayList<> ();
		ArrayList<String> newfilenames=new ArrayList<> ();
		Pattern pattern=Pattern.compile(fileNameMatches);
		
		//寻找子目录
		for (File f : files) {
            if(f.isDirectory())
            {
            	 subdirectory.add(f.getPath());
            	
                 ArrayList<String> resultFileName=new  ArrayList<String>();
                 getFilenames(f.getPath(),resultFileName,pattern);//寻找子目录下的文件
                 
                 String name=f.getPath()+"new.java";
                 System.out.println(name);
                 newfilenames.add(name);
                  mergerFiles(resultFileName,name,removeComments);//进行文件融合
            }
		}
		
		int num =subdirectory.size();
		CodeFile[] codeFiles=new CodeFile[num];
		for(int i=0;i<num;i++)
		{
			codeFiles[i]=new CodeFile(newfilenames.get(i));
		}
		
		Recordnode[] record=new Recordnode[(num*(num-1))/2];
		for(int i=0;i<(num*(num-1))/2;i++)
		{
			record[i]=new Recordnode();
		}
		
		int index=0;
		for(int i=0;i<num;i++)
		{
			for(int j=i+1;j<num;j++)
			{
				record[index].index1=i;
				record[index].index2=j;
				record[index].s=codeFiles[i].cosineSimilarity(codeFiles[j]);
				index++;
			}
		}
		
		//进行排序
		int maxindex=0;
		double maxs=0.0;
		for(int i=0;i<index;i++)
		{
			for(int j=i+1;j<index;j++)
			{
				if(record[j].s>maxs)
				{
					maxindex=j;
					maxs=record[j].s;
				}
			}
			Recordnode temp=record[i];
			record[i]=record[maxindex];
			record[maxindex]=temp;
		}
		
		//进行输出
		for(int i=0;i<index;i++)
		{
			if(record[i].s>topRate)
			{
				System.out.print(subdirectory.get(record[i].index1)+"-------"+subdirectory.get(record[i].index2)+"------");
				System.out.print(record[i].s);
				System.out.println();
			}
		}
		

	}
	/***
	 * 用于多个文件融合
	 * @param Files 各类文件名
	 * @param filename 融合后文件的名字
	 * @param removeComments	是否删除注释
	 * @throws IOException
	 * @throws Exception
	 */
	private static void mergerFiles (ArrayList<String> Files,String filename,boolean removeComments) throws IOException, Exception {
	//根据对应的编码格式读取 
		int count=0;
		for(String file:Files)
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			StringBuffer content = new StringBuffer(); String tmp = null;
			
			while ((tmp = reader.readLine()) != null)	{ 
				content.append(tmp);
				content.append("\n"); 
			}

			String s=null;
			if (removeComments==true)
			{
				String target = content.toString(); 
				s = target.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
			}else
				s=content.toString(); 
				
			//使用对应的编码格式输出,打开要以追加的方式打开
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename,true), "UTF-8")); out.write(s);
			out.flush(); out.close(); count++; 
		System.out.println("-----文件处理完成---"+count +"----"+file);
			reader.close();
		}
	
		
	}
	
/***
 * 用于获得目录下文件的路径，采用递归的方法
 * @param filename文件的根目录
 * @param resultFileName用于记录文件名
 * @param pattern用于筛选文件
 */
	 private static void getFilenames(String filename,ArrayList<String> resultFileName,Pattern  pattern){
		 	File file=new File(filename);
	        File[] files = file.listFiles();
	       
	        if(files==null)return ;// 判断目录下是不是空的
	        for (File f : files) {
	            if(f.isDirectory())
	            {
	            	// 判断是否文件夹
	            	getFilenames(f.getPath(),resultFileName, pattern);// 调用自身,查找子目录
	            }else {
	            	
	            	//判断是否匹配筛选条件
	            	boolean isMatch=pattern.matcher(f.getPath()).matches();
	    			if(isMatch==true)
	    				resultFileName.add(f.getPath());
	            }
	        }
	        return ;
	    }
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		  String path="lab1-codes-fortest"; String fileNameMatches=".*TurtleSoup.java"; 
		  double topRate=0.0; 
		  boolean removeComments=true;  
		  closestCodes(path,fileNameMatches,topRate,removeComments);		 
}

}


class  Recordnode{
	public	int index1=0;
	public	int index2=0;
	public	double s=0.0;
}

