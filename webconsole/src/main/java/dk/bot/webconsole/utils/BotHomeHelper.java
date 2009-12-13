package dk.bot.webconsole.utils;

/** Get the bot home directory
 * 
 * @author daniel
 *
 */
public class BotHomeHelper {

	public static String getBotHome() {
		String baseDir = System.getenv("BOT_HOME");
		if(baseDir==null) {
			baseDir="./";
		}
		return baseDir;
	}
}
