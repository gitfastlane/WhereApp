package com.findmybaby.www.controller;

import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.FamilyDTO;
import com.findmybaby.www.dto.LocationDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class ServerController {
	final private String PARENT = "parent";
	final private String CHILD = "child";
	final private String FAMILY = "family";
	final private String LOCATION = "location";
	
	public String goService(String msg) {
		// msg = "parent,Login,userId,pw,phone,name"
		// msg = "child,Login,userId,pw,phone,name"
		// msg = "family,Insert,parent,child,confirm"
		// msg = "location,Insert,userId,latitude,longitude,heading"
		// return = ""
		
		String service = msg.substring(0, msg.indexOf(","));
		String result = null;
		
		if(service.equals(PARENT)) {
			ParentDTO dto = new ParentDTO();
			int len = service.length()+1;
			String serviceName = msg.substring(len, msg.indexOf(",", len));
			System.out.println("serviceName="+serviceName);
			len += serviceName.length()+1;
			String userId = msg.substring(len, msg.indexOf(",", len));
			System.out.println("userId="+userId);
			len += userId.length()+1;
			String pw = msg.substring(len, msg.indexOf(",", len));
			System.out.println("pw="+pw);
			len += pw.length()+1;
			String phone = msg.substring(len, msg.indexOf(",", len));
			System.out.println("phone="+phone);
			len += phone.length()+1;
			String name = msg.substring(len);
			System.out.println("name="+name);
			dto.setP_id(userId);
			dto.setP_pw(pw);
			dto.setP_phone(phone);
			dto.setP_name(name);
			System.out.println("serviceName: "+serviceName+", userId: "+userId+", pw: "+pw+", phone: "+phone+", name: "+name);
			try {
				result = ((Service)Class.forName("com.findmybaby.www.s_parent."+serviceName).newInstance()).execute(dto);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(service.equals(CHILD)) {
			ChildDTO dto = new ChildDTO();
			int len = service.length()+1;
			String serviceName = msg.substring(len, msg.indexOf(",", len));
			len += serviceName.length()+1;
			String userId = msg.substring(len, msg.indexOf(",", len));
			len += userId.length()+1;
			String pw = msg.substring(len, msg.indexOf(",", len));
			len += pw.length()+1;
			String phone = msg.substring(len, msg.indexOf(",", len));
			len += phone.length()+1;
			String name = msg.substring(len);
			dto.setC_id(userId);
			dto.setC_pw(pw);
			dto.setC_phone(phone);
			dto.setC_name(name);
			System.out.println("serviceName: "+serviceName+", userId: "+userId+", pw: "+pw+", phone: "+phone+", name: "+name);
			try {
				result = ((Service)Class.forName("com.findmybaby.www.s_child."+serviceName).newInstance()).execute(dto);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(service.equals(FAMILY)) {
			FamilyDTO dto = new FamilyDTO();
			int len = service.length()+1;
			String serviceName = msg.substring(len, msg.indexOf(",", len));
			len += serviceName.length()+1;
			String parent = msg.substring(len, msg.indexOf(",", len));
			len += parent.length()+1;
			String child = msg.substring(len, msg.indexOf(",", len));
			len += child.length()+1;
			String confirmStr = msg.substring(len);
			int confirm = Integer.parseInt(confirmStr);
			dto.setF_parent(parent);
			dto.setF_child(child);
			dto.setF_confirm(confirm);
			System.out.println("serviceName: "+serviceName+", parent: "+parent+", child: "+child+", confirm: "+confirm);
			try {
				result = ((Service)Class.forName("com.findmybaby.www.s_family."+serviceName).newInstance()).execute(dto);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(service.equals(LOCATION)) {
			LocationDTO dto = new LocationDTO();
			int len = service.length()+1;
			String serviceName = msg.substring(len, msg.indexOf(",", len));
			len += serviceName.length()+1;
			String userId = msg.substring(len, msg.indexOf(",", len));
			len += userId.length()+1;
			String latitude = msg.substring(len, msg.indexOf(",", len));
			len += latitude.length()+1;
			String longitude = msg.substring(len, msg.indexOf(",", len));
			len += longitude.length()+1;
			String heading = msg.substring(len);
			dto.setL_id(userId);
			dto.setL_latitude(latitude);
			dto.setL_longitude(longitude);
			dto.setL_heading(heading);
			System.out.println("serviceName: "+serviceName+", userId: "+userId+", latitude: "+latitude+", longitude: "+longitude+", heading: "+heading);
			try {
				result = ((Service)Class.forName("com.findmybaby.www.s_location."+serviceName).newInstance()).execute(dto);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("ServerController Error");
		}
		
		return result;
	}
}
