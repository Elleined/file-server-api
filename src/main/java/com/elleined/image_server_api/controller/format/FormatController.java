package com.elleined.image_server_api.controller.format;

import com.elleined.image_server_api.dto.format.FormatDTO;
import com.elleined.image_server_api.mapper.format.FormatMapper;
import com.elleined.image_server_api.service.format.FormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image-formats")
public class FormatController {
    private final FormatService formatService;
    private final FormatMapper formatMapper;

    @GetMapping
    public Page<FormatDTO> getAll(@RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                  @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                  @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                  @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return formatService.getAll(pageable)
                .map(formatMapper::toDTO);
    }
}
