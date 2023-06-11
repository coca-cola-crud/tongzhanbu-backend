
package com.shu.tongzhanbu.admindb.repository;


import com.shu.tongzhanbu.admindb.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 唐延清
 * @date 2018-11-24
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {

    /**
     * 根据日志类型删除信息
     *
     * @param logType 日志类型
     */
    @Modifying
    @Query(value = "delete from sys_log where logType = ?1", nativeQuery = true)
    void deleteByLogType(String logType);
}
