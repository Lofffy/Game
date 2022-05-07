package model.effects;

import model.world.Champion;

abstract public class Effect implements Cloneable {

	private String name;
	private int duration;
	private EffectType type;

	public Effect(String n, int d, EffectType t) {
		name = n;
		duration = d;
		type = t;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public EffectType getType() {
		return type;
	}

	@Override
	public Effect clone() {
		try {
			return (Effect) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
	public static void RemoveEffect(String name, Champion c){
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if(c.getAppliedEffects().get(i).getName().equals(name)) {
				c.getAppliedEffects().remove(i);
				break;
			}
		}
	}
	abstract void apply(Champion c);
	abstract void remove(Champion c);

}
