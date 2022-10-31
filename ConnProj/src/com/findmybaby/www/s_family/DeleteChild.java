package com.findmybaby.www.s_family;

import java.util.ArrayList;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.FamilyDAO;
import com.findmybaby.www.dto.FamilyDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class DeleteChild implements Service{

	@Override
	public String execute(TotalDTO dto) {
		String result = null;
		FamilyDAO fdao = new FamilyDAO();
		if(fdao.deleteMyChild((FamilyDTO)dto)) {
			ArrayList<String> list = fdao.selectListMyChild(((FamilyDTO)dto).getF_parent());
			ChildDAO cdao = new ChildDAO();
			result = cdao.selectListChildName(list);
		}
		return result;
	}

}
