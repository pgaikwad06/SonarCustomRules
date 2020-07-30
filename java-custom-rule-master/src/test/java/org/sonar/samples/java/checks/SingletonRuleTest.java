package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class SingletonRuleTest {
	@Test
	public void verify() {
		
		  JavaCheckVerifier.newVerifier().onFile("src/test/files/Singleton.java")
		  .withCheck(new SingletonRule()).verifyNoIssues();
		 
	}
}
