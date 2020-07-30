package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class JAFClassNamingRuleTest {
	@Test
	public void verify() {
		
		  JavaCheckVerifier.newVerifier().onFile("src/test/files/test.java")
		  .withCheck(new JAFClassNamingRule()).verifyIssues();
		 
	}
}
