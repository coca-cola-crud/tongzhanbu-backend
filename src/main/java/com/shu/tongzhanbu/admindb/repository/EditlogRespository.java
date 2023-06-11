package com.shu.tongzhanbu.admindb.repository;

import com.shu.tongzhanbu.admindb.entity.Editlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EditlogRespository extends JpaRepository<Editlog,Long>, JpaSpecificationExecutor<Editlog> {
}
