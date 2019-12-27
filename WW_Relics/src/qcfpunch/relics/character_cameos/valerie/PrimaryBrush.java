package qcfpunch.relics.character_cameos.valerie;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import qcfpunch.QCFP_Misc;
import qcfpunch.resources.relic_graphics.GraphicResources;
import theArtist.AbstractCanvas.VexColor;
import theArtist.TheArtist;
import theArtist.actions.PaintAction;

public class PrimaryBrush extends CustomRelic  {

	public static final String ID = QCFP_Misc.returnPrefix() + "Primary_Brush";
	
	private static int attacks_played = 0;
	private static int skills_played = 0;
	private static int powers_played = 0;
	
	private static final int AMOUNT_OF_ATTACK_CARDS_PLAYED = 3;
	private static final int AMOUNT_OF_SKILL_CARDS_PLAYED = 3;
	private static final int AMOUNT_OF_POWER_CARDS_PLAYED = 3;
	
	private static final Color ATTACK_COLOR = new Color(0.9f, 0.0f, 0.0f, 1.0f);
	private static final Color SKILL_COLOR = new Color(0.0f, 0.9f, 0.0f, 1.0f);
	private static final Color POWER_COLOR = new Color(0.0f, 0.0f, 0.9f, 1.0f);
	
	public static final Logger logger =
			LogManager.getLogger(PrimaryBrush.class.getName());
	
	public PrimaryBrush() {
		super(ID, GraphicResources.LoadRelicImage(
				"Temp School Backpack - steeltoe-boots - Lorc - CC BY 3.0.png"),
				RelicTier.RARE, LandingSound.CLINK);
	}
	
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}
	
	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		checkAndCountCardType(c);
		
		applyEffectsAndResetCountersIfNecessary();
		
		logger.debug(attacks_played + " " + skills_played + " " + powers_played);
	}
	
	private void checkAndCountCardType(AbstractCard card) {
		CardType card_type = card.type;
		
		switch(card_type) {
			case ATTACK:
				attacks_played += 1;
				break;
			case SKILL:
				skills_played += 1;
				break;
			case POWER:
				powers_played += 1;
				break;
			default:
				break;
		}
	}
	
	private void applyEffectsAndResetCountersIfNecessary() {
		if (attacks_played >= AMOUNT_OF_ATTACK_CARDS_PLAYED) {
			attacks_played = 0;
			AbstractDungeon.actionManager.addToBottom(
					new PaintAction(VexColor.RED));
			AbstractDungeon.actionManager.addToBottom(
					new PaintAction(VexColor.RED));
		}
		
		if (skills_played >= AMOUNT_OF_SKILL_CARDS_PLAYED) {
			skills_played = 0;
			AbstractDungeon.actionManager.addToBottom(
					new PaintAction(VexColor.GREEN));
			AbstractDungeon.actionManager.addToBottom(
					new PaintAction(VexColor.GREEN));
		}
		
		if (powers_played >= AMOUNT_OF_POWER_CARDS_PLAYED) {
			powers_played = 0;
			AbstractDungeon.actionManager.addToBottom(
					new PaintAction(VexColor.BLUE));
			AbstractDungeon.actionManager.addToBottom(
					new PaintAction(VexColor.BLUE));
		}
	}
	
	// Borrowed from regret-index's Administrix's ConductorRitualBaton.
    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        this.scale = Settings.scale;
        if (Settings.hideRelics) {
            return;
        }

        renderOutline(sb, true);
        sb.setColor(Color.WHITE);
        float offsetX = 0f;
        float rotation = 0f;
        sb.draw(this.img, this.currentX - 64.0F + offsetX,
        		this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F,
        		this.scale, this.scale, rotation, 0, 0, 128, 128, false, false);

        renderCounters(sb, true);
        renderFlash(sb, true);
        this.hb.render(sb);
    }
    
	// Borrowed from regret-index's Administrix's ConductorRitualBaton.
    public void renderCounters(SpriteBatch sb, boolean inTopPanel) {
        float offX = this.currentX;
        float offXS = 26.0F * Settings.scale;

        FontHelper.renderFontRightTopAligned(sb, 
        		FontHelper.topPanelInfoFont, 
        		Integer.toString(attacks_played), offX - offXS/2.0f,
        		this.currentY - 7.0F * Settings.scale, ATTACK_COLOR);
        FontHelper.renderFontRightTopAligned(sb, 
        		FontHelper.topPanelInfoFont, 
        		Integer.toString(skills_played), offX + offXS/2.0f,
        		this.currentY - 7.0F * Settings.scale , SKILL_COLOR);
        FontHelper.renderFontRightTopAligned(sb,
        		FontHelper.topPanelInfoFont,
        		Integer.toString(powers_played), offX + 2.5f*offXS/2.0f,
        		this.currentY - 7.0F * Settings.scale, POWER_COLOR);
    }
	
	public static void save(final SpireConfig config) {

        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
        	logger.info(QCFP_Misc.debugStringStartedSaveDataManagement(PrimaryBrush.ID));
    		logger.info(QCFP_Misc.classAndSaveSlotText());

        	String class_name = AbstractDungeon.player.getClass().getName();
        	String field_text = "multicolor_pen_class_" + class_name +
            		"_save_slot_" + CardCrawlGame.saveSlot;
    		
            config.setInt(field_text + "_attacks_played", attacks_played);
            config.setInt(field_text + "_skills_played", skills_played);
            config.setInt(field_text + "_powers_played", powers_played);

            try {
				config.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            logger.info(QCFP_Misc.debugStringFinishedSaveDataManagement(PrimaryBrush.ID));
            logger.info(QCFP_Misc.classAndSaveSlotText());	
        }
        else {

        }

    }
	
	public static void load(final SpireConfig config) {
		
		logger.info(QCFP_Misc.debugStringStartedLoadDataManagement(PrimaryBrush.ID));
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
    	String field_text = "multicolor_pen_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot;
		
		if (AbstractDungeon.player.hasRelic(ID) &&
				config.has(field_text + "_attacks_played")) {

			attacks_played = config.getInt(field_text + "_attacks_played");
			skills_played = config.getInt(field_text + "_skills_played");
			powers_played = config.getInt(field_text + "_powers_played");
			
            try {
				config.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.info(QCFP_Misc.debugStringFinishedLoadDataManagement(
            		PrimaryBrush.ID));
            logger.info(QCFP_Misc.classAndSaveSlotText());
        }
		
		else
		{
			logger.info("There's no info, setting variables accordingly.");
			logger.info("Finished setting PrimaryBrush variables.");
		}
		
    }
	
	public static void clear(final SpireConfig config) {
		logger.info(QCFP_Misc.debugStringStartedClearDataManagement(
        		PrimaryBrush.ID));
        logger.info(QCFP_Misc.classAndSaveSlotText());
		
    	String class_name = AbstractDungeon.player.getClass().getName();
    	String field_text = "multicolor_pen_class_" + class_name +
        		"_save_slot_" + CardCrawlGame.saveSlot;
		
        config.remove(field_text + "_attacks_played");
        config.remove(field_text + "_skills_played");
        config.remove(field_text + "_powers_played");
        
        logger.info(QCFP_Misc.debugStringFinishedClearDataManagement(
        		PrimaryBrush.ID));
        logger.info(QCFP_Misc.classAndSaveSlotText());
	}
	
	public boolean canSpawn() {
		if (QCFP_Misc.checkForMod(QCFP_Misc.the_artist_class_code)){
			return AbstractDungeon.player.chosenClass != TheArtist.Enums.THE_ARTIST;
		} else {
			return false;	
		}
		
	}
	
	public AbstractRelic makeCopy() { // always override this method to return a new instance of your relic
		return new PrimaryBrush();
	}
	
}
