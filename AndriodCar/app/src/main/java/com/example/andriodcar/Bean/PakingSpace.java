package com.example.andriodcar.Bean;

/**
 * ͣ��λ��
 * @author ����
 * @date 2020/2/12
 */
public class PakingSpace {
	//停车位id
	private int parkingId;
	//停车位编号
	private String parkSpaceNum;
	//用户电话
	private String phone_number;
	//所属小区id
	private int residentialQuarId;
	//状态
	private int state;
	//空闲开始时间
	private String startYime;
	//空闲结束时间
	private String endingTime;
	//前三小时
	private String firstThreeTime;
	//三到六小时
	private String threeAndSixTime;
	//六小时以上
	private String sixAndTwenty;
	//每小时收费
	private int Price;
	public int getParkingId() {
		return parkingId;
	}
	public void setParkingId(int parkingId) {
		this.parkingId = parkingId;
	}
	public String getParkSpaceNum() {
		return parkSpaceNum;
	}
	public void setParkSpaceNum(String parkSpaceNum) {
		this.parkSpaceNum = parkSpaceNum;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStartYime() {
		return startYime;
	}
	public void setStartYime(String startYime) {
		this.startYime = startYime;
	}
	public String getEndingTime() {
		return endingTime;
	}
	public void setEndingTime(String endingTime) {
		this.endingTime = endingTime;
	}
	public String getFirstThreeTime() {
		return firstThreeTime;
	}
	public void setFirstThreeTime(String firstThreeTime) {
		this.firstThreeTime = firstThreeTime;
	}
	public String getThreeAndSixTime() {
		return threeAndSixTime;
	}
	public void setThreeAndSixTime(String threeAndSixTime) {
		this.threeAndSixTime = threeAndSixTime;
	}
	public String getSixAndTwenty() {
		return sixAndTwenty;
	}
	public void setSixAndTwenty(String sixAndTwenty) {
		this.sixAndTwenty = sixAndTwenty;
	}


	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public int getResidentialQuarId() {
		return residentialQuarId;
	}

	public void setResidentialQuarId(int residentialQuarId) {
		this.residentialQuarId = residentialQuarId;
	}
}
