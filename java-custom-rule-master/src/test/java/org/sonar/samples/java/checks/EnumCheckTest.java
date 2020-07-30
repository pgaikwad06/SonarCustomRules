package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class EnumCheckTest {

	@Test
	public void verify() {
		JavaCheckVerifier.newVerifier().onFile("src/test/files/Enum.java")
		.withCheck(new EnumRule())
		.verifyIssues();
	}
}
