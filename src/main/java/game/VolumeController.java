package game;


import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
public class VolumeController {
    public static double totalVolume = 0.5;
    public static boolean musicMute = false;
    public static boolean soundMute = false;

    private static HashMap<String, MediaPlayer> musicMap = new HashMap<>() {
        {
            put("level1", new MediaPlayer(new Media(getClass().getResource("level1_music.mp3").toString())));
            put("level2", new MediaPlayer(new Media(getClass().getResource("level2_music.mp3").toString())));
        }
    };

    private static HashMap<String, MediaPlayer> soundMap = new HashMap<>() {
        {
            put("attack", new MediaPlayer(new Media(getClass().getResource("attack.mp3").toString())));
        }
    };

    public static void playMusic(String musicName) {
        MediaPlayer mediaPlayer = musicMap.get(musicName);
        mediaPlayer.setVolume(totalVolume);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public static void playSound(String soundName) {
        MediaPlayer mediaPlayer = soundMap.get(soundName);
        mediaPlayer.setVolume(totalVolume);
        mediaPlayer.setCycleCount(1);
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }

    public static void stopMusic(String musicName) {
        MediaPlayer mediaPlayer = musicMap.get(musicName);
        mediaPlayer.stop();
    }

    public static void stopSound(String soundName) {
        MediaPlayer mediaPlayer = soundMap.get(soundName);
        mediaPlayer.stop();
    }

    public static void setTotalVolume(double volume) {
        System.out.println(volume);
        if (volume <= 0) {
            totalVolume = 0;
        } else if (volume >= 1) {
            totalVolume = 1;
        } else {
            totalVolume = volume;
        }
        System.out.println(totalVolume);

        for (MediaPlayer mediaPlayer : musicMap.values()) {
            mediaPlayer.setMute(musicMute);
            mediaPlayer.setVolume(totalVolume);
        }
        for (MediaPlayer mediaPlayer : soundMap.values()) {
            mediaPlayer.setMute(soundMute);
            mediaPlayer.setVolume(totalVolume);
        }
    }

    public static void setMusicMute(boolean mute) {
        musicMute = mute;
        for (MediaPlayer mediaPlayer : musicMap.values()) {
            mediaPlayer.setMute(mute);
        }
    }

    public static void setSoundMute(boolean mute) {
        soundMute = mute;
        for (MediaPlayer mediaPlayer : soundMap.values()) {
            mediaPlayer.setMute(mute);
        }
    }
}
