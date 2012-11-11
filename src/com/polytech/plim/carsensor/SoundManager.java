package com.polytech.plim.carsensor;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {

	private static MediaPlayer mMediaPlayer;
	
	public static void play(Context ctx,int raw){
		 mMediaPlayer = MediaPlayer.create(ctx, raw);
		 mMediaPlayer.start();
		 //mMediaPlayer.stop();
//		 mMediaPlayer.release();
//		 mMediaPlayer = null;
	}
	
	
}
