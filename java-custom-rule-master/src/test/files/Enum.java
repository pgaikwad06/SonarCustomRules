import java.util.ArrayList;
import java.util.HashMap;

import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.google.common.collect.ImmutableMap;

public class Enum {
	
	//Map<String,String> str = new HashMap<String,String>("test","test1");
	
	public void methodTest1() {
		//int i=0;
	//	ArrayList<String> al = new ArrayList<String>();
		
		Map<String, String> immutableMap =  ImmutableMap.of("United States", "Washington D.C.");// Noncompliant {{Use Identifier type ImmutableMap.}}
		
	}

	public void methodTest2() {
		
		ImmutableMap<String, String> immutableMap =  ImmutableMap.of("United States", "Washington D.C.");
		
	}

}
