package game;

import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
        totalVolume = volume;
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

    public static void setVolume(double volume) {
        totalVolume = volume;
        for (MediaPlayer mediaPlayer : musicMap.values()) {
            mediaPlayer.setMute(musicMute);
            mediaPlayer.setVolume(totalVolume);
        }
        for (MediaPlayer mediaPlayer : soundMap.values()) {
            mediaPlayer.setMute(soundMute);
            mediaPlayer.setVolume(totalVolume);
        }
    }
}
