package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.CategoryRepo;
import com.studia.biblioteka.dao.CopyRepo;
import com.studia.biblioteka.dao.entity.Category;
import com.studia.biblioteka.dao.entity.Copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CopyManager {
    private final CopyRepo copyRepo;
    @Autowired
    public CopyManager(CopyRepo copyRepo){
        this.copyRepo = copyRepo;
    }

    public Optional<Copy> findById(Long id) {
        return copyRepo.findById(id);
    }
    public Iterable<Copy> findAll() {
        return copyRepo.findAll();
    }

    public Copy save(Copy copy) {
        return copyRepo.save(copy);
    }
    public void delete(Long id) {
        copyRepo.deleteById(id);
    }
}
