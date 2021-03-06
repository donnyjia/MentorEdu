package com.mentor.pte.tools;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class Test {

    public static void main(String [] args) {
        try {
            AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(new File("D:/123.mp3"));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("D:/123.mp3"));

            AudioFormat audioFormat = ais.getFormat();

            System.out.println("File Format Type: " + inputFileFormat.getType());
            System.out.println("File Format String: " + inputFileFormat.toString());
            System.out.println("File lenght: " + inputFileFormat.getByteLength());
            System.out.println("Frame length: " + inputFileFormat.getFrameLength());
            System.out.println("Channels: " + audioFormat.getChannels());
            System.out.println("Encoding: " + audioFormat.getEncoding());
            System.out.println("Frame Rate: " + audioFormat.getFrameRate());
            System.out.println("Frame Size: " + audioFormat.getFrameSize());
            System.out.println("Sample Rate: " + audioFormat.getSampleRate());
            System.out.println("Sample size (bits): " + audioFormat.getSampleSizeInBits());
            System.out.println("Big endian: " + audioFormat.isBigEndian());
            System.out.println("Audio Format String: " + audioFormat.toString());

            AudioInputStream encodedASI = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, ais);

            try {
                int i = AudioSystem.write(encodedASI, AudioFileFormat.Type.AIFF, new File("d:\\1.mp3"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
