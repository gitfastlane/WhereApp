package com.findmybaby.www.s_location;

import com.findmybaby.www.dao.LocationDAO;
import com.findmybaby.www.dto.LocationDTO;
import com.findmybaby.www.dto.TotalDTO;
import com.findmybaby.www.service.Service;

public class UpdateLocation implements Service{

	@Override
	public String execute(TotalDTO dto) {
		LocationDAO dao = new LocationDAO();
		if(dao.updateLocation((LocationDTO)dto)) {	
			return "true";
		}
		return "UpdateFail";
	}

	
}
