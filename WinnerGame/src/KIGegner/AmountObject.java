package KIGegner;

public class AmountObject {
	public final long amount;
	public final int round;

	public AmountObject(long amount, int round) {
		this.round = round;
		this.amount = amount;
	}

	@Override
	public String toString() {
		if (round < 10) {
			return "Runde:000" + round + " - Guthaben:" + amount;
		} else if (round < 100) {
			return "Runde:00" + round + " - Guthaben:" + amount;
		} else if (round < 1000) {
			return "Runde:0" + round + " - Guthaben:" + amount;
		} else {
			return "Runde:" + round + " - Guthaben:" + amount;
		}
	}

}
