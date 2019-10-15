package qcfpunch.cards.ui;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;
import qcfpunch.QCFP_Misc;

public class ErrorCard extends CustomCard {

	public static final String ID = QCFP_Misc.returnPrefix()
			+ "ErrorCard";
	
	private static final CardStrings cardStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    
    public static String error_string;
    
    private static final int COST = -2;
    
    public ErrorCard() {
    	super(ID, ErrorCard.NAME,
        		QCFP_Misc.returnCardsImageMainFolder() + "temp_skill.png",
        		COST, ErrorCard.DESCRIPTION, CardType.ATTACK,
        		CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
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
        NAME = ErrorCard.cardStrings.NAME;
        DESCRIPTION = ErrorCard.cardStrings.DESCRIPTION;
    }

}
