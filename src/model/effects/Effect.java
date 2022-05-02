package model.effects;

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
			Effect clone = (Effect) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
