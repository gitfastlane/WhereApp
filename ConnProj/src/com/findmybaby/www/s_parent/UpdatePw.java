package com.findmybaby.www.s_parent;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class UpdatePw implements Service {

	@Override
	public String execute(TotalDTO dto) {
		if(dto instanceof ParentDTO) {
			ParentDAO dao = new ParentDAO();
			if(dao.updatePw((ParentDTO)dto)) {
				return "true";
			}
			return "false";
		}
		else if(dto instanceof ChildDTO) {
			ChildDAO dao = new ChildDAO();
			if(dao.updatePw((ChildDTO)dto)) {
				return "true";
			}
			return "false";
		}
		return "false";
	}

}
