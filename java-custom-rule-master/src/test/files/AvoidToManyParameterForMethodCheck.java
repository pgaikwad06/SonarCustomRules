package com.sonarcube.noncompliance.code;

public class AvoidToManyParameterForMethodCheck {
	public void methodTest1(int param1, int param2, int param3, int param4, int param5) { // Noncompliant {{Too Many
																							// Parameters}}
		System.out.println(param1 + param2 + param3 + param4 + param5);
	}

	public void methodTest2(int param1, float param2, int param3, int param4) {
		System.out.println(param1 + param2 + param3 + param4);
	}
}
