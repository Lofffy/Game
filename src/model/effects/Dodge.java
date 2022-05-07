package model.effects;

import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int d) {
		super("Dodge", d, EffectType.BUFF);
	}

	@Override
	void apply(Champion c) {
		c.getAppliedEffects().add(clone());


		int speed = c.getSpeed();
		int pre = (int)(speed*(5.00/100.00));
		c.setSpeed(speed+pre);
	}

	@Override
	void remove(Champion c) {
		RemoveEffect("Dodge",c);

		double dom = c.getSpeed()*100*1.05;
		double nom = 1.05*105;
		c.setSpeed((int)(dom/nom));
	}
}
