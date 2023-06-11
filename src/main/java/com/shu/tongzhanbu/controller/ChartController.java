package com.shu.tongzhanbu.controller;

import com.shu.tongzhanbu.component.apipath.ApiRestController;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiRestController
@RequiredArgsConstructor
@RequestMapping("/Chart")
public class ChartController {

    private final ChartService chartService;
    @GetMapping("/partypersonnum")
//    @PreAuthorize("@el.check()")
    private ResultBean getPartypersonnum(){
      return chartService.getPartypersonnum();
    }
    @GetMapping("/partyPies")
//    @PreAuthorize("@el.check()")
    private ResultBean getPartyPies(String dangpai){
        return  chartService.getPartyPies(dangpai);
    }

    @GetMapping("/minzu")
//    @PreAuthorize("@el.check()")
    private ResultBean getMinzu(){
        return chartService.getMinzu();
    }

    @GetMapping("/tongzhanparty")
//    @PreAuthorize("@el.check()")
    private ResultBean getTongzhanparty(){
        return chartService.getTongzhanparty();
    }

    @GetMapping("/dangpaizl")
//    @PreAuthorize("@el.check()")
    private ResultBean getdangpaizl(){
        return chartService.getDangpaizaili();
    }

    @GetMapping("/tongzhanPies")
//    @PreAuthorize("@el.check()")
    private ResultBean getTongzhanPies(String tongzhan){
        return  chartService.getTongzhanPies(tongzhan);
    }
}

