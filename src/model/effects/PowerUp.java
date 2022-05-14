package model.effects;

import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp(int d) {
		super("PowerUp", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) throws CloneNotSupportedException {
		c.getAppliedEffects().add(clone());
		for (int i = 0; i < c.getAbilities().size(); i++) {
			if(c.getAbilities().get(i)instanceof HealingAbility){
				((HealingAbility) c.getAbilities().get(i)).setHealAmount((int) (((HealingAbility) c.getAbilities().get(i)).getHealAmount()*1.2));
			}else if(c.getAbilities().get(i)instanceof DamagingAbility)
				((DamagingAbility) c.getAbilities().get(i)).setDamageAmount((int) (((DamagingAbility) c.getAbilities().get(i)).getDamageAmount()*1.2));
		}
	}

	@Override
	public void remove(Champion c) {
		RemoveEffect("PowerUp",c);

		double dom = 100*1.2;
		double nom = 1.2*120;
		for (int i = 0; i < c.getAbilities().size(); i++) {
			if(c.getAbilities().get(i)instanceof HealingAbility){
				((HealingAbility) c.getAbilities().get(i)).setHealAmount((int) ((((HealingAbility) c.getAbilities().get(i)).getHealAmount()*dom)/nom));
			}else if(c.getAbilities().get(i)instanceof DamagingAbility)
				((DamagingAbility) c.getAbilities().get(i)).setDamageAmount((int) ((((DamagingAbility) c.getAbilities().get(i)).getDamageAmount()*dom)/nom));
		}
	}
}
