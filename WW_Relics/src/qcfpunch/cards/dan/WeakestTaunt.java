package qcfpunch.cards.dan;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.abstracts.CustomCard;
import qcfpunch.QCFP_Misc;
import qcfpunch.powers.PotentialPower;

public class WeakestTaunt extends CustomCard {

	public static final String ID = QCFP_Misc.returnPrefix() + "WeakestTaunt";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    private static final int COST = 0;
    private static final int WEAK_AMOUNT = 1;
    private static final int POTENTIAL_NUMERATOR = 1;
    private static final int POTENTIAL_DENOMINATOR = 3;
	
	public WeakestTaunt() {
		super(ID, WeakestTaunt.NAME, QCFP_Misc.returnCardsImageMainFolder() + "temp_skill.png",
				WeakestTaunt.COST, WeakestTaunt.DESCRIPTION, CardType.SKILL,
				CardColor.COLORLESS, CardRarity.COMMON, CardTarget.ENEMY);

        this.exhaust = true;
        this.isEthereal = true;
	}
    
	@Override
	public void upgrade() {
		return;
	}

	@Override
	public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractDungeon.actionManager.addToBottom(
        		new ApplyPowerAction(monster, player,
        				new WeakPower(monster, WEAK_AMOUNT, false), WEAK_AMOUNT));
        
        AbstractDungeon.actionManager.addToBottom(
        		new ApplyPowerAction(player, player, 
        				new PotentialPower(player, POTENTIAL_NUMERATOR, POTENTIAL_DENOMINATOR)));
	}
	
	static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = WeakestTaunt.cardStrings.NAME;
        DESCRIPTION = WeakestTaunt.cardStrings.DESCRIPTION;
    }

}
