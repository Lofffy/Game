package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import exceptions.*;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.*;
import model.world.*;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

public class Game {

	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed ;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player p1, Player p2) {
		this.firstLeaderAbilityUsed = false;
		this.secondLeaderAbilityUsed = false;
		this.firstPlayer = p1;
		this.secondPlayer = p2;
		turnOrder = new PriorityQueue(6);
		board = new Object[BOARDHEIGHT][BOARDWIDTH];
		placeChampions();
		placeCovers();
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	public static void loadChampions(String filepath) throws IOException {
		availableChampions = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals("H")) {
				Hero h = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			} else if (content[0].equals("V")) {
				Villain h = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			} else if (content[0].equals("A")) {
				AntiHero h = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				h.getAbilities().add(getAbilityFromAvailable(content[8]));
				h.getAbilities().add(getAbilityFromAvailable(content[9]));
				h.getAbilities().add(getAbilityFromAvailable(content[10]));
				availableChampions.add(h);
			}
			line = br.readLine();
		}
		br.close();
	}
	private static Ability getAbilityFromAvailable(String string) {
		for (Ability ability : availableAbilities) {
			if (ability.getName().equals(string))
				return ability;
		}
		return null;
	}
	public static void loadAbilities(String filepath) throws IOException {
		availableAbilities = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals("CC")) {
				availableAbilities.add(new CrowdControlAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), getEffect(content[7], Integer.parseInt(content[8]))));
			} else if (content[0].equals("DMG")) {
				availableAbilities.add(new DamagingAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), Integer.parseInt(content[7])));
			} else if (content[0].equals("HEL")) {
				availableAbilities.add(new HealingAbility(content[1], Integer.parseInt(content[2]),
						Integer.parseInt(content[4]), Integer.parseInt(content[3]), AreaOfEffect.valueOf(content[5]),
						Integer.parseInt(content[6]), Integer.parseInt(content[7])));
			}
			line = br.readLine();
		}
		br.close();
	}
	private static Effect getEffect(String name, int duration) {
		if (name.equals("Dodge"))
			return new Dodge(duration);
		if (name.equals("Disarm"))
			return new Disarm(duration);
		if (name.equals("Embrace"))
			return new Embrace(duration);
		if (name.equals("Stun"))
			return new Stun(duration);
		if (name.equals("Shield"))
			return new Shield(duration);
		if (name.equals("Shock"))
			return new Shock(duration);
		if (name.equals("PowerUp"))
			return new PowerUp(duration);
		if (name.equals("SpeedUp"))
			return new SpeedUp(duration);
		if (name.equals("Silence"))
			return new Silence(duration);
		if (name.equals("Root"))
			return new Root(duration);
		return null;
	}
	public void placeChampions() {
		for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
			board[0][i + 1] = firstPlayer.getTeam().get(i);
			firstPlayer.getTeam().get(i).setLocation(new Point(0, i + 1));
			;
		}
		for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
			board[4][i + 1] = secondPlayer.getTeam().get(i);
			secondPlayer.getTeam().get(i).setLocation(new Point(4, i + 1));
			;
		}
	}
	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = (int) (Math.random() * 3) + 1;
			int y = (int) (Math.random() * 5);
			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}
	}
	public Champion getCurrentChampion() {
		prepareChampionTurns();
		return (Champion) this.turnOrder.peekMin();
	}
	public Player checkGameOver() {
		if(firstPlayer.getTeam().isEmpty())
			return secondPlayer;
		if(secondPlayer.getTeam().isEmpty())
			return firstPlayer;
		return null;
	}
	public void move(Direction d) throws UnallowedMovementException, NotEnoughResourcesException {
		if (d == Direction.UP) {
			if (getCurrentChampion().getLocation().x != 4) {
				if (board[getCurrentChampion().getLocation().x + 1][getCurrentChampion().getLocation().y] == null && getCurrentChampion().getCondition() != Condition.ROOTED ) {
					if (getCurrentChampion().getCurrentActionPoints() != 0) {
						Point p = new Point(getCurrentChampion().getLocation().x + 1, getCurrentChampion().getLocation().y);
						getCurrentChampion().setLocation(p);
						getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
						//board[getCurrentChampion().getLocation().x ][getCurrentChampion().getLocation().y] = null;
						board[getCurrentChampion().getLocation().x ][getCurrentChampion().getLocation().y] = getCurrentChampion();
					}else
						throw new NotEnoughResourcesException();
				}else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");

		} else if (d == Direction.DOWN) {
			if (getCurrentChampion().getLocation().x != 0) {
				if (board[getCurrentChampion().getLocation().x - 1][getCurrentChampion().getLocation().y] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x - 1, getCurrentChampion().getLocation().y);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					//board[getCurrentChampion().getLocation().x ][getCurrentChampion().getLocation().y] = null;
					board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");

		} else if (d == Direction.LEFT) {
			if (getCurrentChampion().getLocation().y != 0) {
				if (board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y - 1] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x, getCurrentChampion().getLocation().y - 1);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					//board[getCurrentChampion().getLocation().x ][getCurrentChampion().getLocation().y] = null;
					board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y ] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");

		} else if (d == Direction.RIGHT) {
			if (getCurrentChampion().getLocation().y != 4) {
				if (board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y + 1] == null && getCurrentChampion().getCondition() != Condition.ROOTED && getCurrentChampion().getCurrentActionPoints() != 0) {
					Point p = new Point(getCurrentChampion().getLocation().x, getCurrentChampion().getLocation().y + 1);
					getCurrentChampion().setLocation(p);
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - 1);
					//board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y] = null;
					board[getCurrentChampion().getLocation().x][getCurrentChampion().getLocation().y ] = getCurrentChampion();
				} else
					throw new UnallowedMovementException("You're not allowed to move there");
			} else
				throw new UnallowedMovementException("You're not allowed to move there");
		}
	}
	public static boolean checkShield(Champion c){
		for (int i = 0; i < c.getAppliedEffects().size(); i++) {
			if(c.getAppliedEffects().get(i).getName().equals("Shield")){
				c.getAppliedEffects().get(i).remove(c);
				return true;
			}
		}
		return false;
	}

	public boolean IsDodge (Champion champion) {
		int check = 0;
		for (int j = 0; j < champion.getAppliedEffects().size(); j++) {
			if (champion.getAppliedEffects().get(j) instanceof Dodge) {
				check = (int) (Math.random() * 2);
				return check == 1;
			}
		}
		return false;
	}
	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException {

		for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
			if (getCurrentChampion().getAppliedEffects().get(i) instanceof Disarm)
				throw new ChampionDisarmedException();
		}

		if (getCurrentChampion().getCurrentActionPoints() <= 1)
			throw new NotEnoughResourcesException();
		getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints()-2);
		Damageable target;
		switch (d) {
			case UP -> {
				target = helpAttack(getCurrentChampion(), 1, 0);
				if (target != null) {
					if (target instanceof Cover) {
						target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
						checkCondition(target);
					} else if (!checkShield((Champion) target) && !IsDodge((Champion) target)) {
						if (getCurrentChampion() instanceof Hero) {
							if (target instanceof Hero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else if (getCurrentChampion() instanceof Villain) {
							if (target instanceof Villain) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else {
							if (target instanceof AntiHero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						}
					}
				}
			}
			case DOWN -> {
				target = helpAttack(getCurrentChampion(), -1, 0);
				if (target != null) {
					if (target instanceof Cover) {
						target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
						checkCondition(target);
						return;
					} else if (!checkShield((Champion) target) && !IsDodge((Champion) target)) {
						if (getCurrentChampion() instanceof Hero) {
							if (target instanceof Hero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else if (getCurrentChampion() instanceof Villain) {
							if (target instanceof Villain) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else {
							if (target instanceof AntiHero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						}
					}
				}
			}
			case RIGHT -> {
				target = helpAttack(getCurrentChampion(), 0, 1);
				if (target != null) {
					if (target instanceof Cover) {
						target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
						checkCondition(target);
						return;
					} else if (!checkShield((Champion) target) && !IsDodge((Champion) target)) {
						if (getCurrentChampion() instanceof Hero) {
							if (target instanceof Hero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else if (getCurrentChampion() instanceof Villain) {
							if (target instanceof Villain) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else {
							if (target instanceof AntiHero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						}
					}
				}
			}
			case LEFT -> {
				target = helpAttack(getCurrentChampion(), 0, -1);
				if (target != null) {
					if (target instanceof Cover) {
						target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
						checkCondition(target);
					} else if(!checkShield((Champion) target) && !IsDodge((Champion) target)){
						if (getCurrentChampion() instanceof Hero) {
							if (target instanceof Hero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else if (getCurrentChampion() instanceof Villain) {
							if (target instanceof Villain) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((int) (target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						} else {
							if (target instanceof AntiHero) {
								target.setCurrentHP(target.getCurrentHP() - getCurrentChampion().getAttackDamage());
								checkCondition(target);
							} else {
								target.setCurrentHP((target.getCurrentHP() - ((int) (getCurrentChampion().getAttackDamage() * 1.5))));
								checkCondition(target);
							}
						}
					}
				}
			}
		}
		checkGameOver();
	}
	public  void checkCondition(Damageable target) {
		if (target instanceof Champion) {
			if (target.getCurrentHP() == 0) {
				((Champion) target).setCondition(Condition.KNOCKEDOUT);
				getBoard()[target.getLocation().x][target.getLocation().y] = null;
				if(firstPlayer.getTeam().contains((Champion) target))
					firstPlayer.getTeam().remove(target);
				else
					secondPlayer.getTeam().remove((Champion) target);
			}
		}else if (target.getCurrentHP() == 0)
			getBoard()[target.getLocation().x][target.getLocation().y] = null;

		checkGameOver();
		ChangeQ();
	}
	public  ArrayList<Damageable> findTarget(Champion champion, int x, int y,int z){
		ArrayList<Damageable> target = new ArrayList<>();
		boolean Up = false, Down = false, Left = false, Right = false;
		if (x == 1)
			Up = true;
		if (x == -1)
			Down = true;
		if (y == 1)
			Right = true;
		if (y == -1)
			Left = true;
		for (int i = 0; i < z; i++) {
			if (champion.getLocation().x + x > getBoardheight() - 1 || champion.getLocation().x + x < 0 || champion.getLocation().y + y > getBoardwidth() - 1 || champion.getLocation().y + y < 0)
				break;
			if (getBoard()[champion.getLocation().x + x][getCurrentChampion().getLocation().y + y] instanceof Damageable) {
				target.add((Damageable) getBoard()[getCurrentChampion().getLocation().x + x][getCurrentChampion().getLocation().y + y]);
			}
			if (Up)
				x += 1;
			if (Down)
				x -= 1;
			if (Right)
				y += 1;
			if (Left)
				y -= 1;
		}
		return target ;
	}
	public Damageable helpAttack(Champion champion, int x, int y) {
		ArrayList<Damageable> target = findTarget(champion,  x,  y,champion.getAttackRange());
		for (int i = 0; i < target.size(); i++) {
			if (target.get(i) instanceof Champion) {
				if (firstPlayer.getTeam().contains(champion)) {
					if (firstPlayer.getTeam().contains((Champion) target.get(i))) {
						target.remove(i);
						i--;
					} else
						return target.get(i);
				} else {
					if (secondPlayer.getTeam().contains((Champion) target.get(i))) {
						target.remove(i);
						i--;
					} else
						return target.get(i);
				}
			}else
				return target.get(i);
		}
		return null;
	}
	public ArrayList<Damageable> validTarget(Champion champion) { // Surround Help
		ArrayList<Damageable> validT = new ArrayList<>();
		for (int i = 0; i < getBoardheight(); i++) {
			for (int j = 0; j < getBoardwidth(); j++) {
				if (Math.abs(champion.getLocation().x - i) == 1 && Math.abs(champion.getLocation().y - j) == 1 && getBoard()[i][j] != null)
					validT.add((Damageable) getBoard()[i][j]);
				if (Math.abs(champion.getLocation().x - i) == 1 && Math.abs(champion.getLocation().y - j) == 0 && getBoard()[i][j] != null)
					validT.add((Damageable) getBoard()[i][j]);
				if (Math.abs(champion.getLocation().x - i) == 0 && Math.abs(champion.getLocation().y - j) == 1 && getBoard()[i][j] != null)
					validT.add((Damageable) getBoard()[i][j]);
			}
		}
		return validT;
	}
	public void castAbility(Ability a) throws NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException, AbilityUseException {
		if (a.getManaCost() <= getCurrentChampion().getMana() && a.getRequiredActionPoints() <= getCurrentChampion().getCurrentActionPoints()) {
			if (a.getCurrentCooldown() == 0) {
				for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
					if (getCurrentChampion().getAppliedEffects().get(i).getName().equals("Silence"))
						throw new AbilityUseException();
				}
			} else
				throw new AbilityUseException();
		} else
			throw new NotEnoughResourcesException();

		ArrayList<Damageable> target = new ArrayList<>();

		switch (a.getCastArea()) {
			case SELFTARGET -> {
				if (a instanceof HealingAbility) {
					target.add(getCurrentChampion());
					a.execute(target);
					getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
				}else if(a instanceof CrowdControlAbility){
					if(((CrowdControlAbility) a).getEffect().getType().equals(EffectType.BUFF)){
						target.add(getCurrentChampion());
						a.execute(target);
						getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
						getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());

					}
				}
			}
			case SURROUND -> {
				target = validTarget(getCurrentChampion());
				if (a instanceof DamagingAbility) {
					checkTeamEnemy(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Champion) {
							if (checkShield((Champion) target.get(i))) {
								target.remove(i);
								i--;
							}
						}
					}
					a.execute(target);
					getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
					for (Damageable damageable : target) {
						checkCondition(damageable);
					}
				}
				if (a instanceof CrowdControlAbility) {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.DEBUFF)) {
						checkTeamEnemy(getCurrentChampion(), target);
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i) instanceof Champion) {
								if (checkShield((Champion) target.get(i))) {
									target.remove(i);
									i--;
								}
							}
						}
					} else
						checkTeamSame(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Cover) {
							target.remove(i);
							i--;
						}
					}

					a.execute(target);
					getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
					for (Damageable damageable : target) {
						checkCondition(damageable);
					}
				}
				if (a instanceof HealingAbility) {
					checkTeamSame(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Cover) {
							target.remove(i);
							i--;
						}
					}
					if (target.size() > 0) {
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i) instanceof Champion) {
								if (checkShield((Champion) target.get(i))) {
									target.remove(i);
									i--;
								}
							}
						}
						a.execute(target);
						getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
						getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
						for (Damageable damageable : target) {
							checkCondition(damageable);
						}
					}
				}
			}
			case TEAMTARGET -> {
				if (a instanceof DamagingAbility) {
					if (firstPlayer.getTeam().contains(getCurrentChampion())) {
						target = inOrOut(getCurrentChampion(), a, secondPlayer);
					} else {
						target = inOrOut(getCurrentChampion(), a, firstPlayer);
					}
				} else if (a instanceof HealingAbility) {
					if (firstPlayer.getTeam().contains(getCurrentChampion())) {
						target = inOrOut(getCurrentChampion(), a, firstPlayer);
					} else
						target = inOrOut(getCurrentChampion(), a, secondPlayer);
				} else {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.BUFF)) {
						if (firstPlayer.getTeam().contains(getCurrentChampion())) {
							target = inOrOut(getCurrentChampion(), a, firstPlayer);
						} else
							target = inOrOut(getCurrentChampion(), a, secondPlayer);
					}
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.DEBUFF)) {
						if (firstPlayer.getTeam().contains(getCurrentChampion())) {
							target = inOrOut(getCurrentChampion(), a, secondPlayer);
						} else
							target = inOrOut(getCurrentChampion(), a, firstPlayer);
					}
				}
				if (target.size() > 0) {
					for (int i = 0; i < target.size(); i++) {
						if(target.get(i) instanceof Champion){
							if(checkShield((Champion) target.get(i))) {
								target.remove(i);
								i--;
							}
						}
					}
					a.execute(target);
					getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
					getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
					for (Damageable damageable : target) {
						checkCondition(damageable);
					}
				}
			}
		}
		ChangeQ();
	}
	public static ArrayList<Damageable> inOrOut (Champion champion, Ability ability, Player secondPlayer){
			ArrayList<Damageable> target = new ArrayList<>();
			for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
				if (Math.abs(champion.getLocation().x - secondPlayer.getTeam().get(i).getLocation().x) + Math.abs(champion.getLocation().y - secondPlayer.getTeam().get(i).getLocation().y) <= ability.getCastRange())
					target.add(secondPlayer.getTeam().get(i));
			}
			return target;
		}
	public void castAbility(Ability a,Direction d) throws NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException, AbilityUseException {
		if (a.getManaCost() <= getCurrentChampion().getMana() && a.getRequiredActionPoints() <= getCurrentChampion().getCurrentActionPoints()) {
			if (a.getCurrentCooldown() == 0) {
				for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
					if (getCurrentChampion().getAppliedEffects().get(i).getName().equals("Silence"))
						throw new AbilityUseException();
				}
			} else
				throw new AbilityUseException();
		} else
			throw new NotEnoughResourcesException();
		ArrayList<Damageable> target = new ArrayList<>();
		switch (d) {
			case UP -> {
				target = findTarget(getCurrentChampion(), 1, 0, a.getCastRange());
				if (a instanceof DamagingAbility) {
					checkTeamEnemy(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Champion) {
							if (checkShield((Champion) target.get(i))) {
								target.remove(i);
								i--;
							}
						}
					}
					if (target.size() != 0) {
						a.execute(target);
						for (Damageable damageable : target) {
							checkCondition(damageable);
						}
					}
				} else if (a instanceof HealingAbility) {
					checkTeamSame(getCurrentChampion(), target);
					if (target.size() != 0) {
						a.execute(target);
					}
				} else if (a instanceof CrowdControlAbility) {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.DEBUFF)) {
						checkTeamEnemy(getCurrentChampion(), target);
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i) instanceof Cover) {
								target.remove(i);
								i--;
							}
						}
						if (target.size() != 0) {
							a.execute(target);
							for (Damageable damageable : target) {
								checkCondition(damageable);
							}
						}
					} else {
						checkTeamSame(getCurrentChampion(), target);
						if (target.size() != 0) {
							a.execute(target);
							for (Damageable damageable : target) {
								checkCondition(damageable);
							}
						}
					}
				}
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
			}
			case DOWN -> {
				target = findTarget(getCurrentChampion(), -1, 0, a.getCastRange());
				if (a instanceof DamagingAbility) {
					checkTeamEnemy(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Champion) {
							if (checkShield((Champion) target.get(i))) {
								target.remove(i);
								i--;
							}
						}
					}
					if (target.size() != 0) {
						a.execute(target);
						for (Damageable damageable : target) {
							checkCondition(damageable);
						}
					}
				} else if (a instanceof HealingAbility) {
					checkTeamSame(getCurrentChampion(), target);
					if (target.size() != 0) {
						a.execute(target);
					}
				} else if (a instanceof CrowdControlAbility) {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.DEBUFF)) {
						checkTeamEnemy(getCurrentChampion(), target);
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i) instanceof Cover) {
								target.remove(i);
								i--;
							}
						}
						if (target.size() != 0) {
							a.execute(target);
							for (Damageable damageable : target) {
								checkCondition(damageable);
							}
						}
					} else {
						checkTeamSame(getCurrentChampion(), target);
						if (target.size() != 0) {
							a.execute(target);
						}
					}
				}
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
			}
			case RIGHT -> {
				target = findTarget(getCurrentChampion(), 0, 1, a.getCastRange());
				if (a instanceof DamagingAbility) {
					checkTeamEnemy(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Champion) {
							if (checkShield((Champion) target.get(i))) {
								target.remove(i);
								i--;
							}
						}
					}
					if (target.size() != 0) {
						a.execute(target);
						for (Damageable damageable : target) {
							checkCondition(damageable);
						}
					}
				} else if (a instanceof HealingAbility) {
					checkTeamSame(getCurrentChampion(), target);
					if (target.size() != 0) {
						a.execute(target);
					}
				} else if (a instanceof CrowdControlAbility) {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.DEBUFF)) {
						checkTeamEnemy(getCurrentChampion(), target);
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i) instanceof Cover) {
								target.remove(i);
								i--;
							}
						}
						if (target.size() != 0) {
							a.execute(target);
							for (Damageable damageable : target) {
								checkCondition(damageable);
							}
						}
					} else {
						checkTeamSame(getCurrentChampion(), target);
						if (target.size() != 0) {
							a.execute(target);
						}
					}
				}
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
			}case LEFT -> {
				target = findTarget(getCurrentChampion(), 0, -1, a.getCastRange());
				if (a instanceof DamagingAbility) {
					checkTeamEnemy(getCurrentChampion(), target);
					for (int i = 0; i < target.size(); i++) {
						if (target.get(i) instanceof Champion) {
							if (checkShield((Champion) target.get(i))) {
								target.remove(i);
								i--;
							}
						}
					}
					if (target.size() != 0) {
						a.execute(target);
						for (Damageable damageable : target) {
							checkCondition(damageable);
						}
					}
				} else if (a instanceof HealingAbility) {
					checkTeamSame(getCurrentChampion(), target);
					if (target.size() != 0) {
						a.execute(target);
					}
				} else if (a instanceof CrowdControlAbility) {
					if (((CrowdControlAbility) a).getEffect().getType().equals(EffectType.DEBUFF)) {
						checkTeamEnemy(getCurrentChampion(), target);
						for (int i = 0; i < target.size(); i++) {
							if (target.get(i) instanceof Cover) {
								target.remove(i);
								i--;
							}
						}
						if (target.size() != 0) {
							a.execute(target);
							for (Damageable damageable : target) {
								checkCondition(damageable);
							}
						}
					} else {
						checkTeamSame(getCurrentChampion(), target);
						if (target.size() != 0) {
							a.execute(target);
						}
					}
				}
				getCurrentChampion().setMana(getCurrentChampion().getMana() - a.getManaCost());
				getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints() - a.getRequiredActionPoints());
			}
		}
		ChangeQ();
	}
	public void castAbility(Ability a , int x , int y) throws AbilityUseException, NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException {
		if (a.getManaCost() <= getCurrentChampion().getMana() && a.getRequiredActionPoints() <= getCurrentChampion().getCurrentActionPoints()){
			if (a.getCurrentCooldown() == 0) {
				for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
					if (getCurrentChampion().getAppliedEffects().get(i).getName().equals("Silence"))
						throw new AbilityUseException();
				}
			} else
				throw new AbilityUseException();
		} else
			throw new NotEnoughResourcesException();

		ArrayList<Damageable> target = new ArrayList<>();
		if(x>=getBoardheight()){
			throw new InvalidTargetException();
		}
		if(y>=getBoardwidth()){
			throw new InvalidTargetException();
		}
		if (board[x][y] != null) {
			if (a instanceof DamagingAbility) {
				target.add((Damageable) getBoard()[x][y]);
				checkTeamEnemy(getCurrentChampion(), target);
			} else if (a instanceof HealingAbility) {
				target.add((Damageable)getBoard()[x][y]);
				checkTeamSame(getCurrentChampion(),target);
			}else if(((CrowdControlAbility)a).getEffect().getType().equals(EffectType.DEBUFF)){
				target.add((Damageable) getBoard()[x][y]);
				if(target.get(0) instanceof Cover)
					target.remove(0);
				checkTeamEnemy(getCurrentChampion(), target);

			}else {
				target.add((Damageable) getBoard()[x][y]);
				checkTeamSame(getCurrentChampion(), target);
			}
			if(target.size()!=0) {
				if(target.get(0) instanceof Champion){
					if(!checkShield((Champion) target.get(0))) {
						if (Math.abs(getCurrentChampion().getLocation().x - target.get(0).getLocation().x) + Math.abs(getCurrentChampion().getLocation().y - target.get(0).getLocation().y) <= a.getCastRange()) {
							a.execute(target);
							checkCondition(target.get(0));
						}else
							throw new AbilityUseException();

					}
				}else {
					a.execute(target);
					checkCondition(target.get(0));
				}
			}
			else
				throw new InvalidTargetException();
		}else
			throw new InvalidTargetException();
		getCurrentChampion().setMana(getCurrentChampion().getMana()-a.getManaCost());
		getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getCurrentActionPoints()-a.getRequiredActionPoints());
		checkGameOver();
		ChangeQ();
	}
    public void checkTeamEnemy(Champion champion, ArrayList<Damageable> target){
	if(firstPlayer.getTeam().contains(champion)){
		for (int i = 0; i < target.size(); i++) {
			if (target.get(i) instanceof Champion) {
				if(firstPlayer.getTeam().contains((Champion) target.get(i))) {
					target.remove(i);
							i--;
				}
			}
		}
	}else {
		for (int i = 0; i < target.size(); i++) {
			if (target.get(i) instanceof Champion) {
				if(secondPlayer.getTeam().contains((Champion) target.get(i))) {
					target.remove(i);
					i--;
				}
			}
		}
	}
}
    public void checkTeamSame(Champion champion , ArrayList<Damageable> target) {
	if (firstPlayer.getTeam().contains(champion)) {
		for (int i = 0; i < target.size(); i++) {
			if (target.get(i) instanceof Champion) {
				if (secondPlayer.getTeam().contains((Champion) target.get(i))) {
					target.remove(i);
					i--;
				}
			} else if (target.get(i) instanceof Cover) {
				target.remove(i);
				i--;
			}
		}
	} else {
		if (secondPlayer.getTeam().contains(champion)) {
			for (int i = 0; i < target.size(); i++) {
				if (target.get(i) instanceof Champion) {
					if (firstPlayer.getTeam().contains((Champion) target.get(i))) {
						target.remove(i);
						i--;
					}
				} else if (target.get(i) instanceof Cover) {
					target.remove(i);
					i--;
				}
			}
		}
	}
}
    public ArrayList<Champion> AntiHeroCheck(Player firstPlayer, Player secondPlayer) { //Help for Leader
		ArrayList<Champion> targets = new ArrayList<>();
		targets.addAll(firstPlayer.getTeam());
		targets.addAll(secondPlayer.getTeam());
		for (int i = 0; i < targets.size(); i++) {
			if ((targets.get(i).equals(firstPlayer.getLeader()) || targets.get(i).equals(secondPlayer.getLeader())) && !targets.get(i).getCondition().equals(Condition.KNOCKEDOUT)) {
				targets.remove(i);
				i--;
			}
		}
		return targets;
	}
	public void useLeaderAbility() throws CloneNotSupportedException, LeaderAbilityAlreadyUsedException, LeaderNotCurrentException {
		ArrayList<Champion> targets = new ArrayList<>();
		if(firstPlayer.getTeam().contains(getCurrentChampion())){
			if(!firstLeaderAbilityUsed){
				if(getCurrentChampion().equals(firstPlayer.getLeader())){
					if(getCurrentChampion() instanceof Hero){
						targets.addAll(firstPlayer.getTeam());
					}else if(getCurrentChampion() instanceof Villain)
						targets.addAll(secondPlayer.getTeam());
					else
						targets = AntiHeroCheck(firstPlayer,secondPlayer);
					getCurrentChampion().useLeaderAbility(targets);
					firstLeaderAbilityUsed = true;
				}else
					throw new LeaderNotCurrentException();
			}else
				throw new LeaderAbilityAlreadyUsedException();
		}else {
			if(!secondLeaderAbilityUsed){
				if(getCurrentChampion().equals(secondPlayer.getLeader())) {
					if (getCurrentChampion() instanceof Hero) {
						targets.addAll(secondPlayer.getTeam());
					}
					else if (getCurrentChampion() instanceof Villain){
						targets.addAll(firstPlayer.getTeam());
				}else
						targets = AntiHeroCheck(firstPlayer,secondPlayer);

					  getCurrentChampion().useLeaderAbility(targets);
					  secondLeaderAbilityUsed =true;
				}else
					throw new LeaderNotCurrentException();
			}else
				throw new LeaderAbilityAlreadyUsedException();
		}
	}
	public void endUpdate(Champion c){
		for (int i = 0; i < getCurrentChampion().getAppliedEffects().size(); i++) {
			getCurrentChampion().getAppliedEffects().get(i).setDuration(getCurrentChampion().getAppliedEffects().get(i).getDuration() - 1);
			if (getCurrentChampion().getAppliedEffects().get(i).getDuration() == 0) {
				getCurrentChampion().getAppliedEffects().get(i).remove(getCurrentChampion());
//				getCurrentChampion().getAppliedEffects().remove(i);
//				i--;
			}
		}
			for (int i = 0; i < getCurrentChampion().getAbilities().size(); i++) {
				if (getCurrentChampion().getAbilities().get(i).getCurrentCooldown() > 0)
					getCurrentChampion().getAbilities().get(i).setCurrentCooldown(getCurrentChampion().getAbilities().get(i).getCurrentCooldown() - 1);

		}

	}
	public void endTurn() {
		turnOrder.remove();
		endUpdate(getCurrentChampion());
		while (getCurrentChampion().getCondition().equals(Condition.INACTIVE)){
			turnOrder.remove();
			endUpdate(getCurrentChampion());
		}
		while (getCurrentChampion().getCondition().equals(Condition.KNOCKEDOUT))
			turnOrder.remove();

		getCurrentChampion().setCurrentActionPoints(getCurrentChampion().getMaxActionPointsPerTurn());
	}
	public void ChangeQ(){
		PriorityQueue a = new PriorityQueue(6);
		while (!turnOrder.isEmpty()){
			if(((Champion)turnOrder.peekMin()).getCondition()!=Condition.KNOCKEDOUT) {
				a.insert(turnOrder.peekMin());
				turnOrder.remove();
			}else
				turnOrder.remove();
		}
		turnOrder = a ;
	}
	private void prepareChampionTurns(){
		if(turnOrder.isEmpty()){
			for (int i = 0; i < firstPlayer.getTeam().size(); i++) {
				if (firstPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
					turnOrder.insert(firstPlayer.getTeam().get(i));
			}
			for (int i = 0; i < secondPlayer.getTeam().size(); i++) {
				if (secondPlayer.getTeam().get(i).getCondition() != Condition.KNOCKEDOUT)
					turnOrder.insert(secondPlayer.getTeam().get(i));
			}
		}
	}
}

