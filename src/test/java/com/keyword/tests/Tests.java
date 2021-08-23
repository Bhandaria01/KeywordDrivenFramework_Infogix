
// test execution

package com.keyword.tests;

import org.testng.annotations.Test;

import com.keyword.engine.Engine;

public class Tests {

	public Engine engine;
	
	@Test
	public void Test1() { // method for first testcase

	 engine = new Engine();
	 engine.startExecution("TestCase1");
		
	}
	
	@Test
	public void Test2() {		// method for second testcase

	 engine = new Engine();
	 engine.startExecution("TestCase2");
		
	}

}
