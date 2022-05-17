package model.effects;

import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp(int d) {
		super("PowerUp", d, EffectType.BUFF);
	}

	@Override
	public void apply(Champion c){
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

		for (int i = 0; i < c.getAbilities().size(); i++) {
			if(c.getAbilities().get(i)instanceof HealingAbility){
				((HealingAbility) c.getAbilities().get(i)).setHealAmount((int) ((((HealingAbility) c.getAbilities().get(i)).getHealAmount()/1.2)));
			}else if(c.getAbilities().get(i)instanceof DamagingAbility)
				((DamagingAbility) c.getAbilities().get(i)).setDamageAmount((int) ((((DamagingAbility)c.getAbilities().get(i)).getDamageAmount()/1.2)));
		}
	}
}
