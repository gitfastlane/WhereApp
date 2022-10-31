package com.findmybaby.www.s_child;

import com.findmybaby.www.dao.ChildDAO;
import com.findmybaby.www.dao.ParentDAO;
import com.findmybaby.www.dto.ChildDTO;
import com.findmybaby.www.dto.ParentDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class SearchPw implements Service {

	String resultdto = null;
	
	@Override
	public String execute(TotalDTO dto) {
		if(dto instanceof ParentDTO) {
			ParentDAO dao = new ParentDAO();
			resultdto =	dao.searchPw((ParentDTO)dto);
			if(resultdto !=null) { return resultdto; }
			
		}else if(dto instanceof ChildDTO) {
			ChildDAO dao = new ChildDAO();
			resultdto =	dao.searchPw((ChildDTO)dto);
			if(resultdto !=null) { return resultdto; }
			
		}
		
		return "false";
	}

}
