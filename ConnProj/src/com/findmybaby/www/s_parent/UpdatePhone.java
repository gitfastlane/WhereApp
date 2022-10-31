package com.findmybaby.www.s_parent;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class UpdatePhone implements Service {

	@Override
	public String execute(TotalDTO dto) {
		if(dto instanceof ParentDTO) {
			ParentDAO dao = new ParentDAO();
			if(dao.updatePhone((ParentDTO)dto)) {
				return "true";
			}
			return "false";
		}
		else if(dto instanceof ChildDTO) {
			ChildDAO dao = new ChildDAO();
			if(dao.updatePhone((ChildDTO)dto)) {
				return "true";
			}
			return "false";
		}
		return "false";
	}
}
