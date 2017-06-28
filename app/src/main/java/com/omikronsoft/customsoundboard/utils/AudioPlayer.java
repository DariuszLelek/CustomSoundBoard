package com.omikronsoft.customsoundboard.utils;

import android.media.MediaPlayer;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class AudioPlayer {
    private static AudioPlayer instance;
    private final Map<MediaPlayer, Integer> activeMedia;
    private final Map<MediaPlayer, Integer> activeLoopingMedia;
    private MediaPlayer listItemCurrentlyPlaying;
    private int threadNum;

    private AudioPlayer() {
        activeMedia = new HashMap<>();
        activeLoopingMedia = new HashMap<>();
        threadNum = 0;
    }

    public void playListItem(MediaPlayer player, int offset) {
        stopPlayingListItem();
        listItemCurrentlyPlaying = player;
        listItemCurrentlyPlaying.seekTo(offset);
        listItemCurrentlyPlaying.start();
    }

    public void stopPlayingListItem() {
        if (listItemCurrentlyPlaying != null && listItemCurrentlyPlaying.isPlaying()) {
            listItemCurrentlyPlaying.pause();
        }
    }

    public void loopWithOffset(final MediaPlayer player, final int startOffset, final int endOffset){
        if(!activeLoopingMedia.containsKey(player)){
            activeLoopingMedia.put(player, threadNum);
            Thread play = new Thread(new Runnable() {
                @Override
                public void run() {
                    final int sleepTime = player.getDuration() - startOffset - endOffset;
                    final int threadNumber = threadNum++;

                        while(activeLoopingMedia.containsKey(player) && threadNumber == activeLoopingMedia.get(player)){
                            playWithOffset(player, startOffset);
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                }
            });

            play.start();
        }
    }

    public synchronized boolean isLooping(MediaPlayer player){
        return activeLoopingMedia.containsKey(player);
    }

    public synchronized void stopLoop(MediaPlayer player){
        if(activeLoopingMedia.containsKey(player)){
            stopMedia(player);
            activeLoopingMedia.remove(player);
        }
    }

    private void playWithOffset(MediaPlayer player, int startOffset){
        if (!activeMedia.containsKey(player)) {
            activeMedia.put(player, threadNum);
        }

        if (player.isPlaying()) {
            player.pause();
        }

        player.seekTo(startOffset);
        player.start();
    }

    public void playWithOffset(final MediaPlayer player,final int startOffset,final int endOffset) {
        if(!activeMedia.containsKey(player)){
            activeMedia.put(player, threadNum);
        }else{
            activeMedia.put(player, activeMedia.get(player) + 1);
        }

        if (player.isPlaying()) {
            player.pause();
        }

        player.seekTo(startOffset);
        player.start();

        if(endOffset > 0){
            new Handler().postDelayed(new Runnable() {
                final int threadNumber = threadNum++;
                @Override
                public void run() {
                    if(activeMedia.containsKey(player) && threadNumber == activeMedia.get(player)){
                        player.pause();
                        activeMedia.remove(player);
                    }
                }
            }, player.getDuration() - startOffset - endOffset);
        }
    }

    public synchronized void stopAll() {
        activeLoopingMedia.clear();
        for (MediaPlayer player : activeMedia.keySet()) {
            if (player.isPlaying()) {
                player.pause();
            }
        }
        activeMedia.clear();
    }

    public void stopMedia(MediaPlayer media) {
        if(activeMedia.containsKey(media)){
            media.pause();
            activeMedia.remove(media);
        }
    }

    public synchronized static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }
}
