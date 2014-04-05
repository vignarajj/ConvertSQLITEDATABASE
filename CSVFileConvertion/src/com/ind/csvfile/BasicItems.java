package com.ind.csvfile;

public class BasicItems {
	int _id;
	String name, age, address, bloodGroup;

	public BasicItems() {

	}
	public BasicItems(String name, String age, String address, String bloodGroup)
	{
		this.name=  name;
		this.age = age;
		this.address = address;
		this.bloodGroup = bloodGroup;
	}
	public BasicItems(int _id, String name, String age, String address, String bloodGroup)
	{
		this._id = _id;
		this.name=  name;
		this.age = age;
		this.address = address;
		this.bloodGroup = bloodGroup;
	}
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
}
