package qcfpunch.cards.dan;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import qcfpunch.QCFP_Misc;

public class DefendWeakest extends CustomCard {

	public static final String ID = QCFP_Misc.returnPrefix() + "Defend_Weakest";
    private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    private static final int COST = 0;
    private static final int BLOCK_INIT_GAINED = 1;
	
    public DefendWeakest() {
        super(ID, DefendWeakest.NAME,
        		QCFP_Misc.returnCardsImageMainFolder() + "temp_skill.png", COST, DefendWeakest.DESCRIPTION,
        		CardType.SKILL, CardColor.COLORLESS, CardRarity.BASIC, CardTarget.SELF);
        
        this.baseBlock = BLOCK_INIT_GAINED;
        this.exhaust = true;
        this.isEthereal = true;
        
    }
    
	@Override
	public AbstractCard makeCopy() {
		return new DefendWeakest();
	}

	@Override
	public void upgrade() {
		return;
	}

	@Override
	public void use(AbstractPlayer player, AbstractMonster monster) {
	      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, this.block));
	}
	
	static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = DefendWeakest.cardStrings.NAME;
        DESCRIPTION = DefendWeakest.cardStrings.DESCRIPTION;
    }

}
