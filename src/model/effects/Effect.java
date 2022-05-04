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

	Disarm s = new Disarm(4);


	@Override
	public Effect clone() {
		try {
			Effect clone = (Effect) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
	abstract void apply(Champion c);
	abstract void remove(Champion c);

}
