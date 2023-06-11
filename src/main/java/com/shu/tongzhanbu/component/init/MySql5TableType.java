package com.shu.tongzhanbu.component.init;

import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.stereotype.Component;

/**
 * @author tangyanqing
 * Description:
 * Date: 2019-09-02
 * Time: 12:25
 */
@Component
public class MySql5TableType extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
