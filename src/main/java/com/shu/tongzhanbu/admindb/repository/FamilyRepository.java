package com.shu.tongzhanbu.admindb.repository;

import com.shu.tongzhanbu.admindb.entity.Log;
import com.shu.tongzhanbu.admindb.entity.Memberfamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends JpaRepository<Memberfamily, Long>, JpaSpecificationExecutor<Memberfamily> {
    @Query(value = "SELECT * from sys_member_family where EXISTS (select gonghao from sys_member where gonghao = ?1 and isdelete= 0) and gonghao = ?1",nativeQuery = true)
    List<Memberfamily> findByGonghao(String id);
}
