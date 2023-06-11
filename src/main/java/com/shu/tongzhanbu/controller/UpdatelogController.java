package com.shu.tongzhanbu.controller;

import com.shu.tongzhanbu.component.annotation.Log;
import com.shu.tongzhanbu.service.UpdatelogService;
import com.shu.tongzhanbu.service.dto.EditlogQueryCriteria;
import com.shu.tongzhanbu.service.dto.UpdatelogQueryCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/updatelog")
public class UpdatelogController {
    private final UpdatelogService updatelogService;
    @Log("查询自动更新记录")
    @GetMapping("/getUpdatelog")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> getUpdatelog(Pageable pageable, UpdatelogQueryCriteria updatelogQueryCriteria){
        return new ResponseEntity<>(updatelogService.findAll(pageable,updatelogQueryCriteria), HttpStatus.OK);
    }
}
