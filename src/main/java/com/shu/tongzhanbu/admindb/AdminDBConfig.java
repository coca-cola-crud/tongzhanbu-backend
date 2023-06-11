package com.shu.tongzhanbu.admindb;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application.yml"})
@EnableJpaRepositories(
        basePackages = "com.shu.tongzhanbu.admindb.repository",
        entityManagerFactoryRef = "adminDBEntityManager",
        transactionManagerRef = "adminDBTransactionManager"
)
public class AdminDBConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean adminDBEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(adminDBDataSource());
        em.setPackagesToScan(
                "com.shu.tongzhanbu.admindb.entity");
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "com.shu.tongzhanbu.component.init.MySql5TableType");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public JPAQueryFactory adminJpaQueryFactory(@Autowired EntityManagerFactory entityManagerFactory) {
        return new JPAQueryFactory(entityManagerFactory.createEntityManager());
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.admin-datasource")
    public DataSource adminDBDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager adminDBTransactionManager(@Autowired EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory);
        return transactionManager;
    }
}