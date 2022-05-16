package model.effects;

import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {


	public Disarm(int d) {
		super("Disarm", d, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c)  {
		DamagingAbility Punch = new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50);
		c.getAbilities().add(Punch);
	}
	@Override
	public void remove(Champion c) {
		RemoveEffect("Disarm",c);

		for (int i = 0; i < c.getAbilities().size(); i++) {
			if(c.getAbilities().get(i).getName().equals("Punch")) {
				c.getAbilities().remove(i);
				break;
			}
		}
	}

	public static void main(String[] args) {

	}
}
