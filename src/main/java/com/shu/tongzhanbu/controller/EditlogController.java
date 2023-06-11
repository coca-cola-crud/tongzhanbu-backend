package com.shu.tongzhanbu.controller;

import com.shu.tongzhanbu.admindb.entity.Editlog;
import com.shu.tongzhanbu.component.annotation.Log;
import com.shu.tongzhanbu.component.apipath.ApiRestController;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.EditlogService;
import com.shu.tongzhanbu.service.dto.EditlogQueryCriteria;
import com.shu.tongzhanbu.service.dto.MemberQueryCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@ApiRestController
@RequiredArgsConstructor
@RequestMapping("/editlog")
public class EditlogController {
    private final EditlogService editlogService;
    @Log("查询修改日志")
    @GetMapping("/getEditlog")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> getAllEditlog(Pageable pageable, EditlogQueryCriteria editlogQueryCriteria){
        return new ResponseEntity<>(editlogService.findAll(pageable,editlogQueryCriteria), HttpStatus.OK);
    }

}
