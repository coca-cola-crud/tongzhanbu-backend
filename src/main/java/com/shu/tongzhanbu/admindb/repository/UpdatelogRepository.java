package com.shu.tongzhanbu.admindb.repository;

import com.shu.tongzhanbu.admindb.entity.Updatelog;
import com.shu.tongzhanbu.admindb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UpdatelogRepository  extends JpaRepository<Updatelog, Long>, JpaSpecificationExecutor<Updatelog> {
}
