package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield(int d) {
		super("Shield", d, EffectType.BUFF);
	}

	@Override
	void apply(Champion c) {


		c.setSpeed((int) (c.getSpeed()*1.02));
	}

	@Override
	void remove(Champion c) {
		double dom = c.getSpeed()*100*1.02;
		double nom = 1.02*120;
		c.setSpeed((int)(dom/nom));
	}
}
