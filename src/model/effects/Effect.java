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
	public Object clone() throws CloneNotSupportedException {
			return  super.clone();

	}
	public static void RemoveEffect(String name, Champion c){
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if(c.getAppliedEffects().get(i).getName().equals(name)) {
				c.getAppliedEffects().remove(i);
				break;
			}
		}
	}
	public abstract void apply(Champion c) throws CloneNotSupportedException;
	public abstract void remove(Champion c);

}
