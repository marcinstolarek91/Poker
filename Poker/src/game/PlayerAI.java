package game;

import java.util.Random;

import cards.PlayersTurn;
import cards.ProbabilityChecker;
import cards.StartHands;
import cards.Turn;

public class PlayerAI extends Player {
	private float bluffTendency; // 0.0 - 1.0, 0.0 is playing with no bluff
	private float riskSusceptibility; // 0.0 - 1.0, standard is 0.5
	private float seeFlopTendency; // 0.0 - 1.0, standard is 0.5
	private static final int PAIR_POINTS = 5;
	private static final int TWO_PAIRS_POINTS = 25;
	private static final int THREE_POINTS = 60;
	private static final int STRAIGHT_POINTS = 110;
	private static final int FLUSH_POINTS = 140;
	private static final int FULL_HOUSE_POINTS = 180;
	private static final int FOUR_POINTS = 230;
	private static final int POKER_POINTS = 250;

	public PlayerAI(String name, int place) {
		super(name, place);
		bluffTendency = 0.5F;
		riskSusceptibility = 0.5F;
		seeFlopTendency = 0.5F;
	}
	
	public void setPlayerCharacteristics(float bluff, float risk, float seeFlop) {
		bluffTendency = bluff;
		riskSusceptibility = risk;
		seeFlopTendency = seeFlop;
	}
	
	@Override
	public PlayersTurn goThroughTurn(int cardsOnTable, int playerPosition, int playersNumber, int pot, int tableBet, int smallBlind, int startBet) {
		int newBet;
		if (cardsOnTable > 0)
			newBet = calculateBet(startBet, calculateMaxBet(pot, playersNumber, playerPosition), smallBlind);
		else // no flop
			newBet = calculateBet(startBet, calculateMaxStartBet(pot, playersNumber, playerPosition), smallBlind);
		if (newBet < tableBet) {
			active = false;
			return new PlayersTurn(Turn.FOLD);
		}
		else if (newBet == startBet && newBet == tableBet)
			return new PlayersTurn(Turn.CHECK);
		else if (newBet == tableBet) {
			if (newBet - startBet == chips) {
				chips = 0;
				activeAllIn = true;
				return new PlayersTurn(Turn.ALL_IN, newBet - startBet);
			}
			else {
				chips -= newBet - startBet;
				return new PlayersTurn(Turn.CALL, newBet - startBet);
			}
		}
		else {
			if (newBet - startBet == chips) {
				chips = 0;
				activeAllIn = true;
				return new PlayersTurn(Turn.ALL_IN, newBet - startBet);
			}
			else {
				chips -= newBet - startBet;
				return new PlayersTurn(Turn.RAISE, newBet - startBet);
			}
		}
		
	}
	
	private int calculateMaxStartBet(int pot, int opponentNumber, int playerPosition) {
		final float positionWeight = 0.5F;
		final float bluffVariation = 0.2F;
		float riskFactor = seeFlopTendency * 2.0F - positionWeight * (1.0F - ((float)playerPosition / (float)opponentNumber));
		float winPoints = 1.0F - StartHands.opponentHasBetterStartHand(cards.getOwnCards(), opponentNumber);
		float bluff = bluffTendency + ((new Random()).nextFloat() - 0.5F) * bluffVariation;
		if (bluff < 0.0F)
			bluff = 0.0F;
		if (riskFactor < 0.0F)
			riskFactor = 0.0F;
		winPoints *= riskFactor;
		winPoints += bluff;
		return (int) ((float) (pot * winPoints));
	}
	
	private float calculateHandPoints() {
		float handPoints;
		handPoints = (ProbabilityChecker.checkChanceToOwnPoker(cards.getCards(), cards.getOwnCards()) * (float)POKER_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnFour(cards.getCards(), cards.getOwnCards()) * (float)FOUR_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnFullHouse(cards.getCards(), cards.getOwnCards()) * (float)FULL_HOUSE_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnFlush(cards.getCards(), cards.getOwnCards()) * (float)FLUSH_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnStraight(cards.getCards(), cards.getOwnCards()) * (float)STRAIGHT_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnThree(cards.getCards(), cards.getOwnCards()) * (float)THREE_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnTwoPairs(cards.getCards(), cards.getOwnCards()) * (float)TWO_PAIRS_POINTS) / 1000;
		handPoints += (ProbabilityChecker.checkChanceToOwnPair(cards.getCards(), cards.getOwnCards()) * (float)PAIR_POINTS) / 1000;
		if (cards.getCards().size() < 6) {
			handPoints += (ProbabilityChecker.checkChanceToOwnPoker(cards.getCards(), cards.getOwnCards(), 1) * (float)POKER_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnFour(cards.getCards(), cards.getOwnCards(), 1) * (float)FOUR_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnFullHouse(cards.getCards(), cards.getOwnCards(), 1) * (float)FULL_HOUSE_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnFlush(cards.getCards(), cards.getOwnCards(), 1) * (float)FLUSH_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnStraight(cards.getCards(), cards.getOwnCards(), 1) * (float)STRAIGHT_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnThree(cards.getCards(), cards.getOwnCards(), 1) * (float)THREE_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnTwoPairs(cards.getCards(), cards.getOwnCards(), 1) * (float)TWO_PAIRS_POINTS) / 1000;
			handPoints += (ProbabilityChecker.checkChanceToOwnPair(cards.getCards(), cards.getOwnCards(), 1) * (float)PAIR_POINTS) / 1000;
			handPoints /= 2;
		}
		return handPoints;
	}
	
	private float calculateOpponentsBetterHand(int opponentNumbers) {
		double opponentChanceToBetterHand = (double) ProbabilityChecker.probabilityOpponentHasBetterHand(cards.getCards(), cards.getOwnCards());
		return 1.0F - (float) Math.pow(1.0 - opponentChanceToBetterHand, (double) opponentNumbers);
	}
	
	private int calculateMaxBet(int pot, int opponentNumber, int playerPosition) {
		final float positionWeight = 0.5F;
		final float bluffVariation = 0.2F;
		float handPoints = calculateHandPoints();
		float riskFactor = riskSusceptibility * 2.0F - positionWeight * (1.0F - (float) (playerPosition / opponentNumber));
		float winPoints = 1.0F - calculateOpponentsBetterHand(opponentNumber);
		float bluff = bluffTendency + ((new Random()).nextFloat() - 0.5F) * bluffVariation;
		if (bluff < 0.0F)
			bluff = 0.0F;
		if (riskFactor < 0.0F)
			riskFactor = 0.0F;
		winPoints += bluff;
		handPoints *= riskFactor;
		return (int) ((float) pot * (winPoints + handPoints));
	}
	
	private int calculateBet(int bet, int maxBet, int smallBlind) {
		int newBet;
		float betPercentage = (float) (new Random()).nextGaussian() + 0.8F; // calculate percentage of max bet
		if (betPercentage > 1.0F)
			betPercentage = 1.0F;
		else if (betPercentage < 0.0F)
			betPercentage = 0.0F;
		newBet = (int) (betPercentage * (float) maxBet);
		newBet -= newBet % smallBlind;
		if (newBet > chips + bet)
			newBet = chips + bet;
		if (newBet < bet)
			newBet = bet;
		return newBet;
	}

}
