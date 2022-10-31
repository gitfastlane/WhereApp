package com.findmybaby.www.s_child;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class ChangePw implements Service{
	
	@Override
	public String execute(TotalDTO dto) {
		if(dto instanceof ParentDTO) {
			ParentDAO dao = new ParentDAO();
			if(dao.changPw((ParentDTO)dto)) {
				return "true";
			}
		}
		else if (dto instanceof ChildDTO){
			ChildDAO dao = new ChildDAO();
			if(dao.changPw((ChildDTO)dto)) {
				return "true";
			}
		}
		return "false";
	}
	
}
