package com.shu.tongzhanbu.service;

import com.alibaba.fastjson.JSONArray;
import com.shu.tongzhanbu.admindb.entity.Memberfamily;
import com.shu.tongzhanbu.admindb.repository.FamilyRepository;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.dto.FamilyDTO;
import com.shu.tongzhanbu.service.dto.FamilyOV;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class FamilyService {

    private final FamilyRepository familyRepository;

    public FamilyService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }


    public ResultBean addFamilyMember(Memberfamily memberfamily) {




        try {


            familyRepository.save(memberfamily);
            return ResultBean.success("操作成功");
        }catch (Exception e){
            return ResultBean.error("操作失败");
        }


    }

    public ResultBean getFamilyMembers(String gonghao,HttpServletResponse response) {
//        String id = null;
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            id = authentication.getName();
//
//        } catch (Exception e) {
//            response.setStatus(401);
//            return null;
//        }
        try {
            List<Memberfamily> memberfamilyList =  familyRepository.findByGonghao(gonghao);
            JSONArray jsonArray = new JSONArray();
            for(Memberfamily memberfamily:memberfamilyList){
                FamilyOV familyOV = new FamilyOV();
                BeanUtils.copyProperties(memberfamily,familyOV);
                familyOV.setIsAdd(false);
                familyOV.setIsEdit(false);
                jsonArray.add(familyOV);
            }
            return ResultBean.success(jsonArray);
        }catch (Exception e){
            return ResultBean.error("获取失败");
        }
    }

    public ResultBean editFamilyMember(Memberfamily memberfamily) {
        String id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();

        memberfamily.setGonghao(id);
        familyRepository.save(memberfamily);
        return ResultBean.success("修改成功");

    }

    public ResultBean deleteMember(String id) {
        familyRepository.deleteById(Long.valueOf(id));
        return ResultBean.success("删除成功");
    }
}
