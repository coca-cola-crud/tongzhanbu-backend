package com.shu.tongzhanbu.controller;

import com.shu.tongzhanbu.admindb.entity.Member;
import com.shu.tongzhanbu.admindb.entity.Memberfamily;
import com.shu.tongzhanbu.component.apipath.ApiRestController;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.FamilyService;
import com.shu.tongzhanbu.service.dto.FamilyDTO;
import com.shu.tongzhanbu.service.dto.FamilyOV;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@ApiRestController
@RequiredArgsConstructor
@RequestMapping("/family")
public class FamilyController {

    private final FamilyService familyService;
    //添加/编辑家庭成员
    @PostMapping("/addMember")
    private ResultBean addFamilyMember(@Valid @RequestBody Memberfamily memberfamily, BindingResult bindingResult){

        return familyService.addFamilyMember(memberfamily);
    }
    //获取家庭成员列表
    @GetMapping("/getMembers")
    private ResultBean getFamilyMembers(String gonghao,HttpServletResponse response){
        return familyService.getFamilyMembers(gonghao,response);
    }
    //shanchu
    @GetMapping("/deleteMember")
    private ResultBean deleteMember(String id){
        System.out.println("dhs "+id);
        return familyService.deleteMember(id);
    }

//    //编辑信息
//    @PostMapping("/editMember")
//    private ResultBean editMember(@Valid @RequestBody Memberfamily memberfamily){
//        return familyService.editFamilyMember(memberfamily);
//    }
}
