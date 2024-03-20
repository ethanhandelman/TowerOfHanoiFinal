package com.mirohaap.towerofhanoitutor;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoPlayUtil {
    private DragDropUtil dragDropUtil;
    private ScheduledExecutorService exec;
    private MutableBoolean reenable;
    public AutoPlayUtil(DragDropUtil dragDropUtil){
        this.dragDropUtil = dragDropUtil;
        reenable = new MutableBoolean(false);
    }

    public void beginPlaying(int interval){
        dragDropUtil.disableUserInput();

        exec = Executors.newSingleThreadScheduledExecutor();
        Runnable makeNextMove = new Runnable() {
            @Override
            public void run() {
                Move next = Tutor.getInstance().getNextMove();
                next.setValid(true);
                Repository.getInstance().applyMove(next);
                Platform.runLater(() -> dragDropUtil.animateMove(next, interval * 0.9, reenable));

                /*if(Repository.getInstance().checkWin()){
                    exec.shutdown();
                    System.out.println("Autoplay won!");
                }*/

            }
        };

        exec.scheduleAtFixedRate(makeNextMove, 0, interval, TimeUnit.MILLISECONDS);
    }

    public void stopPlaying(){
        exec.shutdown();
        if(AnimationRepository.getInstance().animationsRunning()){
            reenable.setTrue();
        }
        else{
            dragDropUtil.enableUserInput();
        }

    }
}
