package com.example.monopoly.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.monopoly.domain.DataDO;

public interface MonopolyDataRepository extends CrudRepository<DataDO, Integer>{

}
