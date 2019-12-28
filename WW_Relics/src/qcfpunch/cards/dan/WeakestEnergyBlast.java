package qcfpunch.cards.dan;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.PotentialPower;
import qcfpunch.vfx.combat.WeakestEnergyBlastParticleEffect;

public class WeakestEnergyBlast extends CustomCard {

	public static final String ID = QCFP_Misc.returnPrefix() + "WeakestEnergyBlast";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    private static final int COST = 0;
    private static final int ATTACK_DMG = 3;
    private static final int POTENTIAL_NUMERATOR = 1;
    private static final int POTENTIAL_DENOMINATOR = 3;
    private static final float FX_DURATION = 1.6f;
    private static final float FAST_FX_DURATION = 1.3f;
    private static final float HUH_DURATION = 0.5f;
    private static final float FAST_HUH_DURATION = 0.2f;
	
	public WeakestEnergyBlast() {
		super(ID, WeakestEnergyBlast.NAME, QCFP_Misc.returnCardsImageMainFolder() + "temp_attack.png",
				WeakestEnergyBlast.COST, WeakestEnergyBlast.DESCRIPTION, CardType.ATTACK,
				CardColor.COLORLESS, CardRarity.COMMON, CardTarget.ENEMY);

        this.baseDamage = ATTACK_DMG;
        this.exhaust = true;
        this.isEthereal = true;
	}
    
	@Override
	public void upgrade() {
		return;
	}

	@Override
	public void use(AbstractPlayer player, AbstractMonster monster) {
		
	    if (monster != null) {
	    	
	    	float fx_duration;
	    	float huh_duration;
	    	if (Settings.FAST_MODE) {
	    		fx_duration = FAST_FX_DURATION;
	    		huh_duration = FAST_HUH_DURATION;
	    	} else {
	    		fx_duration = FX_DURATION;
	    		huh_duration = HUH_DURATION;
	    	}
	    	
	    	AbstractDungeon.actionManager.addToBottom(
	    			new VFXAction(new WeakestEnergyBlastParticleEffect(
	    					player.hb.cX + player.hb_w*0.65f,
	    					player.hb.cY,
	    					monster.hb.cX, monster.hb.cY,
	    					new Color(0.9f, 0.9f, 0.9f, 1.0f),
	    					new Color(0.66f, 0.33f, 0.99f, 0.5f), 1.0f, fx_duration),
	    					fx_duration));
	    	
	    	AbstractDungeon.actionManager.addToBottom(
	    			new WaitAction(huh_duration));
	    	
	    	AbstractDungeon.actionManager.addToBottom(
	    			new AnimateFastAttackAction(player));
	    	
	        AbstractDungeon.actionManager.addToBottom(
	        		new DamageAction(monster, new DamageInfo(player,
	        				this.damage, this.damageTypeForTurn),
	        				AbstractGameAction.AttackEffect.BLUNT_LIGHT));
	    }
		
        AbstractDungeon.actionManager.addToBottom(
        		new ApplyPowerAction(player, player, 
        				new PotentialPower(player, POTENTIAL_NUMERATOR,
        						POTENTIAL_DENOMINATOR)));
	}

	static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = WeakestEnergyBlast.cardStrings.NAME;
        DESCRIPTION = WeakestEnergyBlast.cardStrings.DESCRIPTION;
    }

}
