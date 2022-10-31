package com.findmybaby.www.s_family;

import com.findmybaby.www.dao.FamilyDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.FamilyDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class SelectParentList implements Service{

	@Override
	public String execute(TotalDTO dto) {
		FamilyDTO fdto = (FamilyDTO)dto;
		FamilyDAO fdao = new FamilyDAO();
		ParentDAO pdao = new ParentDAO();
		String result = pdao.selectParentList(fdao.selectMyParentList(fdto));
		return result;
	}

}
