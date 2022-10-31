package com.findmybaby.www.s_child;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.LocationDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class Join implements Service{
	@Override
	public String execute(TotalDTO dto) {
		if(dto instanceof ParentDTO) {
			ParentDAO dao = new ParentDAO();
			if(dao.insertParent((ParentDTO)dto)) {
				LocationDAO ldao = new LocationDAO();
				ldao.insertLocation(((ParentDTO)dto).getP_id());
				return "true";
			}
			return "false";
		}else if(dto instanceof ChildDTO) {
			ChildDAO dao = new ChildDAO();
			if(dao.insertChild((ChildDTO)dto)) {
				LocationDAO ldao = new LocationDAO();
				ldao.insertLocation(((ChildDTO)dto).getC_id());
				return "true";
			}
			return "false";
		}
		return "회원가입 실패";
	}

}
