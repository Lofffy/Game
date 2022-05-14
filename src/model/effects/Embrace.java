package model.effects;

import model.world.Champion;

public class Embrace extends Effect {

	public Embrace(int d) {
		super("Embrace", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		c.getAppliedEffects().add(clone());
		c.setSpeed((int)(c.getSpeed()*1.2));
		c.setCurrentHP((int)(c.getMaxHP()*0.2+c.getCurrentHP()));
		c.setMana((int)(c.getMana()*1.2));
		c.setAttackDamage((int)(c.getAttackDamage()*1.2));
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Embrace",c);



		double dom = 100*1.2;
		double nom = 1.2*120;
		c.setSpeed((int)((c.getSpeed()*dom)/nom));
		c.setAttackDamage((int)((c.getAttackDamage()*dom)/nom));
	}
}
