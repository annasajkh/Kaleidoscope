package com.github.annasajkh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.annasajkh.Kaleidoscope;

public class DesktopLauncher
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 600;
		config.title = "Random Kaleidoscope";
		new LwjglApplication(new Kaleidoscope(), config);
	}
}
