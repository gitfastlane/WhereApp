package com.findmybaby.www.s_child;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class SearchChild implements Service{

	@Override
	public String execute(TotalDTO dto) {
		String childId = ((ChildDTO)dto).getC_id();
		ChildDAO dao = new ChildDAO();
		return dao.selectChildById(childId);
	}

}
