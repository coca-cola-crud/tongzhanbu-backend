package com.shu.tongzhanbu.service;

import com.shu.tongzhanbu.admindb.repository.EditlogRespository;
import com.shu.tongzhanbu.component.util.QueryHelp;
import com.shu.tongzhanbu.component.util.ResultBean;
import com.shu.tongzhanbu.service.dto.EditlogQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class EditlogService {
    private final EditlogRespository editlogRespository;

    public EditlogService(EditlogRespository editlogRespository) {
        this.editlogRespository = editlogRespository;
    }

    public Object findAll(Pageable pageable, EditlogQueryCriteria criteria) {
        return editlogRespository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, criteria, cb)), pageable);
    }
}
