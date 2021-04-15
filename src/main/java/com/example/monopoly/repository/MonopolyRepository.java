package com.example.monopoly.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.monopoly.domain.GameDO;

public interface MonopolyRepository extends CrudRepository<GameDO, Integer> {

}
