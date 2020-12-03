package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.Campus;
import com.managedormitory.models.dto.CampusDto;
import org.springframework.stereotype.Component;

@Component
public class CampusCovertToCampusDto extends Converter<Campus, CampusDto> {
    @Override
    public CampusDto convert(Campus source) throws BadRequestException {
        CampusDto campusDto = new CampusDto();
        campusDto.setId(source.getId());
        campusDto.setName(source.getName());
        return campusDto;
    }
}
