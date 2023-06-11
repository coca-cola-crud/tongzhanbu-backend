package com.shu.tongzhanbu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shu.tongzhanbu.admindb.entity.*;
import com.shu.tongzhanbu.admindb.repository.*;
import com.shu.tongzhanbu.component.util.*;
import com.shu.tongzhanbu.component.util.constance.Constants;
import com.shu.tongzhanbu.service.dto.MemberAdd;
import com.shu.tongzhanbu.service.dto.MemberDTO;
import com.shu.tongzhanbu.service.dto.MemberQueryCriteria;
import com.shu.tongzhanbu.service.ov.MemberQueryOv;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final EditlogRespository editlogRespository;
    private final TongzhanpartyRepository tongzhanpartyRepository;
    private final UpdatelogRepository updatelogRepository;
    private final LogRepository logRepository;

    public MemberService(MemberRepository memberRepository, FamilyRepository familyRepository, UserRepository userRepository, EditlogRespository editlogRespository, TongzhanpartyRepository tongzhanpartyRepository, UpdatelogRepository updatelogRepository, LogRepository logRepository) {
        this.memberRepository = memberRepository;
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
        this.editlogRespository = editlogRespository;
        this.tongzhanpartyRepository = tongzhanpartyRepository;
        this.updatelogRepository = updatelogRepository;
        this.logRepository = logRepository;
    }

    public Member save(Member user) {
        if (memberRepository.findMemberByGonghao(user.getGonghao()) == null) {
//            System.out.println(user.getXingming());
            user.setIsdelete(0);
            return memberRepository.save(user);
        } else {
            return null;
        }

    }


    public ResultBean findByGonghao(String gonghao) {
        Member member = memberRepository.findMemberByGonghao(gonghao);
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(member, memberDTO);
        return ResultBean.success(memberDTO);
    }

    public ResultBean findAll(Pageable pageable, MemberQueryCriteria memberQueryDTO) {
//        Pageable pageable = PageRequest.of(curPage,pageSize);
//        PageRequest pageRequest = PageRequest.of(curPage,pageSize);

//        Specification<Member> specification = (Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb)->{
//            List<Predicate> predicates = new LinkedList<>();
//            if(StringUtils.isNotBlank(name)){
//                predicates.add(cb.equal(root.get("gonghao").as(String.class),name));
//
//            }
//            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//        };

        memberQueryDTO.setIsdelete(0);
        Page<Member> members = memberRepository.findAll(((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, memberQueryDTO, criteriaBuilder)), pageable);

        List<Member> list = members.getContent();

//        System.out.println(list);
        return ResultBean.success(list, Integer.valueOf(String.valueOf(members.getTotalElements())));
    }


    public ResultBean updateInfo(Member member) {

        String id = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        id = authentication.getName();

        try {
            if (!member.getGonghao().equals(id) && !userRepository.findUserByUid(id).getIsAdmin()) {
                return ResultBean.error("修改失败,工号与当前登录用户不同");
            } else {
                User user  = userRepository.findUserByUid(id);

                Editlog editlog = new Editlog();
                editlog.setXingming(user.getXingming());
                editlog.setEditperson(member.getXingming());
//                System.out.println(member.getGonghao());
                Member oldmember = memberRepository.findMemberByGonghao(member.getGonghao());
                member.setIsdelete(0);
                memberRepository.save(member);
                Member newmember = memberRepository.findMemberByGonghao(member.getGonghao());
//                System.out.println(oldmember);
                ArrayList<ComparbleResult> comparbleResults = ComparbleUtils.compareInstance(oldmember,newmember);
                ArrayList<String> content =  new ArrayList<>();
//                System.out.println(comparbleResults);
                for(ComparbleResult comparbleResult:comparbleResults){
                    if(comparbleResult.getHanderType().equals(EnumDataChangeHanderType.UPDATE.getCode())){
                        String editfield = comparbleResult.getFieldName();
                        if(editfield.equals("tzpartys")){
                          Set<Tongzhanparty> oldsets = (Set<Tongzhanparty>) comparbleResult.getFieldContent();
                          String oldcontent = "";
                          if(oldsets.isEmpty()){
                              oldcontent = "空";
                          }else{
                              for(Tongzhanparty tongzhanparty:oldsets){
                                  oldcontent+=tongzhanparty.getName()+',';
                              }
                          }
                          Set<Tongzhanparty> newsets = (Set<Tongzhanparty>) comparbleResult.getNewFieldContent();

                          String newcontent = "";
                          if(newsets.isEmpty()){
                                newcontent = "空";
                            }else{
                              for(Tongzhanparty tongzhanparty:newsets){
//                                  System.out.println(tongzhanparty.getId());
                                  newcontent+=tongzhanparty.getName()+',';
                              }

                          }
                          String edit = "原来："+oldcontent+" 修改后："+newcontent;
                          content.add(edit);
                        }else {
                            String oldcontent  = comparbleResult.getFieldContent()==null||comparbleResult.getFieldContent().equals("")?"空": String.valueOf(comparbleResult.getFieldContent());
                            String newcontent  = comparbleResult.getNewFieldContent()==null||comparbleResult.getNewFieldContent().equals("")?"空": String.valueOf(comparbleResult.getNewFieldContent());
                            String edit = "原来："+oldcontent+" 修改后："+newcontent;
                            content.add(edit);
                        }

                    }

                }
                editlog.setContent(content.toString());
                if(comparbleResults.size()>0){
                    editlogRespository.save(editlog);
                }

                return ResultBean.success("修改成功");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.error("修改失败");
        }

    }

    public ResultBean addMember(MemberAdd memberAdd) {
        Member member = new Member();
        BeanUtils.copyProperties(memberAdd, member);
//        System.out.println(member.getTzpartys().size());
        Member member1 = memberRepository.findMemberByGonghao(member.getGonghao());

        if (member1 == null) {
            member.setIsdelete(0);
            if (StringUtils.isNotBlank(member.getBirthday())) {
                Date birthday = CommonUtils.stringToDate(member.getBirthday());
                Integer age = CommonUtils.getAgeByBirth(birthday);
                member.setAge(CommonUtils.getAgeByBirth(birthday));
                if (CommonUtils.getAgeByBirth(birthday) <= 30) {
                    member.setAgelabel(Constants.AGE_30);
                } else if (age > 30 && age <= 40) {
                    member.setAgelabel(Constants.AGE_30_40);
                } else if (age > 40 && age <= 50) {
                    member.setAgelabel(Constants.AGE_40_50);
                } else if (age > 50 && age <= 60) {
                    member.setAgelabel(Constants.AGE_50_60);
                } else {
                    member.setAgelabel(Constants.AGE_60);
                }
            }

            memberRepository.save(member);
            return ResultBean.success(memberRepository.findMemberByGonghao(member.getGonghao()));
        } else {
            BeanUtils.copyProperties(memberAdd, member1);
            memberRepository.save(member1);
            return ResultBean.success("修改成功");
        }


    }

    public ResultBean deleteMember(Long id) {
        Member member = memberRepository.findOneById(id);
        member.setIsdelete(1);
        memberRepository.save(member);
        List<Memberfamily> memberfamilyList = familyRepository.findByGonghao(String.valueOf(id));
        familyRepository.deleteAll(memberfamilyList);
        return ResultBean.success("删除成功");

    }

    public void download(String gonghao, HttpServletResponse response) throws IOException {
        String id = null;
        if (!gonghao.equals("")) {
            id = gonghao;
        } else {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                id = authentication.getName();
            } catch (Exception e) {
                response.setStatus(401);
                return;
            }
        }


        Member member = memberRepository.findMemberByGonghao(id);
        ClassPathResource classPathResource = new ClassPathResource("static/上海大学统战成员基本信息表新版.xls");
        InputStream inputStream = classPathResource.getInputStream();
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        //读取excel模板
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        //读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);

        sheet.setForceFormulaRecalculation(true);
        SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");
        sheet.getRow(1).getCell(1).setCellValue(member.getXingming());
        sheet.getRow(1).getCell(3).setCellValue(member.getGender());
        sheet.getRow(1).getCell(5).setCellValue(member.getMinzu());
        sheet.getRow(1).getCell(7).setCellValue(member.getCountry());
        sheet.getRow(2).getCell(1).setCellValue(member.getBirthday());

//        System.out.println(f.format(member.getBirthday()));
        sheet.getRow(2).getCell(3).setCellValue(member.getJiguan());
        sheet.getRow(2).getCell(5).setCellValue(member.getBirthplace());
        sheet.getRow(2).getCell(7).setCellValue(member.getHealth());
        sheet.getRow(3).getCell(1).setCellValue(member.getJoinworkyearmonth());
        sheet.getRow(3).getCell(3).setCellValue(member.getJishuzhicheng());
        sheet.getRow(3).getCell(5).setCellValue(member.getZhiwulevel());
        sheet.getRow(3).getCell(7).setCellValue(member.getZhichengjibie());
        sheet.getRow(4).getCell(1).setCellValue(member.getCard());
        sheet.getRow(4).getCell(5).setCellValue(member.getSpeciality());
        sheet.getRow(4).getCell(7).setCellValue(member.getZongjiao());
        sheet.getRow(5).getCell(1).setCellValue(member.getDangpai());
        sheet.getRow(5).getCell(3).setCellValue(member.getJoindptime());
        sheet.getRow(5).getCell(6).setCellValue(member.getSeconddangpai());
        sheet.getRow(5).getCell(8).setCellValue(member.getJoinsdptime());
        sheet.getRow(6).getCell(2).setCellValue(member.getXuewei());
        sheet.getRow(6).getCell(6).setCellValue(member.getCollege());
        sheet.getRow(7).getCell(2).setCellValue(member.getZzxuewei());
        sheet.getRow(7).getCell(6).setCellValue(member.getZzcollege());

        sheet.getRow(8).getCell(1).setCellValue(member.getRendaInfo());
        sheet.getRow(8).getCell(5).setCellValue(member.getZhengxieInfo());
        if(member.getYjjuliuquan()!=null&&!member.getYjjuliuquan().equals("")){
            sheet.getRow(9).getCell(2).setCellValue("\u221A");
            sheet.getRow(9).getCell(3).setCellValue(member.getYjjuliuquan());
        }else {
            sheet.getRow(10).getCell(2).setCellValue("\u221A");
        }

        if(member.getYjjuliuxuke()!=null&&!member.getYjjuliuxuke().equals("")){
            sheet.getRow(9).getCell(7).setCellValue("\u221A");
            sheet.getRow(9).getCell(8).setCellValue(member.getYjjuliuxuke());
        }else {
            sheet.getRow(10).getCell(7).setCellValue("\u221A");
        }


        sheet.getRow(9).getCell(8).setCellValue(member.getYjjuliuxuke());
        sheet.getRow(11).getCell(2).setCellValue("上海大学" + (member.getDep1() == null ? "" : member.getDep1()) +
                (member.getDep2() == null||member.getDep2().equals(member.getDep1()) ? "" : member.getDep2()) +
                (member.getJobInfo() == null ? "" : member.getJobInfo()));//工作单位
        sheet.getRow(11).getCell(8).setCellValue("高校");
        sheet.getRow(12).getCell(2).setCellValue(member.getNowsocialjob());
        sheet.getRow(12).getCell(8).setCellValue(member.getRenxuantype());
        sheet.getRow(13).getCell(1).setCellValue(member.getJobaddress());
        sheet.getRow(13).getCell(5).setCellValue(member.getJobphone());
        sheet.getRow(13).getCell(8).setCellValue(member.getJobyoubian());
        sheet.getRow(14).getCell(1).setCellValue(member.getHujiaddress());
        sheet.getRow(14).getCell(5).setCellValue(member.getHujiphone());
        sheet.getRow(14).getCell(8).setCellValue(member.getHujiyoubian());
        sheet.getRow(15).getCell(1).setCellValue(member.getJuzhuaddress());
        sheet.getRow(15).getCell(5).setCellValue(member.getJuzhuphone());
        sheet.getRow(15).getCell(8).setCellValue(member.getJuzhuyoubian());
        sheet.getRow(16).getCell(1).setCellValue(member.getTelephone());
        sheet.getRow(16).getCell(6).setCellValue(member.getEmail());
        sheet.getRow(17).getCell(1).setCellValue(member.getJianli());
        sheet.getRow(18).getCell(1).setCellValue(member.getChengjiu());
        sheet.getRow(19).getCell(1).setCellValue(member.getHuojiang());

        //照片
        byte[] decoderes = testUtil.decode(member.getPicture());
        int pictureIdx = wb.addPicture(decoderes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = wb.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        anchor.setCol1(8);
        anchor.setRow1(1);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize(0.3, 0.3);

        //家庭成员
        List<Memberfamily> memberfamilyList = familyRepository.findByGonghao(member.getGonghao());
        Integer count = 1;
        for (Memberfamily memberfamily : memberfamilyList) {
            Integer row = count + 20;
            sheet.getRow(row).getCell(1).setCellValue(memberfamily.getGuanxi());
            sheet.getRow(row).getCell(2).setCellValue(memberfamily.getName());
            sheet.getRow(row).getCell(3).setCellValue(memberfamily.getCard());
            sheet.getRow(row).getCell(5).setCellValue(memberfamily.getZzmianmao());
            sheet.getRow(row).getCell(6).setCellValue(memberfamily.getYjcjjuliu());
            sheet.getRow(row).getCell(8).setCellValue(memberfamily.getJobdanwei());
            count += 1;

        }


        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "Attachment;Filename=" + new String(member.getXingming().getBytes()));
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setContentType("applicion/vnd.ms-excel;charset=utf-8");// 设置contentType为excel格式
//        System.out.println(response.getHeader("Content-Disposition"));
        OutputStream output = response.getOutputStream();
        wb.write(output);
        output.flush();
        output.close();
    }

    public ResultBean getInfo(HttpServletResponse response) {
        String id = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            id = authentication.getName();
//            System.out.println("获取信息");

        } catch (Exception e) {
            response.setStatus(401);

        }
        try {
            Member member = memberRepository.findMemberByGonghao(id);
            MemberDTO memberOv = new MemberDTO();
            BeanUtils.copyProperties(member, memberOv);

            if (StringUtils.isNotBlank(member.getYjjuliuquan())) {
                memberOv.setHasyjjuliuquan("1");
            } else {
                memberOv.setHasyjjuliuquan("0");
            }
            if (StringUtils.isNotBlank(member.getYjjuliuxuke())) {
                memberOv.setHasyjjuliuxuke("1");
            } else {
                memberOv.setHasyjjuliuxuke("0");
            }
            if (StringUtils.isNotBlank(member.getAbroad())) {
                memberOv.setIsAbroad("1");
            } else {
                memberOv.setIsAbroad("0");
            }
            return ResultBean.success(memberOv);
        } catch (Exception e) {
            return ResultBean.error("您还不是统战部成员");
        }

    }

    public List<Member> searchMemberALL(MemberQueryOv memberQueryOv){
        Specification<Member> specification = (Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(cb.equal(root.get("isdelete"), 0));
            if (StringUtils.isNotBlank(memberQueryOv.getGonghao())) {
                predicates.add(cb.equal(root.get("gonghao").as(String.class), memberQueryOv.getGonghao()));

            }
            if (StringUtils.isNotBlank(memberQueryOv.getXingming())) {
                predicates.add(cb.like(root.get("xingming"),"%"+ memberQueryOv.getXingming()+"%"));

            }
            if (StringUtils.isNotBlank(memberQueryOv.getJuzhuaddress())) {
                predicates.add(cb.like(root.get("juzhuaddress"),"%"+ memberQueryOv.getJuzhuaddress()+"%"));

            }
            if (memberQueryOv.getYuangongzu().size() != 0) {
                Path<Object> path = root.get("yuangongzu");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getYuangongzu()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getYuangongzizu().size() != 0) {
                Path<Object> path = root.get("yuangongzizu");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getYuangongzizu()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (StringUtils.isNotBlank(memberQueryOv.getCountry())) {
                predicates.add(cb.equal(root.get("country").as(String.class), memberQueryOv.getCountry()));
            }
            if (StringUtils.isNotBlank(memberQueryOv.getGender())) {
                predicates.add(cb.equal(root.get("gender").as(String.class), memberQueryOv.getGender()));
            }
            if (memberQueryOv.getMinzu().size() != 0) {
                Path<Object> path = root.get("minzu");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getMinzu()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            //  年龄
            if (memberQueryOv.getAge().size() != 0) {
                Path<Object> path = root.get("agelabel");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getAge()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            //党派
            if (memberQueryOv.getDangpai().size() != 0) {
                Path<Object> path = root.get("dangpai");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getDangpai()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getTongzhanpartyId().size() != 0) {
                Set<Long> memberIdlist = memberRepository.findByTzpartyIds(memberQueryOv.getTongzhanpartyId());
                Path<Object> path = root.get("id");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for(Long id:memberIdlist){
                    in.value(id);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getXuewei().size() != 0) {
                Path<Object> path = root.get("xuewei");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getXuewei()) {
                    in.value(str);
                }
                Path<Object> path1 = root.get("zzxuewei");
                CriteriaBuilder.In<Object> zzin = cb.in(path1);
                for (String str : memberQueryOv.getXuewei()) {
                    zzin.value(str);
                }
                predicates.add(cb.or(zzin, in));

            }
            if (memberQueryOv.getZhichengjibie().size() != 0) {
                Path<Object> path = root.get("zhichengjibie");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getZhichengjibie()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getZhuanjiatype().size() != 0) {
                Path<Object> path = root.get("zhuanjiatype");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getZhuanjiatype()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            //单位到一级部门
            if (memberQueryOv.getDanwei().size() != 0) {
                List<String> danweis = memberQueryOv.getDanwei();
                Set<Long> memberIdlist = memberRepository.findByDept1(danweis.get(0));
                for(String danwei:danweis){
                    memberIdlist.addAll(memberRepository.findByDept1(danwei));
                }
                Path<Object> path = root.get("id");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for(Long id:memberIdlist){
                    in.value(id);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getZhiwujibie().size() != 0) {
                Path<Object> path = root.get("zhichengjibie");
                CriteriaBuilder.In<Object> in = cb.in(path);
                int flag =0;//标记有没有其他
                Set<String> paichu = new HashSet<>();
                paichu.add("正高级");
                paichu.add("副高级");
                paichu.add("中级");
                for (String str : memberQueryOv.getZhichengjibie()) {
                    if(str.equals("其他")){
                        flag = 1;
                    }else {
                        in.value(str);
                        paichu.remove(str);
                    }

                }
                if(flag == 1){
                    CriteriaBuilder.In<Object> newin = cb.in(path);
                    for(String str:paichu){
                        newin.value(str);
                    }
                    predicates.add(cb.and(newin.not()));
                }else {
                    predicates.add(cb.and(in));
                }

            }
            if (StringUtils.isNotBlank(memberQueryOv.getAboard())) {
                if (memberQueryOv.getAboard().equals("1")) {//1有海外学习
                    predicates.add(cb.isNotNull(root.get("abroad").as(String.class)));
                } else {
                    predicates.add(cb.isNull(root.get("abroad").as(String.class)));
                }
            }
            if (memberQueryOv.getJobaddress().size() != 0) {
                Path<Object> path = root.get("jobaddress");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getJobaddress()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

       return memberRepository.findAll(specification);
    }
    public ResultBean searchMember(Integer page, Integer size, MemberQueryOv memberQueryOv) {
        Pageable pageable = PageRequest.of(page, size);
        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Member> specification = (Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(cb.equal(root.get("isdelete"), 0));
            if (StringUtils.isNotBlank(memberQueryOv.getGonghao())) {
                predicates.add(cb.equal(root.get("gonghao").as(String.class), memberQueryOv.getGonghao()));

            }
            if (StringUtils.isNotBlank(memberQueryOv.getXingming())) {
                predicates.add(cb.like(root.get("xingming"),"%"+ memberQueryOv.getXingming()+"%"));

            }
            if (StringUtils.isNotBlank(memberQueryOv.getJuzhuaddress())) {
                predicates.add(cb.like(root.get("juzhuaddress"),"%"+ memberQueryOv.getJuzhuaddress()+"%"));

            }
            if (memberQueryOv.getYuangongzu().size() != 0) {
                Path<Object> path = root.get("yuangongzu");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getYuangongzu()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getYuangongzizu().size() != 0) {
                Path<Object> path = root.get("yuangongzizu");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getYuangongzizu()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (StringUtils.isNotBlank(memberQueryOv.getCountry())) {
                predicates.add(cb.equal(root.get("country").as(String.class), memberQueryOv.getCountry()));
            }
            if (StringUtils.isNotBlank(memberQueryOv.getGender())) {
                predicates.add(cb.equal(root.get("gender").as(String.class), memberQueryOv.getGender()));
            }
            if (memberQueryOv.getMinzu().size() != 0) {
                Path<Object> path = root.get("minzu");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getMinzu()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            //  年龄
            if (memberQueryOv.getAge().size() != 0) {
                Path<Object> path = root.get("agelabel");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getAge()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            //党派
            if (memberQueryOv.getDangpai().size() != 0) {
                Path<Object> path = root.get("dangpai");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getDangpai()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getTongzhanpartyId().size() != 0) {
                Set<Long> memberIdlist = memberRepository.findByTzpartyIds(memberQueryOv.getTongzhanpartyId());
                Path<Object> path = root.get("id");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for(Long id:memberIdlist){
                    in.value(id);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getXuewei().size() != 0) {
                Path<Object> path = root.get("xuewei");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getXuewei()) {
                    in.value(str);
                }
                Path<Object> path1 = root.get("zzxuewei");
                CriteriaBuilder.In<Object> zzin = cb.in(path1);
                for (String str : memberQueryOv.getXuewei()) {
                    zzin.value(str);
                }
                predicates.add(cb.or(zzin, in));

            }
            if (memberQueryOv.getZhichengjibie().size() != 0) {
                Path<Object> path = root.get("zhichengjibie");
                CriteriaBuilder.In<Object> in = cb.in(path);
                int flag =0;//标记有没有其他
                Set<String> paichu = new HashSet<>();
                paichu.add("正高级");
                paichu.add("副高级");
                paichu.add("中级");
                for (String str : memberQueryOv.getZhichengjibie()) {
                    if(str.equals("其他")){
                        flag = 1;
                    }else {
                        in.value(str);
                        paichu.remove(str);
                    }

                }
                if(flag == 1){
                    CriteriaBuilder.In<Object> newin = cb.in(path);
                    for(String str:paichu){
                        newin.value(str);
                    }
                    predicates.add(cb.and(newin.not()));
                }else {
                    predicates.add(cb.and(in));
                }

            }
            if (memberQueryOv.getZhuanjiatype().size() != 0) {
                Path<Object> path = root.get("zhuanjiatype");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getZhuanjiatype()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            //单位到一级部门
            if (memberQueryOv.getDanwei().size() != 0) {
//                Path<Object> path = root.get("dep1");
//                CriteriaBuilder.In<Object> in = cb.in(path);
//                for (String str : memberQueryOv.getDanwei()) {
//                    in.value(str);
//                }
//                predicates.add(cb.and(in));
//                List<String> searchdanwei = memberQueryOv.getDanwei();
//                List<String> newsearchdanwei = new ArrayList<>();
//                for(String danwei:searchdanwei){
//                    String[] strings = danwei.split(",");
//                    for(String s:strings){
//                        if(!newsearchdanwei.contains(s)){
//                            newsearchdanwei.add(s);
//                        }
//                    }
//                }
                List<String> danweis = memberQueryOv.getDanwei();
                Set<Long> memberIdlist = memberRepository.findByDept1(danweis.get(0));
                for(String danwei:danweis){
                    memberIdlist.addAll(memberRepository.findByDept1(danwei));
                }
                Path<Object> path = root.get("id");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for(Long id:memberIdlist){
                    in.value(id);
                }
                predicates.add(cb.and(in));
            }
            if (memberQueryOv.getZhiwujibie().size() != 0) {
                Path<Object> path = root.get("zhichengjibie");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getZhiwujibie()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            if (StringUtils.isNotBlank(memberQueryOv.getAboard())) {
                if (memberQueryOv.getAboard().equals("1")) {//1有海外学习
                    predicates.add(cb.isNotNull(root.get("abroad").as(String.class)));
                } else {
                    predicates.add(cb.isNull(root.get("abroad").as(String.class)));
                }
            }
            if (memberQueryOv.getJobaddress().size() != 0) {
                Path<Object> path = root.get("jobaddress");
                CriteriaBuilder.In<Object> in = cb.in(path);
                for (String str : memberQueryOv.getJobaddress()) {
                    in.value(str);
                }
                predicates.add(cb.and(in));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<Member> members = memberRepository.findAll(specification, pageable);
        List<Member> list = members.getContent();
        return ResultBean.success(list, Integer.valueOf(String.valueOf(members.getTotalElements())));
    }


    public void computeage() {
//        System.out.println("计算年龄");
        List<Member> members = memberRepository.findAllMembers();
        for (Member member : members) {
            if (StringUtils.isNotBlank(member.getBirthday()) && StringUtils.isNotBlank(member.getXingming())) {
//                System.out.println(member.getBirthday());
                Date birthday = CommonUtils.stringToDate(member.getBirthday());
                Integer age = CommonUtils.getAgeByBirth(birthday);
                member.setAge(CommonUtils.getAgeByBirth(birthday));
                if (CommonUtils.getAgeByBirth(birthday) <= 30) {
                    member.setAgelabel(Constants.AGE_30);
                } else if (age > 30 && age <= 40) {
                    member.setAgelabel(Constants.AGE_30_40);
                } else if (age > 40 && age <= 50) {
                    member.setAgelabel(Constants.AGE_40_50);
                } else if (age > 50 && age <= 60) {
                    member.setAgelabel(Constants.AGE_50_60);
                } else {
                    member.setAgelabel(Constants.AGE_60);
                }
                memberRepository.save(member);
            }


        }

    }

    public ResultBean tongbu(String gonghao) {
        HashMap<String, String> map = TongbuUtil.getInfofromRsc(gonghao);
        if (map == null) {
            return ResultBean.error("同步失败,请检查工号是否正确");
        } else {
            return ResultBean.success(JSONObject.parse(JSON.toJSONString(map)));
        }

    }

    public ResultBean getDeptlist() {
        List deptlist = memberRepository.getdanweidep1();
        JSONArray jsonArray = new JSONArray();
        for (Object row : deptlist) {
            String dept = (String) row;
            JSONObject jsonObject = new JSONObject();
            if (dept != null) {
                String[] depts = dept.split(",");
                for(String tmp:depts){
                    jsonObject.put("value", tmp);
                    jsonObject.put("label", tmp);
                }

            }
            if(!jsonArray.contains(jsonObject)){
                jsonArray.add(jsonObject);
            }

        }
        return ResultBean.success(jsonArray);
    }

    public ResultBean input() {
        List<Member> members = memberRepository.findAllMembers();
        for (Member member : members) {
            HashMap<String, String> map = TongbuUtil.getInfofromRsc(member.getGonghao());
//            System.out.println(map);
            Member member1 = JSONObject.parseObject(JSONObject.toJSONString(map), Member.class);
            member1.setId(member.getId());
            memberRepository.save(member1);
        }
        return ResultBean.success();
    }
//    @Transactional(rollbackFor = Exception.class)
    public ResultBean inputExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        //读取excel模板
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        //读取了模板内所有sheet内容
        HSSFSheet sheet = wb.getSheetAt(0);
        int rowCount = sheet.getLastRowNum();
        for (int i = 1; i <= rowCount; i++) {
            Row row = sheet.getRow(i);
            Member member = new Member();
            int physicalNumberOfCells = row.getPhysicalNumberOfCells();
//            System.out.println(physicalNumberOfCells);
            for (int k = 0; k < 9; k++) {
                HSSFCell cell = (HSSFCell) row.getCell(k);
                String cellValue= "";
                if(cell!=null){
                    cell.setCellType(CellType.STRING);
                    cellValue= cell.getStringCellValue().trim();
                }

                String tongzhanparty = null;
                switch (k) {
                    //根据列数决定如何处理
                    case 0:
                        member.setGonghao(cellValue);
                        break;
                    case 1:
                        member.setJoindptime(cellValue);
                        break;
                    case 2:
                        member.setJuzhuaddress(cellValue);
                        break;
                    case 3:
                        member.setTelephone(cellValue);
                        break;
                    case 4:
                        member.setXuewei(cellValue);
                        break;
                    case 5:
                        member.setEmail(cellValue);
                        break;
                    case 6:
                        member.setDangpai(cellValue);
                        break;
                    case 7:
                        member.setTongzhanparty(cellValue);
                        break;
                    case 8:
                        member.setAbroad(cellValue);
                        break;
                }

            }
            Member memberexist = memberRepository.findMemberByGonghao(member.getGonghao());
            if (memberexist == null) {//如果不存在
                HashMap<String, String> map = TongbuUtil.getInfofromRsc(member.getGonghao());
                Member member1 = JSONObject.parseObject(JSONObject.toJSONString(map), Member.class);
                member1.setIsdelete(0);
                member1.setJoindptime(member.getJoindptime());
                member1.setJuzhuaddress(member.getJuzhuaddress());
                member1.setTelephone(member.getTelephone());
                member1.setXuewei(member.getXuewei());
                member1.setEmail(member.getEmail());
                member1.setDangpai(member.getDangpai());
                if (member.getAbroad()!=null&&!member.getAbroad().equals("")) {
                    member1.setAbroad(member.getAbroad());
                }
                if (member.getTongzhanparty()!=null&&!member.getTongzhanparty().equals("")) {
                    Set<Tongzhanparty> tongzhanparties = new HashSet<>();
                    Tongzhanparty tongzhanparty = new Tongzhanparty();
                    tongzhanparty.setId(Constants.tongzhaninputMap.get(member.getTongzhanparty()));
                    tongzhanparties.add(tongzhanparty);
                    member1.setTzpartys(tongzhanparties);
                }
                memberRepository.save(member1);
                System.out.println(member.getGonghao() + "导入成功");
            } else {//如果已经存在
                System.out.println(member.getGonghao() + "已存在");
                if (member.getAbroad()!=null&&!member.getAbroad().equals("")) {
                    memberexist.setAbroad(member.getAbroad());
                }
                if (member.getTongzhanparty()!=null&&!member.getTongzhanparty().equals("")) {
//                    System.out.println(1);
                    Set<Tongzhanparty> tongzhanparties = new HashSet<>();
                    if (memberexist.getTzpartys().size() != 0&&!member.getTongzhanparty().equals("")) {//如果存在的成员已经有统战团体
                        tongzhanparties = memberexist.getTzpartys();

                    }
                    Tongzhanparty tongzhanparty = new Tongzhanparty();
                    tongzhanparty.setId(tongzhanpartyRepository.findByName(member.getTongzhanparty()));
                    tongzhanparties.add(tongzhanparty);
                    memberexist.setTzpartys(tongzhanparties);
                }
                if(member.getDangpai()!=null&&!member.getDangpai().equals("")){
                    memberexist.setDangpai(member.getDangpai());
                }
                if(member.getJuzhuaddress()!=null&&!member.getJuzhuaddress().equals("")){
                    memberexist.setJuzhuaddress(member.getJuzhuaddress());
                }
                memberRepository.save(memberexist);
            }

        }
        return ResultBean.success();
    }

    public void downloadSearchRes(MemberQueryOv memberQueryOv,HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Member> all = searchMemberALL(memberQueryOv);
        for (Member member: all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("工号", member.getGonghao());
            map.put("姓名", member.getXingming());
            map.put("性别", member.getGender());
            map.put("民族", member.getMinzu());
            map.put("出生年月", member.getBirthday());
            map.put("政治面貌", member.getDangpai());
            map.put("职称",member.getJishuzhicheng());
            map.put("员工组",member.getYuangongzu());
            map.put("员工子组",member.getYuangongzizu());
            map.put("家庭住址",member.getJuzhuaddress());
            map.put("邮箱",member.getEmail());
            map.put("手机号码",member.getTelephone());
            Set<Tongzhanparty> tzpartys = member.getTzpartys();
            String res = "";
            for(Tongzhanparty tongzhanparty:tzpartys){
                res+=tongzhanparty.getName()+" ";
            }
            map.put("统战团体",res);
            list.add(map);
        }
//        System.out.println(list);
        FileUtil.downloadExcel(list, response);

    }

    public ResultBean getZhiwu() {
        HashSet<String> zhiwuset = new HashSet<>();
        zhiwuset.add("正局级");
        zhiwuset.add("副局级");
        zhiwuset.add("正处级");
        zhiwuset.add("副处级");
        List zhiwulist = memberRepository.getZhiwulevel();
        JSONArray jsonArray = new JSONArray();
        for(Object row:zhiwulist){
            String zhiwu = (String) row;
            if(zhiwu!=null){
               zhiwuset.add(zhiwu);
            }
        }
        for(String item:zhiwuset){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value",item);
            jsonObject.put("label",item);
            jsonArray.add(jsonObject);
        }

        return ResultBean.success(jsonArray);
    }

    //同步方法，保证只有一条线程在更新
    public synchronized void  updateAll() {
        System.out.println(Thread.currentThread().getName()+"正在更新");
        List<Member> members = memberRepository.findAllMembers();
        int count = 0;
        int updated = 0;
        List<String> names = new ArrayList<>();
        for(Member member:members){
            String gonghao = member.getGonghao();
            try{
                count++;
                HashMap<String, String> map = TongbuUtil.getInfofromRsc(gonghao);
                Member old = new Member();
                BeanUtils.copyProperties(member,old);
                member.setYuangongzu(map.get("yuangongzu"));
                member.setYuangongzizu(map.get("yuangongzizu"));
                member.setJishuzhicheng(map.get("jishuzhicheng"));
                member.setZhichengjibie(map.get("zhichengjibie"));
                member.setDep1(map.get("dep1"));
                member.setDep2(map.get("dep2"));
                member.setJobInfo(map.get("jobInfo"));
                memberRepository.save(member);
                ArrayList<ComparbleResult> comparbleResults = ComparbleUtils.compareInstance(old,member);
                if(comparbleResults.size()!=0){
                    updated++;
                    names.add(member.getXingming());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Updatelog updatelog = new Updatelog();
        updatelog.setDescription("共"+count+"个，本次更新"+updated+"个，"+"分别为"+names);
        updatelog.setType(SecurityUtils.getCurrentUsername().equals("System")?"System":"user");
        updatelogRepository.save(updatelog);
//        Log log = new Log();
//        log.setUsername(SecurityUtils.getCurrentUsername());
//        log.setDescription("共数据"+count+"个，本次更新"+updated+"个，"+"分别为"+names);
//        logRepository.save(log);

    }
}