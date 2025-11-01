using System;
using System.IO;

namespace RadioPlayerForDesktop;

public static class Constants
{
    public static readonly string LocalAppDataPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData), "Radio Player");
    
    public static readonly string LogFile = Path.Combine(LocalAppDataPath, "Latest.log");
    public static readonly string ConfigFile = Path.Combine(LocalAppDataPath, "Config.json");
    
    public static readonly string CachePath = Path.Combine(LocalAppDataPath, "Cache");
    public static readonly string ModifiedMapPath = Path.Combine(CachePath, "Modified.json");
}