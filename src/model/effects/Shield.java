package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield(int d) {
		super("Shield", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		c.getAppliedEffects().add(clone());
		c.setSpeed((int) (c.getSpeed()*1.02));
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Shield",c);
		double dom = c.getSpeed()*100*1.02;
		double nom = 1.02*120;
		c.setSpeed((int)(dom/nom));
	}
}
