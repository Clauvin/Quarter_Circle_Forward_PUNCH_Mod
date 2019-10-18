package qcfpunch.relics.character_cameos.valerie;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.actions.SetAlwaysRetainOfCardAtCombatAction;
import qcfpunch.actions.SetExhaustOfCardAtCombatAction;
import qcfpunch.resources.relic_graphics.GraphicResources;
import theArtist.TheArtist;
import theArtist.cards.PalettePick;

public class MixedPaintBucket extends CustomRelic {

	public static final String ID = QCFP_Misc.returnPrefix() + "Mixed_Paint_Bucket";
	
	public AbstractCard card_to_give;
	
	public MixedPaintBucket() {
		super(ID, GraphicResources.LoadRelicImage(
				"Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.UNCOMMON, LandingSound.CLINK);
		
		card_to_give = new PalettePick();
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void atBattleStart() {
		CardGroup hand = AbstractDungeon.player.hand;
		
		ArrayList<CardColor> colors = new ArrayList<CardColor>();
		int amount_of_cards_to_give = 0;
		
		for (int i = 0; i < hand.size(); i++) {
			CardColor color = hand.getNCardFromTop(i).color;
			boolean new_color = true;
			for (int j = 0; j < colors.size(); j++) {
				if (colors.get(j) == color) {
					new_color = false;
					break;
				}
			}
			if (new_color) {
				colors.add(color);
				amount_of_cards_to_give += 1;
			}
		}
				
		for (int i = 0; i < amount_of_cards_to_give; i++) {
			AbstractCard given_card = card_to_give.makeStatEquivalentCopy();
			
			QCFP_Misc.setCardToAlwaysRetain(
					given_card, true);
			
			QCFP_Misc.reduceCardCostIfNotStatusOrCurseByOne(given_card);
			
			AbstractDungeon.actionManager.addToBottom(
					new MakeTempCardInHandAction(given_card));
			
			AbstractDungeon.actionManager.addToBottom(
					new SetExhaustOfCardAtCombatAction(given_card.uuid, true));
		}
		
		
	}
	
	public boolean canSpawn() {
		return QCFP_Misc.checkForMod(QCFP_Misc.the_artist_class_code);
	}
	
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new MixedPaintBucket();
	}
	
}
