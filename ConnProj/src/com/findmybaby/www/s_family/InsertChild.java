package com.findmybaby.www.s_family;

import com.findmybaby.www.dao.FamilyDAO;
import com.findmybaby.www.dto.FamilyDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class InsertChild implements Service{

	@Override
	public String execute(TotalDTO dto) {
		FamilyDTO fdto = (FamilyDTO)dto;
		FamilyDAO dao = new FamilyDAO();
		String result = dao.insertChildByParent(fdto.getF_parent(), fdto.getF_child());
		return result;
	}

}
