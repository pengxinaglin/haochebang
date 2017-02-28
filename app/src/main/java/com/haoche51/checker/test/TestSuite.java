package com.haoche51.checker.test;



public class TestSuite extends junit.framework.TestSuite {
	public TestSuite() {
		addTestSuite(TestAppServer.class);
		//addTestSuite(TestVehicleService.class);
	}
}
