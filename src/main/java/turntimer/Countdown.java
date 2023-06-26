//package turntimer;
//
//import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import turntimer.patches.EndOfTurn;
//
//import static turntimer.patches.EndOfTurn.getTime;
//
//public class Countdown
//{
//    Thread thread;
//    public Countdown(int turnTime)
//    {
//        thread = new Thread(new Q());
//        EndOfTurn.setTime(turnTime);
//        TurnTimerMain.logger.info("Starting Countdown");
//        thread.start();
//    }
//
//    public void stop()
//    {
//        if (thread.isAlive()) thread.interrupt();
//    }
//
//    private class Q implements Runnable
//    {
//        @Override
//        public void run() {
//            //TurnTimerMain.logger.info("Started Countdown");
//            try
//            {
//                for (int i = 0; i < getTime(); i++)
//                {
//                    this.wait(1000);
//                    EndOfTurn.decrementTime();
//                    //TurnTimerMain.logger.info(getTime());
//                }
//                //TurnTimerMain.logger.info("Ending Turn");
//                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new PressEndTurnButtonAction());
//            }
//            catch (InterruptedException e)
//            {
//                EndOfTurn.setTime(0);
//                return;
//            }
//        }
//    }
//}
