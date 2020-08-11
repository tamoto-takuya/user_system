package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Branch;
import com.example.demo.repository.BranchRepository;

@Service
public class BranchService {
	@Autowired
    private BranchRepository branchRepository;

    public List<Branch> findAll() {
        return branchRepository.findAll();
    }
}

