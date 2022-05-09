package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect {

	public SpeedUp(int d) {
		super("SpeedUp", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		c.getAppliedEffects().add(clone());
		c.setSpeed((int) (c.getSpeed()*1.15));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("SpeedUp",c);
		double dom = c.getSpeed()*100*1.15;
		double nom = 1.15*150;
		c.setSpeed((int)(dom/nom));
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
	}
}
