package com.shu.tongzhanbu.controller;


import com.shu.tongzhanbu.admindb.entity.Member;
import com.shu.tongzhanbu.component.annotation.AnonymousAccess;
import com.shu.tongzhanbu.component.annotation.Log;
import com.shu.tongzhanbu.component.apipath.ApiRestController;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
@RestController
@ApiRestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/getInfoByGh")
    private ResultBean getInfoByGh(@RequestParam String gh){
//        Member member = memberService.findByGonghao(gh);
        return memberService.findByGonghao(gh);
    }

    @GetMapping("/getInfo")
    private ResultBean getInfo(HttpServletResponse response){
//        Member member = memberService.findByGonghao(gh);
//        System.out.println(response);
        return memberService.getInfo(response);
    }


    //修改信息
    @PostMapping("/updateInfo")
//    @PreAuthorize("@el.check('/**')")
    private ResultBean updateInfo(@Validated @RequestBody Member member){

        return memberService.updateInfo(member);
    }


    //下载excel
    @GetMapping("/downloadInfoExcel")
//    @PreAuthorize("@el.check('/**')")
    private void  exportMemberInfo(@RequestParam(required = false)String gonghao, HttpServletResponse response) throws IOException {
        memberService.download(gonghao,response);
    }

    //定时任务计算年龄 每月1号
    @Scheduled(cron = "0 10 0 1 * ?")
    public  void computeage() throws Exception {
        memberService.computeage();
    }

}
