package model.effects;

import model.world.Champion;

public class Embrace extends Effect {

	public Embrace(int d) {
		super("Embrace", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c)  {
		c.setSpeed((int)(c.getSpeed()*1.2));
		c.setCurrentHP((int)((c.getMaxHP()*0.2)+c.getCurrentHP()));
		c.setMana((int)(c.getMana()*1.2));
		c.setAttackDamage((int)(c.getAttackDamage()*1.2));
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("Embrace",c);
		c.setSpeed((int) (c.getSpeed()/1.2));
		c.setAttackDamage((int) (c.getAttackDamage()/1.2));
	}
}
