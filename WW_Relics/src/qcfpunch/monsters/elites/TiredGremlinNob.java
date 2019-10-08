package qcfpunch.monsters.elites;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.CustomMonster;
import qcfpunch.QCFP_Misc;

public class TiredGremlinNob extends CustomMonster {

	public static final String ID = QCFP_Misc.returnPrefix() + "TiredGremlinNob";
	private static final MonsterStrings monsterstrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	public static final String NAME = monsterstrings.NAME;
	public static final String[] DIALOG = monsterstrings.DIALOG;
	private static final int HP_MIN = 300;
	private static final int HP_MAX = 305;
	private static final int INITIAL_HP = 200;
	private static final int A8_HP_MIN = 301;
	private static final int A8_HP_MAX = 306;
	private static final int A8_INITIAL_HP = 210;
	
	private static final int INITIAL_STR_BUFF = 5;
	private static final int INITIAL_ANGRY_BUFF = 2;
	
	private static final int INITIAL_WEAK_DEBUFF = 6;
	private static final int INITIAL_VULNERABLE_DEBUFF = 4;
	private static final int A8_INITIAL_VULNERABLE_DEBUFF = 3;
	private static final int INITIAL_POISON_DEBUFF = 10;
	private static final int A8_INITIAL_POISON_DEBUFF = 8;
	private static final boolean WILL_HAVE_INITIAL_STUN = false;
	private static final int INITIAL_CONSTRICTED_DEBUFF = 3;
	
	private static final byte STARTING_POINT = 4;
	private static final byte ANGRY_SCREAM = 1;
	private static final byte ARM_SMASH = 2;
	private static final byte BODY_BLOW = 3;
	private static final byte HEAVY_BREATHING = 4;
	
	private static final int ARM_SMASH_DAMAGE_INFO_POSITION = 0;
	private static final int BODY_BLOW_DAMAGE_INFO_POSITION = 1;
	
	private static final int ANGRY_SCREAM_STR_BUFF = 2;
	private static final int BASE_ARM_SMASH_DAMAGE = 15;
	private static final int A3_BASE_ARM_SMASH_DAMAGE = 17;
	private static final int BODY_BLOW_NUMBER_OF_HITS = 2;
	private static final int BASE_BODY_BLOW_DAMAGE = 0;
	private static final int A3_BASE_BODY_BLOW_DAMAGE = 3;
	private static final int HEAVY_BREATHING_HEAL = 3;
	private int number_of_turns = 1;
	
	public TiredGremlinNob(float x, float y) {
		
		super(NAME, "Tired Gremlin Nob",  86, -70.0F, -10.0F, 270.0F, 380.0F, null, x, y);
	    this.intentOffsetX = -30.0F * Settings.scale;
	    this.type = EnemyType.ELITE;
	    this.dialogX = -60.0F * Settings.scale;
	    this.dialogY = 50.0F * Settings.scale;
	    
	    loadAnimation("images/monsters/theBottom/nobGremlin/skeleton.atlas",
	    		"images/monsters/theBottom/nobGremlin/skeleton.json", 1.0F);
		
	    AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
	    e.setTime(e.getEndTime() * MathUtils.random());
	    
	    this.type = EnemyType.ELITE;
	    
	    if (AbstractDungeon.ascensionLevel >= 8) {
	    	setHp(A8_HP_MIN, A8_HP_MAX);
	    	this.currentHealth = A8_INITIAL_HP;
	    	
	    }
	    else {
	    	setHp(HP_MIN, HP_MAX);
	    	this.currentHealth = INITIAL_HP;
	    }
	    
	    
	    this.nextMove = (int)STARTING_POINT;
	    
	    if (AbstractDungeon.ascensionLevel >= 3) {
	    	setAscension3DamageInfos();
	    } else setDamageInfos();
	}
	
	public void setDamageInfos() {
		DamageInfo arm_smash_damage_info = new DamageInfo(this, BASE_ARM_SMASH_DAMAGE);
		DamageInfo body_blow_damage_info = new DamageInfo(this, BASE_BODY_BLOW_DAMAGE);
		
		this.damage.add(arm_smash_damage_info);
		this.damage.add(body_blow_damage_info);
	}
	
	public void setAscension3DamageInfos(){
		DamageInfo a3_arm_smash_damage_info = new DamageInfo(this, A3_BASE_ARM_SMASH_DAMAGE);
		DamageInfo a3_body_blow_damage_info = new DamageInfo(this, A3_BASE_BODY_BLOW_DAMAGE);
		
		this.damage.add(a3_arm_smash_damage_info);
		this.damage.add(a3_body_blow_damage_info);
	}

	public void usePreBattleAction() {
        addInitialEnemyBuffs();
        addInitialEnemyDebuffs(AbstractDungeon.ascensionLevel);
    }
	
	public void addInitialEnemyBuffs() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
        		new StrengthPower(this, INITIAL_STR_BUFF)));
        QCFP_Misc.addNonFastModeWaitAction(0.5f);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
        		new AngerPower(this, INITIAL_ANGRY_BUFF), INITIAL_ANGRY_BUFF));
	}
	
	public void addInitialEnemyDebuffs(int ascensionLevel) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
				new WeakPower(this,  INITIAL_WEAK_DEBUFF, false), INITIAL_WEAK_DEBUFF));
		
        QCFP_Misc.addNonFastModeWaitAction(0.5f);
        
        if (ascensionLevel >= 8) {
        	
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
    				new VulnerablePower(this,  A8_INITIAL_VULNERABLE_DEBUFF, false), A8_INITIAL_VULNERABLE_DEBUFF));
    		
            QCFP_Misc.addNonFastModeWaitAction(0.5f);
            
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
    				new PoisonPower(this, this, A8_INITIAL_POISON_DEBUFF), A8_INITIAL_POISON_DEBUFF));       
            
        } else {
        	
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
    				new VulnerablePower(this,  INITIAL_VULNERABLE_DEBUFF, false), INITIAL_VULNERABLE_DEBUFF));
    		
            QCFP_Misc.addNonFastModeWaitAction(0.5f);
            
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
    				new PoisonPower(this, this, INITIAL_POISON_DEBUFF), INITIAL_POISON_DEBUFF));
            
        }

        QCFP_Misc.addNonFastModeWaitAction(0.5f);
        
        if (WILL_HAVE_INITIAL_STUN) {
        	AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(this, this));
        	QCFP_Misc.addNonFastModeWaitAction(0.5f);
        }
        
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
				new ConstrictedPower(this, this, INITIAL_CONSTRICTED_DEBUFF), INITIAL_CONSTRICTED_DEBUFF));
        
        QCFP_Misc.addNonFastModeWaitAction(0.5f);
	}
	
	@Override
	protected void getMove(int arg0) {
		number_of_turns++;
		
		if (number_of_turns % 7 == 0) {
			HeavyBreathingIntent();
			return;
		} 
		
		switch(this.nextMove) {
			case ANGRY_SCREAM:
				ArmSmashIntent();
				break;
			case ARM_SMASH:
				BodyBlowIntent();	
				break;
			case BODY_BLOW:
				HeavyBreathingIntent();
				break;
			case HEAVY_BREATHING:
				AngryScreamIntent();
				break;
			default:
				HeavyBreathingIntent();
				break;
		}
		setNextMove();
	}

	@Override
	public void takeTurn() {
		
		if (number_of_turns % 7 == 0) {
			HeavyBreathingMove();
			AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
			return;
		} 
		
		switch(this.nextMove) {
			case ANGRY_SCREAM:
				AngryScreamMove();
				break;
			case ARM_SMASH:
				ArmSmashMove();
				break;
			case BODY_BLOW:
				BodyBlowMove();
				break;
			case HEAVY_BREATHING:
				HeavyBreathingMove();
				break;
			default:
				break;
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	public void setNextMove() {
		this.nextMove++;
		if (this.nextMove > HEAVY_BREATHING) this.nextMove = ANGRY_SCREAM;
	}
	
	private void playSfx() {
		int roll = MathUtils.random(2);
		if (roll == 0) {
			AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_GREMLINNOB_1A"));
	    } else if (roll == 1) {
	    	AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_GREMLINNOB_1B"));
	    } else {
	    	AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_GREMLINNOB_1C"));
	    } 
	}
	
	public void AngryScreamMove() {
		playSfx();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
        		new StrengthPower(this, ANGRY_SCREAM_STR_BUFF), ANGRY_SCREAM_STR_BUFF));		
	}
	
	public void AngryScreamIntent() {
        setMove(ANGRY_SCREAM, Intent.BUFF);
	}
	
	public void ArmSmashMove() {
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage
              .get(ARM_SMASH_DAMAGE_INFO_POSITION), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
	}
	
	public void ArmSmashIntent() {
		setMove(ARM_SMASH, Intent.ATTACK, ((DamageInfo)this.damage.get(ARM_SMASH_DAMAGE_INFO_POSITION)).base);
	}
	
	public void BodyBlowMove() {
		for (int i = 0; i < BODY_BLOW_NUMBER_OF_HITS; ++i) {
			AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player,
				(DamageInfo)this.damage.get(BODY_BLOW_DAMAGE_INFO_POSITION),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		}
	}
	
	public void BodyBlowIntent() {
		setMove(BODY_BLOW, Intent.ATTACK, ((DamageInfo)this.damage.get(BODY_BLOW_DAMAGE_INFO_POSITION)).base,
				2, true);
	}
	
	public void HeavyBreathingMove() {
		AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, HEAVY_BREATHING_HEAL));
		AbstractPower strength_power = this.getPower("Strength");

		if (strength_power != null) {
			int amount_of_str_buff = strength_power.amount;
			amount_of_str_buff /= 2;
			QCFP_Misc.addNonFastModeWaitAction(0.5f);
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
					new StrengthPower(this, -amount_of_str_buff), -amount_of_str_buff));
		}
	}
	
	public void HeavyBreathingIntent() {
		setMove(HEAVY_BREATHING, Intent.BUFF);
	}

}
