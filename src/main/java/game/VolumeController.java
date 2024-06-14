package game;


import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
public class VolumeController {
    private static double totalVolume = 0.5;
    private static boolean musicMute = false;
    private static boolean soundMute = false;

    private static HashMap<String, MediaPlayer> musicMap = new HashMap<>();
    private static HashMap<String, MediaPlayer> soundMap = new HashMap<>();

    static {
        initializeMaps();
    }

    private static void initializeMaps() {
        String[] musicNames = {"menu", "level1", "level2", "level3", "victory"};
        for (String name : musicNames) {
            musicMap.put(name, createMediaPlayer(name + "_music.mp3"));
        }

        String[] soundNames = {"attack", "jump", "lightning", "die", "mouse_click", "get_box", "get_hurt", "open_door", "bomb"};
        for (String name : soundNames) {
            soundMap.put(name, createMediaPlayer(name + ".mp3"));
        }
    }

    private static MediaPlayer createMediaPlayer(String fileName) {
        return new MediaPlayer(new Media(VolumeController.class.getResource(fileName).toString()));
    }


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
        if (volume <= 0) {
            totalVolume = 0;
        } else if (volume >= 1) {
            totalVolume = 1;
        } else {
            totalVolume = volume;
        }

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

    public static double getTotalVolume() {
        return totalVolume;
    }

    public static boolean getMusicMute() {
        return musicMute;
    }

    public static boolean getSoundMute() {
        return soundMute;
    }
}
