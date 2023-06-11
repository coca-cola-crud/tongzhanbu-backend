package com.shu.tongzhanbu.controller;

import com.shu.tongzhanbu.component.annotation.AnonymousAccess;
import com.shu.tongzhanbu.component.annotation.Log;
import com.shu.tongzhanbu.component.apipath.ApiRestController;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.MemberService;
import com.shu.tongzhanbu.service.UserCacheManager;
import com.shu.tongzhanbu.service.UserService;
import com.shu.tongzhanbu.service.dto.AdminDTO;
import com.shu.tongzhanbu.service.dto.MemberAdd;
import com.shu.tongzhanbu.service.dto.MemberQueryCriteria;
import com.shu.tongzhanbu.service.ov.MemberQueryOv;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ApiRestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {
    private final MemberService memberService;
    private final UserService userService;
    private final UserCacheManager userCacheManager;
    @Log("查询成员")
    @GetMapping("/getAllMember")
    @PreAuthorize("@el.check('/shurole/**')")
    public ResultBean getAllMember(Pageable pageable,
                                   MemberQueryCriteria memberQueryDTO){
        return memberService.findAll(pageable,memberQueryDTO);

    }


    @Log("导出查询结果")
    @PostMapping("/downloadMembers")
    @PreAuthorize("@el.check('/shurole/**')")
    public void downloadMember(HttpServletResponse response,
                               @RequestBody MemberQueryOv memberQueryOv) throws IOException {
        memberService.downloadSearchRes(memberQueryOv,response);

    }

    @Log("同步人事处数据")
    @GetMapping("/tongbuRsc")
    @PreAuthorize("@el.check()")
    public  ResultBean tongbu(String gonghao){
        return memberService.tongbu(gonghao);
    }

    @Log("添加成员")
    @PostMapping("/addMember")
    @PreAuthorize("@el.check()")
    public ResultBean addMember(@Validated @RequestBody MemberAdd memberAdd ){
        return memberService.addMember(memberAdd);
    }
    @Log("删除成员")
    @GetMapping("/deleteMember")
    @PreAuthorize("@el.check()")
    public ResultBean deleteMember(@RequestParam Long id){
        return memberService.deleteMember(id);
    }


    @Log("移除用户")
    @GetMapping("/setNomarlUser")
    @PreAuthorize("@el.check()")
    public ResultBean setNomarlUser(@RequestParam String uid){
        return userService.NomarlUser(uid);
    }

    @Log("修改角色")
    @GetMapping("/editRole")
    @PreAuthorize("@el.check()")
    public ResultBean editRole(@RequestParam String uid){
        userCacheManager.cleanUserCache(uid);
        return userService.editRole(uid);
    }

    @Log("获取所有管理员")
    @GetMapping("/getAdmins")
    @PreAuthorize("@el.check()")
    public ResultBean getAdmins(){
        return ResultBean.success(userService.findAllAdmin());
    }

    //添加管理员
    @Log("添加管理员")
    @PostMapping("/addAdmins")
    @PreAuthorize("@el.check()")
    public ResultBean addAdmin(@RequestBody AdminDTO adminDTO){
        return userService.addAdmin(adminDTO);
    }

    @Log("添加普通用户")
    @PostMapping("/addNormalUser")
    @PreAuthorize("@el.check()")
    public ResultBean addNormalUser(@RequestBody AdminDTO adminDTO){
        return userService.addNormalUser(adminDTO);
    }

    //获取所有成员姓名工号
    @Log("获取所有成员姓名工号")
    @GetMapping("/getAllmembersEasyInfo")
    @PreAuthorize("@el.check()")
    public ResultBean getAllmembersEasyInfo(){
        return userService.getAllmembersEasyInfo();
    }


    @Log("模糊搜索成员")
    @PostMapping("/searchMember")
    @PreAuthorize("@el.check('/shurole/**')")
    public ResultBean searchMember(@RequestParam Integer page,
                                   @RequestParam Integer size,@RequestBody MemberQueryOv memberQueryOv){
        return memberService.searchMember(page,size,memberQueryOv);
    }

    @GetMapping("/getdep1")
    @AnonymousAccess
    public ResultBean getDept(){
        return memberService.getDeptlist();
    }

    @GetMapping("/getzhiwu")
   @AnonymousAccess
    public ResultBean getZhiwu(){
        return memberService.getZhiwu();
    }

    @GetMapping("/input")
    @PreAuthorize("@el.check()")
    public ResultBean input(){
        return memberService.input();
    }

    @PostMapping("/inputexcel")
    @PreAuthorize("@el.check()")
    public ResultBean inputExcel(MultipartFile file) throws IOException {
        return memberService.inputExcel(file);
    }

    //每月1号凌晨1点
    @GetMapping("/updateAll")
    @Scheduled(cron = "0 0 1 1 * ?")
    public void updateAll(){
        memberService.updateAll();
    }

}
