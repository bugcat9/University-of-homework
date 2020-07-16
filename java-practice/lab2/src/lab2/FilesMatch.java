package lab2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lab2.CodeFile;



/***
 * 
 * @author zhouning
 *实现接口FileClosestCodeMatch
 */
public class FilesMatch implements FileClosestCodeMatch{
	CodeFile [] myfiles=null;
	double maxsimilarity=0.0;
	Map<Integer,String >similarityfiles=new HashMap<>() ;
	Map<Integer,Double>similarityes=new HashMap<>();
	
	/***
	 * 
	 * @param file代表文件名，用于输入，一般为txt文本输入
	 */
	public FilesMatch(String file) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("1.txt")),
			        "UTF-8"));
			
			String lineTxt = null;
			int index=-1;
            while ((lineTxt = br.readLine()) != null) {
                	System.out.println(lineTxt);
                	
                	if(index==-1) {
                		int num=Integer.valueOf(lineTxt);
                		myfiles=new CodeFile[num];
                	}
                	else
                	{
                		myfiles[index]=new CodeFile(lineTxt);
                	}
                	index++;
                }		
            
            for(int i=0;i<myfiles.length;i++)
            {
            	similarityfiles.put(i, null);
            	similarityes.put(i, 0.0);
            }
            
         //System.out.println(myfiles[0].cosineSimilarity(myfiles[1]));
            
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * 
	 * @param files，存储文件名
	 * 
	 */
	public FilesMatch(ArrayList<String> files) {
		int num=files.size();
		myfiles=new CodeFile[num];
		
		for(int i=0;i<num;i++)
		{
			myfiles[i]=new CodeFile(files.get(i));
		}
		
		  for(int i=0;i<myfiles.length;i++)
          {
          	similarityfiles.put(i, null);
          	similarityes.put(i, 0.0);
          }
	}
	
	
	
	
	
	/***
	 * 进行文件最匹配选择
	 */
	public void ClosestCodeMatch ()
	{
		
		for(int i=0;i<myfiles.length;i++) {
			for(int j=i+1;j<myfiles.length;j++) {
				double  Similarity=myfiles[i].cosineSimilarity(myfiles[j]);
				if(Similarity>similarityes.get(i))
				{
					similarityes.put(i, Similarity);
					similarityfiles.put(i, myfiles[j].filename);
				}
				
				if(Similarity>similarityes.get(j))
				{
					similarityes.put(j, Similarity);
					similarityfiles.put(j, myfiles[i].filename);
				}
						
			}
		}
	}

	
	/***
	 * 用于显示
	 */
	public void print()
	{
		for(int i=0;i<myfiles.length;i++)
		{	
			//System.out.print(myfiles[i].similarity);
			System.out.print(myfiles[i].filename+"----"+similarityfiles.get(i)+":");
			System.out.print(similarityes.get(i));
			System.out.println();
		}
	}
	
	
	
	public  static void main(String args[]) throws FileNotFoundException
	{
		FilesMatch a=new FilesMatch("1.txt");
		a.ClosestCodeMatch();
		a.print();
		
	}
}
