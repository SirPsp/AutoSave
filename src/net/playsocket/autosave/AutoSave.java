package net.playsocket.autosave;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.utils.PropertiesFile;

public class AutoSave extends Plugin {

	public static String directory = "config" + File.separator;
	public static PropertiesFile props = new PropertiesFile(directory + File.separator + "AutoSave.cfg");
	static long SaveDelay;
	static Timer writeTimer = new Timer();

	@Override
	public void disable() {
		writeTimer.cancel();
		getLogman().logInfo(getName() + " v" + getVersion() + " disabled");
	}

	@Override
	public boolean enable() {
		new File(directory).mkdir();
		SaveDelay = 60000 * props.getInt("autoSaveDelay", 30);
		props.save();
		onPluginReload();
		getLogman().logInfo(getName() + " v"+ getVersion() + " by " + getAuthor() + " enabled");
		return true;
	}

	private void onPluginReload() {
		writeTimer.schedule(new AutoSave.WriteJob(), SaveDelay);
	}
	public class WriteJob extends TimerTask{
		public void run(){
			getLogman().logInfo("AutoSave saving...");
			Canary.getServer().consoleCommand("save-all");
			AutoSave.writeTimer.schedule(new WriteJob(), AutoSave.SaveDelay);
			
		}
	}

}
