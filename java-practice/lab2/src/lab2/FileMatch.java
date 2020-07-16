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

public class FileMatch implements FileClosestCodeMatch{
	CodeFile [] myfiles=null;
	double maxsimilarity=0.0;
	int index1=0;
	int index2=0;
	
	public FileMatch(String file) {
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
	
	/**
	 * 找出最匹配的两个文件
	 */
	public void ClosestCodeMatch () {
		for(int i=0;i<myfiles.length;i++) {
			for(int j=i+1;j<myfiles.length;j++) {
					if(myfiles[i].cosineSimilarity(myfiles[j])>maxsimilarity) {
						maxsimilarity=myfiles[i].cosineSimilarity(myfiles[j]);
						index1=i;index2=j;
					}
			}
		}
	}
	
	public void print() {
		
		System.out.print(myfiles[index1].filename+"----"+myfiles[index2].filename+":");
		System.out.print(maxsimilarity);
	}
	
	
	public  static void main(String args[]) throws FileNotFoundException{
		FileMatch f=new FileMatch("1.txt");
		f.ClosestCodeMatch();
		f.print();
	}
	
}
