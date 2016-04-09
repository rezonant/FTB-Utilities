package ftb.utils.api.guide.repos;

import com.google.gson.JsonElement;
import ftb.utils.mod.config.FTBUConfigGeneral;
import latmod.lib.LMUtils;
import latmod.lib.net.*;

import java.io.File;
import java.util.*;

/**
 * Created by LatvianModder on 03.04.2016.
 */
public class GuideRepoList
{
	public static final Map<String, GuideOnlineRepo> onlineRepos = new HashMap<>();
	public static final Map<String, GuideLocalRepo> localRepos = new HashMap<>();
	
	public static void refreshOnlineRepos()
	{
		onlineRepos.clear();
		long ms = LMUtils.millis();
		
		try
		{
			for(Map.Entry<String, JsonElement> e : new LMURLConnection(RequestMethod.SIMPLE_GET, "https://raw.githubusercontent.com/Slowpoke101/FTBGuides/master/repositories.json").connect().asJson().getAsJsonObject().entrySet())
			{
				try
				{
					GuideOnlineRepo repo = new GuideOnlineRepo(e.getKey(), e.getValue().getAsString());
					onlineRepos.put(repo.getID(), repo);
				}
				catch(Exception ex2)
				{
					System.err.println("Failed to load online repo " + e.getKey());
					//ex2.printStackTrace();
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		System.out.println("Loaded " + onlineRepos.size() + " online repos after " + (LMUtils.millis() - ms) + " ms: " + onlineRepos.values());
	}
	
	public static void refreshLocalRepos()
	{
		localRepos.clear();
		long ms = LMUtils.millis();
		
		try
		{
			File[] folders = FTBUConfigGeneral.guidepacksFolderFile.listFiles();
			
			if(folders != null && folders.length > 0)
			{
				for(File f : folders)
				{
					if(f.isDirectory())
					{
						try
						{
							GuideLocalRepo repo = new GuideLocalRepo(f);
							localRepos.put(repo.getID(), repo);
						}
						catch(Exception ex2)
						{
							System.err.println("Failed to load local repo " + f.getName());
							//ex2.printStackTrace();
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		System.out.println("Loaded " + onlineRepos.size() + " local repos after " + (LMUtils.millis() - ms) + " ms: " + onlineRepos.values());
	}
}