package com.example.monopoly.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.monopoly.domain.DataDO;
import com.example.monopoly.domain.GameDO;
import com.example.monopoly.service.MonopolyService;
import com.google.gson.Gson;
//import com.google.gson.Gson;

@RestController
@RequestMapping("/api")
public class RestControllerR {
	
	@Autowired
	private MonopolyService monopolyService;
	
	@Autowired
	private Gson gson;

	private static final BigDecimal STARTBONUS = new BigDecimal(200);
	private static final BigDecimal STARTPOINTS = new BigDecimal(1000);
	private static final String START = new String("START");
	private List<DataDO> dataDos = new ArrayList<>();
	private GameDO gameDo = new GameDO();
	Random rand = new Random();
	Map<String, Integer> orderingMap = new HashMap<>();
	Integer maxLen = null;
	
	@GetMapping( value="/getroll")
	public ResponseEntity<String> rollDie(){
		System.out.println("Successfully Returned");
//		gameDo.setId(1);
//		gameDo.setLastPlayedBy("Suhaib");
//		gameDo.setpOneProperties("Bumnas");
//		monopolyService.saveOrUpdate(gameDo);
		return new ResponseEntity<String>("Succefully created", HttpStatus.OK);		
	}
	
	@GetMapping( value="/create-game")
	public void createGame(HttpServletRequest request, HttpServletResponse response){
		
		try {
			dataDos = monopolyService.getDataList(); // get the Data from DB 
			gameDo = monopolyService.deleteGame();
			//clear the data from DB and start a new game
			orderingMap.clear();
			gameDo.setpOnePoints(STARTPOINTS);
			gameDo.setpTwoPoints(STARTPOINTS);
			gameDo.setpOnePos(START);
			gameDo.setpTwoPos(START);
			
			orderingMap.put(START, 0);
			dataDos.forEach(x ->{
				orderingMap.put(x.getPlaceId(), orderingMap.size());
			});
			maxLen = orderingMap.size();
			
			monopolyService.saveOrUpdate(gameDo);
			// gameDo save in DB;
			
			response.getWriter().write(gson.toJson(gameDo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	@GetMapping( value="/resume-game")
	public void resumeGame(HttpServletRequest request, HttpServletResponse response){
		
		try {
			dataDos = monopolyService.getDataList(); // get the Data from DB 
			gameDo = monopolyService.getGame();
			//get the last game from DB, if no data available then send new object
			
			orderingMap.put(START, 0);
			dataDos.forEach(x ->{
				orderingMap.put(x.getPlaceId(), orderingMap.size()+1);
			});
			maxLen = dataDos.size();
			
			response.getWriter().write(gson.toJson(gameDo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping( value="/roll-die/{player}")
	public void rollDice( @PathVariable("player") String player, HttpServletRequest request, HttpServletResponse response)
	{
		try {
			Integer diceNumber = rand.nextInt(11)+2; 
			// It will generate random number between 0 to 10. Adding 2 to make it 2 to 12;
			Integer currentPos = null;
			Integer total = 0;
			
			String position = "";
			String pOneProps = ""; String pTwoProps = "";
			BigDecimal pOnePoint = null; BigDecimal pTwoPoint = null;
			
			String lost = null; String won = null;
//			gameDo = monopolyService.getGame();
			if("p1".equalsIgnoreCase(player)){
				position = gameDo.getpOnePos();
				pOneProps = gameDo.getpOneProperties();
				pTwoProps = gameDo.getpTwoProperties();
				pOnePoint = gameDo.getpOnePoints();
				pTwoPoint = gameDo.getpTwoPoints();
			}
			else{
				position = gameDo.getpTwoPos();
				pOneProps = gameDo.getpTwoProperties();
				pTwoProps = gameDo.getpOneProperties();
				pOnePoint = gameDo.getpTwoPoints();
				pTwoPoint = gameDo.getpOnePoints();
			}
			
			gameDo.setStartCrossed(false);// resetting the start crossed value
			currentPos = orderingMap.get(position);
			total = currentPos + diceNumber;
			position = setGameDO(total, player);
			if(gameDo.getStartCrossed()){
				pOnePoint = pOnePoint.add(STARTBONUS);
			}
			gameDo.setRentOrBuyMessage("You landed on "+ getPlaceName(position));
			if(!"START".equals(position))
			{
					if(null == pTwoProps|| (null != pTwoProps && pTwoProps.indexOf(position) == -1))
				{
					// If this condition is true then either the place is Unclaimed or Owned by P1.
					if(null == pOneProps || (null != pOneProps && pOneProps.indexOf(position) == -1)){
						// If this condition is true, then the place is unclaimed.
						BigDecimal buyPrice = getPriceOrRent("PRICE", position);
						pOnePoint = pOnePoint.subtract(buyPrice);
						pOneProps = addProperty(position, pOneProps);
						// property purchased
						gameDo.setRentOrBuyMessage("You purchased "+ getPlaceName(position) +" for $"+buyPrice);
					}
				}
				else{
					// This else block means player has to pay rent as the place is owned by another player
					BigDecimal rent = getPriceOrRent("RENT", position);
					pOnePoint = pOnePoint.subtract(rent);
					pTwoPoint = pTwoPoint.add(rent);
					// Rent paid
					gameDo.setRentOrBuyMessage("Rent paid $"+rent);
				}
			}
				
			if(0 > pOnePoint.intValue()){
				lost = player;
			}else if(null != gameDo.getTurnNumber() && 49 == gameDo.getTurnNumber().intValue())
			{
				// checking winner before 50th turn
				if(-1 < pOnePoint.compareTo(pTwoPoint))
					won = "p1".equals(player)?"p2": "p1";
				else
					won = "p1".equals(player)?"p1": "p2";
			}
			
			if("p1".equalsIgnoreCase(player)){
				gameDo.setpOnePos(position);
				gameDo.setpOneProperties(pOneProps);
				gameDo.setpTwoProperties(pTwoProps);
				gameDo.setpOnePoints(pOnePoint);
				gameDo.setpTwoPoints(pTwoPoint);
			}
			else{
				gameDo.setpTwoPos(position);
				gameDo.setpTwoProperties(pOneProps);
				gameDo.setpOneProperties(pTwoProps);
				gameDo.setpTwoPoints(pOnePoint);
				gameDo.setpOnePoints(pTwoPoint);
			}
			gameDo.setLastPlayedBy(player);
			
			monopolyService.saveOrUpdate(gameDo);
			
			if(null != lost){
				gameDo.setMessageToUi("Game Over, You lose!");
			}
			else if(null != won){
				gameDo.setMessageToUi("Congratulations, "+won+" wins!");
			}
			gameDo.setDiceNumber(diceNumber);
			// setting the message in case of win or lose.
			// Message will be checked in UI and if it not null, then we will display the message and stop the game.
				response.getWriter().write(gson.toJson(gameDo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getPlaceName(String position) {
		AtomicReference<String> placeName = new AtomicReference<>("Start");
		dataDos.forEach(x ->{
			if(position.equals(x.getPlaceId()))
				placeName.set(x.getPlaceName());
		});
		return placeName.get();
	}

	private String addProperty(String pos, String properties) 
	{
		return null != properties? properties+"$"+pos : pos;
	}

	private BigDecimal getPriceOrRent(String string, String place) 
	{
		AtomicReference<BigDecimal> priceOrRent = new AtomicReference<>();
		if(null != string && null != place)
		{
			dataDos.forEach(x -> {
				if(place.equals(x.getPlaceId())){
					if("PRICE".equals(string))
						priceOrRent.set(x.getBuyPrice());
					else
						priceOrRent.set(x.getRent());
				}
			});
		}
		return priceOrRent.get();
	}

	public String setGameDO(Integer total, String player){
		AtomicReference<Integer> num = new AtomicReference<>();
		AtomicReference<String> position = new AtomicReference<>();
		if(maxLen <= total){
			gameDo.setStartCrossed(true);
			num.set(total - maxLen);
		}
		else
			num.set(total);
		orderingMap.entrySet().forEach(k ->{
			if(num.get().intValue() == k.getValue().intValue())
				position.set(k.getKey());
		});
		gameDo.setTurnNumber(null != gameDo.getTurnNumber()? gameDo.getTurnNumber()+1:1);
		// incrementing turn number to declare winner before turn 50
		return position.get();
	}
}
// code practice for circulation of places along with start.
//public static void main(String[] args) {
//	Random rand = new Random();
//	Map<String, Integer> map = new HashMap<String, Integer>();
//	map.put("START", 0);
//	map.put("A", 1);
//	map.put("B", 2);
//	map.put("C", 3);
//	map.put("D", 4);
//	map.put("E", 5);
//	
////	int num = rand.nextInt(6)+1;
//	int num = 2;
////	System.out.println(num);
//	
//	Integer mxLen = 6;
//	int crLen = 1;
//	Integer total = num+crLen;
////	System.out.println(total);
//	if(mxLen > total) {
//		map.entrySet().forEach(k ->{
//			if((total) == k.getValue().intValue())
//				System.out.println(k.getKey());
//		});
//	}
////	else if(mxLen == total) {
////		map.entrySet().forEach(k ->{
////			if((total-1) == k.getValue().intValue())
////				System.out.println(k.getKey());
////		});
////	}
//	else {
//		map.entrySet().forEach(k ->{
//			if((total-mxLen) == k.getValue().intValue())
//				System.out.println(k.getKey());
//		});
//	}
//	System.out.println(total-mxLen);
//}
