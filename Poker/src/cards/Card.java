package cards;

public class Card implements Comparable<Card> {
	FaceCard faceCard;
	CardColor cardColor;
	
	public Card(FaceCard face, CardColor card) {
		faceCard = face;
		cardColor = card;
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
