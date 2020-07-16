package lab2_Test;

import static org.junit.Assert.*;
import org.junit.Test;

import lab2.CodeFile;


public class CodeFileTest {
	/***
	 * 测试CodeFile中cosineSimilarity
	 */
		@Test
		public void cosineSimilarityTest() {
			CodeFile f1=new CodeFile("sample1.code");
			CodeFile f2=new CodeFile("sample2.code");
			assertEquals(0.939040909148062,f1.cosineSimilarity(f2),0.001);
		}
}
