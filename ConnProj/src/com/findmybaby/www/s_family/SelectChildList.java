package com.findmybaby.www.s_family;

import java.util.ArrayList;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.FamilyDAO;
import com.findmybaby.www.dto.FamilyDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class SelectChildList implements Service{

	@Override
	public String execute(TotalDTO dto) {
		String id = ((FamilyDTO)dto).getF_parent();
		FamilyDAO fdao = new FamilyDAO();
		ArrayList<String> list = fdao.selectListMyChild(id);
		ChildDAO cdao = new ChildDAO();
		String result = cdao.selectListChildName(list);
		return result;
	}

}
