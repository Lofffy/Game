package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int d) {
		super("Shock", d, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		c.getAppliedEffects().add(clone());

		c.setSpeed((int)(c.getSpeed()/1.1));
		c.setAttackDamage((int) (c.getAttackDamage()/1.1));

		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Shock",c);
		c.setSpeed((int)Math.round(c.getSpeed()*1.1));
		c.setAttackDamage((int)Math.round(c.getAttackDamage()*1.1));

		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);

	}
}
