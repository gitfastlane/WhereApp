package com.findmybaby.www.s_location;

import com.findmybaby.www.dao.LocationDAO;
import com.findmybaby.www.dto.LocationDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class SearchLocation implements Service{

	@Override
	public String execute(TotalDTO dto) {
		LocationDAO dao = new LocationDAO();
		LocationDTO resultDto = dao.searchLocation((LocationDTO)dto);
		if(resultDto !=null) {	
			return resultDto.getL_latitude()+","
					+resultDto.getL_longitude()+","
					+resultDto.getL_heading();
		}
		return "SearchFail";
	}
	

}
