package com.omikronsoft.customsoundboard.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.omikronsoft.customsoundboard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dariusz Lelek on 5/27/2017.
 * dariusz.lelek@gmail.com
 */

public class AudioPlayer {
    private static AudioPlayer instance;
    private final List<MediaPlayer> activeMedia;
    private MediaPlayer listItemCurrentlyPlaying;
    
    private AudioPlayer(){
        activeMedia = new ArrayList<>();
        Context context = ApplicationContext.get();
    }

    public void playSound(MediaPlayer media){
        play(media);
    }

    private void play(MediaPlayer player) {
        if(!activeMedia.contains(player)){
            activeMedia.add(player);
        }

        if (player.isPlaying()) {
            player.pause();
            player.seekTo(0);
        }
        player.start();
    }

    public void playListItem(MediaPlayer player, int offset){
        stopPlayingListItem();
        listItemCurrentlyPlaying = player;
        listItemCurrentlyPlaying.seekTo(offset);
        listItemCurrentlyPlaying.start();
    }

    public void stopPlayingListItem(){
        if(listItemCurrentlyPlaying != null && listItemCurrentlyPlaying.isPlaying()){
            listItemCurrentlyPlaying.pause();
        }
    }

    public void playWithOffset(MediaPlayer player, int offset){
        if(!activeMedia.contains(player)){
            activeMedia.add(player);
        }

        if (player.isPlaying()) {
            player.pause();
            //player.seekTo(offset);
        }
        player.seekTo(offset);
        player.start();
    }

    public void stopAll(){
        for(MediaPlayer player : activeMedia){
            if (player.isPlaying()) {
                player.pause();
            }
        }
    }
        
    public synchronized static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }
}
