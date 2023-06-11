package com.shu.tongzhanbu.service;

import com.shu.tongzhanbu.admindb.repository.EditlogRespository;
import com.shu.tongzhanbu.admindb.repository.UpdatelogRepository;
import com.shu.tongzhanbu.component.util.QueryHelp;
import com.shu.tongzhanbu.service.dto.UpdatelogQueryCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatelogService {
    private final UpdatelogRepository updatelogRepository;
    public Object findAll(Pageable pageable, UpdatelogQueryCriteria updatelogQueryCriteria) {
        return updatelogRepository.findAll(((root, criteriaQuery, cb) -> QueryHelp.getPredicate(root, updatelogQueryCriteria, cb)), pageable);
    }
}
