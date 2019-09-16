package qcfpunch.cards.dan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import qcfpunch.QCFPunch_MiscCode;
import qcfpunch.powers.PotentialPower;

public class WeakestEnergyBlast extends CustomCard {

	public static final String ID = QCFPunch_MiscCode.returnPrefix() + "WeakestEnergyBlast";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    private static final int COST = 0;
    private static final int ATTACK_DMG = 3;
    private static final int POTENTIAL_NUMERATOR = 1;
    private static final int POTENTIAL_DENOMINATOR = 3;
	
	public WeakestEnergyBlast() {
		super(ID, WeakestEnergyBlast.NAME, QCFPunch_MiscCode.returnCardsImageMainFolder() + "temp_attack.png",
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
	    	AbstractDungeon.actionManager.addToBottom(
	    			new VFXAction(new WeakestEnergyBlastEffect(
	    					player.hb.cX, player.hb.cY, mon1.hb.cX, mon1.hb.cY, 1), 0.6f));));
	    	
	        AbstractDungeon.actionManager.addToBottom(
	        		new DamageAction(monster, new DamageInfo(player,
	        				this.damage, this.damageTypeForTurn),
	        				AbstractGameAction.AttackEffect.BLUNT_HEAVY));
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
