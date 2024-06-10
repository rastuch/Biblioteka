package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.FineRepo;
import com.studia.biblioteka.dao.entity.Fine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FineManager {
    private final FineRepo fineRepo;
    @Autowired
    public FineManager(FineRepo fineRepo){
        this.fineRepo = fineRepo;
    }

    public Optional<Fine> findById(Long id) {
        return fineRepo.findById(id);
    }
    public Iterable<Fine> findAll() {
        return fineRepo.findAll();
    }
    public Iterable<Fine> findAllByUserId(Long userId) {
        return fineRepo.findAllByUserId(userId);
    }

    public Fine save(Fine fine) {
        return fineRepo.save(fine);
    }
    public void delete(Long id) {
        fineRepo.deleteById(id);
    }
}
