package com.bme.ecgidentification.BmobTest;

import cn.bmob.v3.BmobObject;

/** javaBean
  * @ClassName: Person
  * @Description: TODO
  * @author smile
  * @date 2014-5-20 4:12:55
  */
public class Person extends BmobObject {
	
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}