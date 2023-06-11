package com.shu.tongzhanbu.admindb.repository;


import com.shu.tongzhanbu.admindb.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    @Query(value = "SELECT * from sys_member where gonghao=?1 and isdelete=0",nativeQuery = true)
    Member findMemberByGonghao(String gonghao);
    @Query(value = "SELECT * from sys_member where isdelete=0 order by create_time desc",nativeQuery = true)
    List<Member> findAllMembers();
    //柱状图（带具体数字，不包含离职人员、死亡人员、无编制人员）：各民主党派、无党派人士人数
    @Query(value = "select dangpai,count(*) FROM sys_member WHERE isdelete = 0 and (yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))  group by dangpai",nativeQuery = true)
    List Partypersonnum();

    //党派四个饼图
    @Query(value = "SELECT gender,count(*) from sys_member WHERE isdelete = 0 and dangpai = ?1 and( yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))group by gender",nativeQuery = true)
    List PartyGender(String dangpai);
    //党派四个饼图-学位
    @Query(value = "SELECT xuewei,count(*) from sys_member WHERE isdelete = 0 and dangpai = ?1 and (yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))group by xuewei",nativeQuery = true)
    List PartyXueWei(String dangpai);

    //党派四个饼图-职务级别
    @Query(value = "SELECT zhiwulevel,count(*) from sys_member WHERE isdelete = 0 and dangpai = ?1 and (yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))group by zhiwulevel",nativeQuery = true)
    List PartyZj(String dangpai);


    //党派四个饼图-职称
    @Query(value = "SELECT jishuzhicheng,count(*) from sys_member WHERE isdelete = 0 and dangpai = ?1 and (yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))group by jishuzhicheng",nativeQuery = true)
    List PartyZc(String dangpai);

    //党派四个饼图-职称级别
    @Query(value = "SELECT zhichengjibie,count(*) from sys_member WHERE isdelete = 0 and dangpai = ?1 and (yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))group by zhichengjibie",nativeQuery = true)
    List PartyZcjb(String dangpai);

    //民族柱图
    @Query(value = "SELECT minzu,count(*) from sys_member WHERE minzu!=\"汉族\" and minzu!=\"-\" and minzu!=\"\" and isdelete = 0 and (yuangongzu='在编在岗' or yuangongzu = '在编不在岗' or (yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员'))) \n" +
            "GROUP BY minzu",nativeQuery = true)
    List Minzu();

    //统战团体柱图
    @Query(value = "SELECT c.party_name,count(*) from sys_member as a left join sys_members_partys as b on b.id = a.id LEFT JOIN sys_tongzhanparty as c on c.party_id = b.party_id WHERE a.isdelete = 0  and (a.yuangongzu='在编在岗' or a.yuangongzu = '在编不在岗' or (a.yuangongzu = '不在岗不在编'and (a.yuangongzizu='退休人员' or '离休人员'))) GROUP BY c.party_name ",nativeQuery = true)
    List Tongzhanparty();

    //统战团体四个饼图-性别
    @Query(value = "SELECT a.gender,count(*) from sys_member as a left join sys_members_partys as b on b.id = a.id LEFT JOIN sys_tongzhanparty as c on c.party_id = b.party_id WHERE c.party_name = ?1 and a.isdelete = 0  and (a.yuangongzu='在编在岗' or a.yuangongzu = '在编不在岗' or (a.yuangongzu = '不在岗不在编'and (a.yuangongzizu='退休人员' or '离休人员'))) GROUP BY a.gender",nativeQuery = true)
    List TongzhanGender(String tongzhanparty);

    @Query(value = "SELECT a.xuewei,count(*) from sys_member as a left join sys_members_partys as b on b.id = a.id LEFT JOIN sys_tongzhanparty as c on c.party_id = b.party_id WHERE c.party_name = ?1 and a.isdelete = 0  and (a.yuangongzu='在编在岗' or a.yuangongzu = '在编不在岗' or (a.yuangongzu = '不在岗不在编'and (a.yuangongzizu='退休人员' or '离休人员'))) GROUP BY a.xuewei",nativeQuery = true)
    List TongzhanXueWei(String tongzhanparty);

    @Query(value = "SELECT a.zhiwulevel,count(*) from sys_member as a left join sys_members_partys as b on b.id = a.id LEFT JOIN sys_tongzhanparty as c on c.party_id = b.party_id WHERE c.party_name = ?1 and a.isdelete = 0  and (a.yuangongzu='在编在岗' or a.yuangongzu = '在编不在岗' or (a.yuangongzu = '不在岗不在编'and (a.yuangongzizu='退休人员' or '离休人员'))) GROUP BY a.zhiwulevel",nativeQuery = true)
    List TongzhanZj(String party);

    @Query(value = "SELECT a.zhichengjibie,count(*) from sys_member as a left join sys_members_partys as b on b.id = a.id LEFT JOIN sys_tongzhanparty as c on c.party_id = b.party_id WHERE c.party_name = ?1 and a.isdelete = 0  and (a.yuangongzu='在编在岗' or a.yuangongzu = '在编不在岗' or (a.yuangongzu = '不在岗不在编'and (a.yuangongzizu='退休人员' or '离休人员'))) GROUP BY a.zhichengjibie",nativeQuery = true)
    List TongzhanZcjb(String party);

    @Query(value = "select dangpai,count(*) from sys_member where isdelete = 0 and (yuangongzu = '在编在岗' or yuangongzu = '在编不在岗') GROUP BY dangpai",nativeQuery = true)
    List dangpaizaizhi();

    @Query(value = "select dangpai,count(*) FROM sys_member WHERE isdelete = 0 and ((yuangongzu = '不在岗不在编'and (yuangongzizu='退休人员' or '离休人员')))  group by dangpai",nativeQuery = true)
    List dangpailitui();


    //获取单位一级部门
    @Query(value = "select dep1 from sys_member where isdelete = 0 group by dep1",nativeQuery = true)
    List getdanweidep1();
    //获取职务
    @Query(value = "select zhiwulevel from sys_member where isdelete = 0 group by zhiwulevel",nativeQuery = true)
    List getZhiwulevel();

    @Query(value = "select * from sys_member where isdelete = 0 and id = ?1",nativeQuery = true)
    Member findOneById(Long id);


    @Query(value = "SELECT xuewei from sys_member WHERE isdelete = 0 group by xuewei",nativeQuery = true)
    List Allxuewei();

    @Query(value = "SELECT a.id from sys_member as a left join sys_members_partys as b on b.id = a.id WHERE a.isdelete = 0 and b.party_id in ?1",nativeQuery = true)
    Set<Long> findByTzpartyIds(List<Long> partyids);

    @Query(value = "select id from sys_member where dep1 like CONCAT('%',?1,'%')",nativeQuery = true)
    Set<Long> findByDept1( String danwei);
}