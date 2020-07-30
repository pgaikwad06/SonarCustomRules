package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class TOVariableNamingRuleTest {
	@Test
	public void verify() {
		
		  JavaCheckVerifier.newVerifier().onFile("src/test/files/TOVariableNaming.java")
		  .withCheck(new TOVariableNamingRule()).verifyIssues();
		 
	}
}
