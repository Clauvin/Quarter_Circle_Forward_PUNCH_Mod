package qcfpunch.cards.ui;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import qcfpunch.QCFP_Misc;

public class Finished extends CustomCard {

	public static final String ID = QCFP_Misc.returnPrefix()
			+ "Finished";
	
	private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    
    private static final int COST = 0;
    
    public Finished() {
    	super(ID, Finished.NAME,
        		QCFP_Misc.returnCardsImageMainFolder() + "temp_skill.png",
        		COST, Finished.DESCRIPTION, CardType.SKILL,
        		CardColor.COLORLESS, CardRarity.BASIC, CardTarget.SELF);
    }
	
	@Override
	public void upgrade() {
		// TODO Auto-generated method stub

	}

	@Override
	public void use(AbstractPlayer arg0, AbstractMonster arg1) {
		// TODO Auto-generated method stub

	}
	
	static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = Finished.cardStrings.NAME;
        DESCRIPTION = Finished.cardStrings.DESCRIPTION;
    }

}
