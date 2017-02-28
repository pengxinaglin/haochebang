package com.haoche51.checker.test;

import android.test.AndroidTestCase;

public class TestAppServer extends AndroidTestCase {
	/*
	public void testLogin() {
		HttpResponse response = AppServer.login("xuhaibo", "123");
		assertEquals(0, response.getCode());
		Checker checker = AppServer.parseLoginResult(response.getMsg());
		assertEquals(7, checker.getCheckerId());
		assertEquals("�캣��", checker.getCheckerName());
		assertEquals("������", checker.getCheckerDuty());
	}
	
	public void testGetCheckTask() {
		HttpResponse response = AppServer.getCheckTask(154);
		assertEquals(0, response.getCode());
		CheckTaskEntity checkerTask = AppServer.parseGetCheckTaskResult(response.getMsg());
		assertEquals(154, checkerTask.getId());
		assertEquals(5344, checkerTask.getVehicleSourceId());
		assertEquals(7, checkerTask.getCheckerId());
		assertEquals("�캣��", checkerTask.getChecker());
		assertEquals("������", checkerTask.getSellerName());
		assertEquals("15230537670", checkerTask.getSellerPhone());
		assertEquals(1404370800, checkerTask.getReserveTime());
		assertEquals(1404378000, checkerTask.getFinishTime());
		assertEquals("�����", checkerTask.getAddress());
		assertEquals(2, checkerTask.getStatus());
	}
	
	public void testGetViewTask() {
		HttpResponse response = AppServer.getViewTask(35);
		assertEquals(0, response.getCode());
		ViewTaskEntity viewTask = AppServer.parseGetViewTaskResult(response.getMsg());
		assertEquals(35, viewTask.getId());
		assertEquals("���ǵ�F3 2013�� 1.5L �Զ� ��׼��", viewTask.getCarBrand());
		assertEquals("15230537670", viewTask.getSellerPhone());
		assertEquals("������", viewTask.getSellerName());
		assertEquals("13810386280", viewTask.getBuyerPhone());
		assertEquals("������", viewTask.getBuyerName());
		assertEquals(1404432000, viewTask.getReserveTime());
		assertEquals("��ƽ ����� �����", viewTask.getAddress());
	}
	
	public void testGetCheckTaskList() {
		HttpResponse response = AppServer.getCheckTaskList(7);
		assertEquals(0, response.getCode());
		List<CheckTaskEntity> checkTaskList = AppServer.parseGetCheckTaskListResult(response.getMsg());
		assertEquals(3, checkTaskList.size());
		CheckTaskEntity checkTask = checkTaskList.get(0);
		assertEquals(156, checkTask.getId());
		checkTask = checkTaskList.get(1);
		assertEquals(155, checkTask.getId());
		checkTask = checkTaskList.get(2);
		assertEquals(154, checkTask.getId());
	}
	
	public void testGetViewTaskList() {
		HttpResponse response = AppServer.getViewTaskList(4);
		assertEquals(0, response.getCode());
		List<ViewTaskEntity> viewTaskList = AppServer.parseGetViewTaskListResult(response.getMsg());
		assertEquals(1, viewTaskList.size());
		ViewTaskEntity viewTask = viewTaskList.get(0);
		assertEquals(35, viewTask.getId());
	}
	
	public void testGetCheckReport() {
		HttpResponse response = AppServer.getCheckReport(156);
		assertEquals(0, response.getCode());
		CheckReportEntity checkReport = AppServer.parseGetCheckReportResult(response.getMsg());
		assertEquals(28, checkReport.getId_onserver());
		assertEquals(0, checkReport.getId());
	}
	
	public void testGetVehicleBrandData() {
		HttpResponse response = AppServer.getVehicleBrandData();
		assertEquals(0, response.getCode());
		Map<Integer, List<? extends BaseEntity>> map = AppServer.parseGetVehicleBrandDataResult(response.getMsg());
		assertEquals(3, map.size());
		assertEquals(147, map.get(0).size());
		assertEquals(88, map.get(1).size());
		assertEquals(1504, map.get(2).size());
	}
	
	public void testGetVehicleCount() {
		HttpResponse response = AppServer.getVehicleCount();
		assertEquals(0, response.getCode());
		int count = AppServer.parseGetVehicleCountResult(response.getMsg());
		assertEquals(23634, count);
	}
	
	public void testGetVehicleList() {
		HttpResponse response = AppServer.getVehicleList(0, 10);
		assertEquals(0, response.getCode());
		List<VehicleEntity> vehicleList = AppServer.parseGetVehicleListResult(response.getMsg());
		assertEquals(10, vehicleList.size());
	}
	*/
	
	public void testSendCheckReport() {
	/*	CheckReportEntity entity = new CheckReportEntity();
		entity.setId(1);
		entity.setCheck_appointment_id(10000);
		entity.setVehicle_source_id(250);
		entity.setV_class(849);
		entity.setBrand_name("�µ�");
		entity.setBank_name("�й�������");
		HttpResponse response = AppServer.sendCheckReport(entity);
		assertEquals(0, response.getCode());*/
	}
	
	/*
	public void testBind() {
		HttpResponse response = AppServer.bind(7, "111", "222");
		assertEquals(0, response.getCode());
		pushId = AppServer.parseBindResult(response.getMsg());
		assertTrue(pushId > 0);
		response = AppServer.unbind(pushId);
		assertEquals(0, response.getCode());
	}
	*/
}
