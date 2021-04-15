package com.example.monopoly.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class GameDO {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private BigDecimal pOnePoints;
	
	private BigDecimal pTwoPoints;
	
	private String pOnePos;
	
	private String pTwoPos;
	
	private String pOneProperties;
	
	private String pTwoProperties;
	
	private String lastPlayedBy;
	
	private Integer turnNumber;
	
	@Transient
	private Boolean startCrossed = false;
	
	@Transient 
	private String messageToUi;
	
	@Transient
	private String rentOrBuyMessage;
	
	@Transient
	private Integer diceNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getpOnePoints() {
		return pOnePoints;
	}

	public void setpOnePoints(BigDecimal pOnePoints) {
		this.pOnePoints = pOnePoints;
	}

	public BigDecimal getpTwoPoints() {
		return pTwoPoints;
	}

	public void setpTwoPoints(BigDecimal pTwoPoints) {
		this.pTwoPoints = pTwoPoints;
	}

	public String getpOnePos() {
		return pOnePos;
	}

	public void setpOnePos(String pOnePos) {
		this.pOnePos = pOnePos;
	}

	public String getpTwoPos() {
		return pTwoPos;
	}

	public void setpTwoPos(String pTwoPos) {
		this.pTwoPos = pTwoPos;
	}

	public String getpOneProperties() {
		return pOneProperties;
	}

	public void setpOneProperties(String pOneProperties) {
		this.pOneProperties = pOneProperties;
	}

	public String getpTwoProperties() {
		return pTwoProperties;
	}

	public void setpTwoProperties(String pTwoProperties) {
		this.pTwoProperties = pTwoProperties;
	}

	public String getLastPlayedBy() {
		return lastPlayedBy;
	}

	public void setLastPlayedBy(String lastPlayedBy) {
		this.lastPlayedBy = lastPlayedBy;
	}

	public Boolean getStartCrossed() {
		return startCrossed;
	}

	public void setStartCrossed(Boolean startCrossed) {
		this.startCrossed = startCrossed;
	}

	public Integer getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(Integer turnNumber) {
		this.turnNumber = turnNumber;
	}

	public String getMessageToUi() {
		return messageToUi;
	}

	public void setMessageToUi(String messageToUi) {
		this.messageToUi = messageToUi;
	}

	public Integer getDiceNumber() {
		return diceNumber;
	}

	public void setDiceNumber(Integer diceNumber) {
		this.diceNumber = diceNumber;
	}

	public String getRentOrBuyMessage() {
		return rentOrBuyMessage;
	}

	public void setRentOrBuyMessage(String rentOrBuyMessage) {
		this.rentOrBuyMessage = rentOrBuyMessage;
	}
}
