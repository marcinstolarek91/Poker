package cards;

import java.util.List;

public abstract class StartHands {
	public static final int COMBINATIONS = 169;
	
	public static float opponentHasBetterStartHand(List<Card> ownCards, int opponentNumbers) {
		return 1.0F - (float) Math.pow(1.0 - (((double)ownCardStartPoints(ownCards) - 1) / (double)COMBINATIONS), (double) opponentNumbers);
	}
	
	public static int ownCardStartPoints(List<Card> ownCards) {
		if (HandChecker.getFigureType(ownCards) == PokerHandsType.PAIR)
			return pairsPoint(ownCards.get(0).faceCard.ordinal());
		else if (ownCards.get(0).cardColor.equals(ownCards.get(1).cardColor))
			return suitedPoints(ownCards);
		return unsuitedPoints(ownCards);
	}
	
	private static int pairsPoint(int ordinal) {
		switch (ordinal) {
			case 0: return 52; // TWO
			case 1: return 51;
			case 2: return 50;
			case 3: return 46;
			case 4: return 36; // SIX
			case 5: return 29;
			case 6: return 21;
			case 7: return 17;
			case 8: return 10; //TEN
			case 9: return 5;
			case 10: return 3;
			case 11: return 2;
			default: return 1; // ACE
		}
	}
	
	private static int suitedPoints(List<Card> ownCards) {
		int higher = (ownCards.get(0).faceCard.ordinal() > ownCards.get(1).faceCard.ordinal()) ? 0 : 1;
		int lower = (higher == 1) ? 0 : 1;
		if (ownCards.get(higher).faceCard == FaceCard.ACE) {
			if (ownCards.get(lower).faceCard == FaceCard.KING)
				return 4;
			else if (ownCards.get(lower).faceCard == FaceCard.QUEEN)
				return 6;
			else if (ownCards.get(lower).faceCard == FaceCard.JACK)
				return 8;
			else if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 12;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 19;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 24;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 30;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 34;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 28;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 32;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 33;
			else
				return 39;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.KING) {
			if (ownCards.get(lower).faceCard == FaceCard.QUEEN)
				return 7;
			else if (ownCards.get(lower).faceCard == FaceCard.JACK)
				return 9;
			else if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 14;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 22;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 37;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 44;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 50;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 55;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 58;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 59;
			else
				return 60;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.QUEEN) {
			if (ownCards.get(lower).faceCard == FaceCard.JACK)
				return 13;
			else if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 15;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 25;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 43;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 61;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 66;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 69;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 71;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 72;
			else
				return 75;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.JACK) {
			if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 16;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 26;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 41;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 64;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 79;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 82;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 86;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 87;
			else
				return 89;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.TEN) {
			if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 23;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 38;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 57;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 74;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 93;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 95;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 96;
			else
				return 98;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.NINE) {
			if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 40;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 54;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 68;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 88;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 106;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 107;
			else
				return 111;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.EIGHT) {
			if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 48;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 62;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 78;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 94;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 116;
			else
				return 118;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.SEVEN) {
			if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 56;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 67;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 85;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 103;
			else
				return 120;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.SIX) {
			if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 63;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 70;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 90;
			else
				return 110;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.FIVE) {
			if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 65;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 77;
			else
				return 92;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.FOUR) {
			if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 84;
			else
				return 97;
		}
		else // THREE + TWO
			return 105;
	}
	
	private static int unsuitedPoints(List<Card> ownCards) {
		int higher = (ownCards.get(0).faceCard.ordinal() > ownCards.get(1).faceCard.ordinal()) ? 0 : 1;
		int lower = (higher == 1) ? 0 : 1;
		if (ownCards.get(higher).faceCard == FaceCard.ACE) {
			if (ownCards.get(lower).faceCard == FaceCard.KING)
				return 11;
			else if (ownCards.get(lower).faceCard == FaceCard.QUEEN)
				return 18;
			else if (ownCards.get(lower).faceCard == FaceCard.JACK)
				return 27;
			else if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 42;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 76;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 91;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 102;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 113;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 101;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 104;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 109;
			else
				return 117;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.KING) {
			if (ownCards.get(lower).faceCard == FaceCard.QUEEN)
				return 20;
			else if (ownCards.get(lower).faceCard == FaceCard.JACK)
				return 31;
			else if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 45;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 81;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 112;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 122;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 125;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 128;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 132;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 133;
			else
				return 135;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.QUEEN) {
			if (ownCards.get(lower).faceCard == FaceCard.JACK)
				return 35;
			else if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 49;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 83;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 115;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 131;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 137;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 141;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 143;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 144;
			else
				return 146;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.JACK) {
			if (ownCards.get(lower).faceCard == FaceCard.TEN)
				return 47;
			else if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 80;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 108;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 129;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 147;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 149;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 152;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 153;
			else
				return 155;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.TEN) {
			if (ownCards.get(lower).faceCard == FaceCard.NINE)
				return 73;
			else if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 100;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 124;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 140;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 157;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 158;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 160;
			else
				return 162;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.NINE) {
			if (ownCards.get(lower).faceCard == FaceCard.EIGHT)
				return 99;
			else if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 119;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 134;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 150;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 164;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 165;
			else
				return 166;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.EIGHT) {
			if (ownCards.get(lower).faceCard == FaceCard.SEVEN)
				return 114;
			else if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 126;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 139;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 156;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 167;
			else
				return 168;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.SEVEN) {
			if (ownCards.get(lower).faceCard == FaceCard.SIX)
				return 121;
			else if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 130;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 145;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 161;
			else
				return 169;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.SIX) {
			if (ownCards.get(lower).faceCard == FaceCard.FIVE)
				return 123;
			else if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 136;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 148;
			else
				return 163;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.FIVE) {
			if (ownCards.get(lower).faceCard == FaceCard.FOUR)
				return 127;
			else if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 138;
			else
				return 151;
		}
		else if (ownCards.get(higher).faceCard == FaceCard.FOUR) {
			if (ownCards.get(lower).faceCard == FaceCard.THREE)
				return 142;
			else
				return 154;
		}
		else // THREE + TWO
			return 159;
	}
}
