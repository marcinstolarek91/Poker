package cards;

public class PlayerAI extends Player {
	private float bluffTendency; // 0.0 - 1.0
	private float riskSusceptibility; // 0.0 - 1.0
	private float seeFlopTendency; // 0.0 - 1.0

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
	public PlayersTurn goThroughTurn(int playerPosition, int playersNumber, int pot, int bet, int smallBlind, int startBet) {
		float positionWeight = 0.6F;
		float parametersVariation = 0.1F; // parameter = parameter +/- parametersVariation
		// TODO Auto-generated method stub
		return null;
	}

}
