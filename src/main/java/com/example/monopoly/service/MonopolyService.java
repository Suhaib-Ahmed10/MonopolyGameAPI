package com.example.monopoly.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.monopoly.domain.DataDO;
import com.example.monopoly.domain.GameDO;
import com.example.monopoly.repository.MonopolyDataRepository;
import com.example.monopoly.repository.MonopolyRepository;

@Service
public class MonopolyService {

	@Autowired
	MonopolyRepository monopolyRepository;
	
	@Autowired
	MonopolyDataRepository monopolyDataRepository;
	
	
	public void saveOrUpdate(GameDO baseDO)   
	{  
		monopolyRepository.save(baseDO);  
	} 
	
	public void delete(Integer id)   
	{  
		monopolyRepository.deleteById(id);  
	} 
	
	public GameDO getGame(){
		List<GameDO> gameDOs = new ArrayList<GameDO>();
				monopolyRepository.findAll().forEach(game -> gameDOs.add(game));
		return 0 < gameDOs.size()? gameDOs.get(gameDOs.size()-1): null;
	}
	
	public List<DataDO> getDataList(){
		List<DataDO> dataDOs = new ArrayList<>();
		monopolyDataRepository.findAll().forEach(data -> dataDOs.add(data));
		
		return dataDOs;
	}
	
	public GameDO deleteGame(){
		monopolyRepository.findAll().forEach(data -> monopolyRepository.delete(data));
		return new GameDO();
	}
}
