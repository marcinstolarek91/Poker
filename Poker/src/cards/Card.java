package cards;

public final class Card implements Comparable<Card> {
	public FaceCard faceCard;
	public CardColor cardColor;
	
	public Card(FaceCard face, CardColor card) {
		faceCard = face;
		cardColor = card;
	}
	
	public void changeCard(Card newCard) {
		faceCard = newCard.faceCard;
		cardColor = newCard.cardColor;
	}
	
	public Card(int number) {
		if (number < 0 || number > 51)
			number = 0;
		switch (number % 4) {
			case 0: cardColor = CardColor.HEART; break;
			case 1: cardColor = CardColor.DIAMOND; break;
			case 2: cardColor = CardColor.CLUB; break;
			default: cardColor = CardColor.SPADE; break;
		}
		switch (number / 4) {
			case 0: faceCard = FaceCard.TWO; break;
			case 1: faceCard = FaceCard.THREE; break;
			case 2: faceCard = FaceCard.FOUR; break;
			case 3: faceCard = FaceCard.FIVE; break;
			case 4: faceCard = FaceCard.SIX; break;
			case 5: faceCard = FaceCard.SEVEN; break;
			case 6: faceCard = FaceCard.EIGHT; break;
			case 7: faceCard = FaceCard.NINE; break;
			case 8: faceCard = FaceCard.TEN; break;
			case 9: faceCard = FaceCard.JACK; break;
			case 10: faceCard = FaceCard.QUEEN; break;
			case 11: faceCard = FaceCard.KING; break;
			default: faceCard = FaceCard.ACE; break;
		}
	}
	
	public int getCardNumber() {
		return faceCard.ordinal() * 4 + cardColor.ordinal();
	}

	@Override
	public int compareTo(Card arg0) {
		if (faceCard.ordinal() > arg0.faceCard.ordinal())
			return 1;
		else if (faceCard.ordinal() < arg0.faceCard.ordinal())
			return -1;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardColor == null) ? 0 : cardColor.hashCode());
		result = prime * result + ((faceCard == null) ? 0 : faceCard.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (cardColor != other.cardColor)
			return false;
		if (faceCard != other.faceCard)
			return false;
		return true;
	}
}