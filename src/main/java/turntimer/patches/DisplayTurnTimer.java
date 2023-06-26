package turntimer.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import turntimer.TurnTimerMain;
import turntimer.util.TextureLoader;

import java.util.ArrayList;
import java.util.Set;

public class DisplayTurnTimer
{
    @SpirePatch(clz= AbstractRoom.class,method="render")
    public static class RenderTTUI
    {
        private static float x = Settings.WIDTH / 2;
        private static float y = ((float) ReflectionHacks.getPrivateStatic(AbstractRelic.class, "START_Y")) - (64f * Settings.scale);
        private static String text = CardCrawlGame.languagePack.getUIString(TurnTimerMain.makeID("TimerText")).TEXT[0];
        //static Texture bg = TextureLoader.getTexture("turntimer/ui/timertextbg.png");
        static Texture bg = TextureLoader.getTexture("turntimer/ui/hourglass.png");

        @SpireInsertPatch(locator=Locator.class)
        public static void Insert(AbstractRoom __instance, SpriteBatch spriteBatch)
        {
            String realText = text;
            //x -= FontHelper.getSmartWidth(FontHelper.panelNameFont, text, 999f, 0f);
            x = (Settings.WIDTH / 2f) - ((FontHelper.getSmartWidth(FontHelper.panelNameFont, realText, 999f, 0f) / 2f)*Settings.xScale);
            if (!AbstractDungeon.getCurrRoom().isBattleOver)
            {
                if (EndOfTurn.getTime() < 0)
                {
                    realText += "--";
                }
                else
                {
                    realText += EndOfTurn.getTime() + " seconds";
                }

                Color c = spriteBatch.getColor();
                spriteBatch.setColor(Color.WHITE);
                //spriteBatch.draw(bg, x - 100f * Settings.xScale, y - 60f * Settings.yScale, 0, 0, bg.getWidth() * Settings.xScale, bg.getHeight() * Settings.yScale, 1, 1, 1);
                //spriteBatch.draw(bg, x, y, bg.getWidth() * Settings.scale / 2.0F, bg.getHeight() * Settings.scale / 2.0F, bg.getWidth() * Settings.scale, bg.getHeight() * Settings.scale, 0.2F, 0.2F, 0, 0, 0, bg.getWidth(), bg.getHeight(), false, false);
                //spriteBatch.draw(bg, x, y, 0, 0, bg.getWidth() * Settings.scale, bg.getHeight() * Settings.scale, 1F, 1F, 0, 0, 0, bg.getWidth(), bg.getHeight(), false, false);
                FontHelper.renderFontLeft(spriteBatch, FontHelper.panelNameFont, realText, x, y, Settings.CREAM_COLOR);
//                FontHelper.renderFontLeft(spriteBatch, FontHelper.panelNameFont, text, x + (200/2f), y, Settings.CREAM_COLOR);
                spriteBatch.setColor(c);
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "renderPlayerBattleUi");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
