package turntimer.patches;

import basemod.interfaces.OnPlayerTurnStartPostDrawSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDeathSubscriber;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import turntimer.TurnTimerMain;

import java.util.Timer;
import java.util.TimerTask;

public class EndOfTurn implements
        OnPlayerTurnStartPostDrawSubscriber,
        PostDeathSubscriber,
        PostBattleSubscriber

{
    private static int turnTime = 5;
    private static int currentTime = 0;
    public static int getTime()
    {
        return currentTime;
    }

    public static void setTurnTime(int time)
    {
        turnTime = time;
    }
    private static Timer timer = new Timer();
    @Override
    public void receiveOnPlayerTurnStartPostDraw()
    {
        currentTime = turnTime;
        timer.cancel();
        TurnTimerMain.logger.info("Turn has started");
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (currentTime <= 0)
                {
                    AbstractDungeon.actionManager.addToBottom(new PressEndTurnButtonAction());
                    this.cancel();
                }
                currentTime--;

            }
        }, 1000, 1000);
    }

    @Override
    public void receivePostDeath()
    {
        timer.cancel();
        currentTime = -1;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom)
    {
        timer.cancel();
        currentTime = -1;
    }

    @SpirePatch(clz=GameActionManager.class,method="endTurn")
    public static class EndTurnHook
    {
        @SpirePrefixPatch
        public static void Prefix(GameActionManager __instance)
        {
            timer.cancel();
            currentTime = -1;
        }
    }

}
