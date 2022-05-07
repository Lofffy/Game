package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int d) {
		super("Shock", d, EffectType.DEBUFF);
	}

	@Override
	void apply(Champion c) {
		c.getAppliedEffects().add(clone());
		double dom = c.getSpeed()*100*1.1;
		double nom = 1.1*110;
		c.setSpeed((int)(dom/nom));

		double dom2 = c.getAttackDamage()*100*1.1;
		double nom2 = 1.1*110;
		c.setAttackDamage((int)(dom2/nom2));

		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
	}

	@Override
	void remove(Champion c) {
		RemoveEffect("Shock",c);
		c.setSpeed((int)(c.getSpeed()*1.1));
		c.setAttackDamage((int)(c.getAttackDamage()*1.1));

		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);

	}
}
