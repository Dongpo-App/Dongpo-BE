package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStoreService {

    private final StoreRepository storeRepository;

    public List<Store> findAll(){
        return storeRepository.findAll();
    }
}
