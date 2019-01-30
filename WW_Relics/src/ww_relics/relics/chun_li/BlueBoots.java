package ww_relics.relics.chun_li;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;

public class BlueBoots extends CustomRelic {
	public static final String ID = "WW_Relics_Blue_Boots";
	private static final int NUMBER_OF_USES_PER_FIGHT = 1;
	private static final int NUMBER_OF_CHOSEN_ATTACKS = 1;
	private static final int NUMBER_OF_COPIES = 4;
	private static final int COST_OF_THE_GENERATED_CARDS = 0;
	
	private Random random = new Random();
	
	public BlueBoots() {
		super(ID, "abacus.png", //add method for textures here.
				RelicTier.RARE, LandingSound.SOLID);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + NUMBER_OF_CHOSEN_ATTACKS +
				DESCRIPTIONS[1] + NUMBER_OF_COPIES + DESCRIPTIONS[2] +
				COST_OF_THE_GENERATED_CARDS;
	}

	//how it works:
	//when used, once per combat,
	//Choose an attack in your hand.
	//Create four copies of it, place them in your draw pile, then exhaust the card.
	//The copies have 0-cost the first time they are used.
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new BlueBoots();
	}
}