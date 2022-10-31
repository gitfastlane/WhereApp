package com.findmybaby.www.s_parent;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class Login implements Service{

	ParentDTO resultdto = null;
	ChildDTO resultdto2 = null;
	@Override
	public String execute(TotalDTO dto) {
		if(dto instanceof ParentDTO) {
			ParentDAO dao = new ParentDAO();
			resultdto =	dao.loginParent((ParentDTO)dto);		
			if(resultdto !=null) { return "true"; }
			return "false";
		}
		else if(dto instanceof ChildDTO) {
			ChildDAO dao = new ChildDAO();
			resultdto2 =	dao.loginChild((ChildDTO)dto);		
			if(resultdto2 !=null) { return "true"; }
			return "false";
		
		}
		return "false";
		
		
		
	}


}
