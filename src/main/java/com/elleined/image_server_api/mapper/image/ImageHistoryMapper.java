package com.elleined.image_server_api.mapper.image;

import com.elleined.image_server_api.dto.image.ImageHistoryDTO;
import com.elleined.image_server_api.mapper.CustomMapper;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.ImageHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ImageHistoryMapper extends CustomMapper<ImageHistory, ImageHistoryDTO> {

    @Override
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "lastAccessedAt", source = "lastAccessedAt"),
            @Mapping(target = "description", source = "description"),
            @Mapping(target = "additionalInformation", source = "additionalInformation"),
            @Mapping(target = "imageFormatId", source = "imageFormat.id"),
            @Mapping(target = "image", source = "image"),
//            @Mapping(target = "activeImageId", source = "activeImage.id"),
//            @Mapping(target = "deletedImageId", source = "deletedImage.id")
    })
    ImageHistoryDTO toDTO(ImageHistory imageHistory);

}
