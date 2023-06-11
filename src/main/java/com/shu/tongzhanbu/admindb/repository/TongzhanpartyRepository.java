package com.shu.tongzhanbu.admindb.repository;

import com.shu.tongzhanbu.admindb.entity.Tongzhanparty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TongzhanpartyRepository  extends JpaRepository<Tongzhanparty, Long>, JpaSpecificationExecutor<Tongzhanparty> {
        @Query(value ="select party_id from sys_tongzhanparty where party_name =?1",nativeQuery = true)
        Long findByName(String name);
}
