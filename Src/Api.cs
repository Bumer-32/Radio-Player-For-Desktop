using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using RadioPlayerForDesktop.Data;
using Serilog;

namespace RadioPlayerForDesktop;

public static class Api
{
    private const string ApiEndpoint = "https://api.radioplayer.ua/config";
    private static RpData? _config;

    internal static async Task<RpData> Get()
    {
        if (_config != null)
        {
            return _config;
        }
        
        using var client = new HttpClient();

        var response = await client.GetAsync(ApiEndpoint);

        if (response.IsSuccessStatusCode)
        {
            try
            {
                var content = await response.Content.ReadAsStringAsync();
                _config = JsonSerializer.Deserialize<RpData>(content)!;
            }
            catch (Exception e)
            {
                Log.Fatal(e.ToString());
                Environment.Exit(1);
            }
        }
        else
        {
            Log.Fatal(response.StatusCode.ToString());
            Environment.Exit(1);
        }
        
        return _config;
    }

    // Downloads radio logos and caches it
    internal static async Task CheckAndPreload()
    {
        Log.Information("Checking cache");
        
        using var client = new HttpClient();
        Dictionary<int, string> modifiedAt;
        
        if (File.Exists(Constants.ModifiedMapPath))
        {
            if (DateTime.UtcNow - File.GetLastWriteTimeUtc(Constants.ModifiedMapPath) < TimeSpan.FromHours(24))
            {
                Log.Information("Modifications file modified less than 24 hours ago, skip cache checking");
                return;
            }
            
            modifiedAt = JsonSerializer.Deserialize<Dictionary<int, string>>(await File.ReadAllTextAsync(Constants.ModifiedMapPath)) ?? new();
        }
        else
        {
            modifiedAt = new();
        }
        
        var config = await Get();

        Directory.CreateDirectory(Constants.CachePath);

        foreach (var station in config.Stations)
        {
            if (station.Enabled)
            {
                var file = Path.Combine(Constants.CachePath, station.Id + ".png");
                
                if (File.Exists(file) && modifiedAt.ContainsKey(station.Id))
                {
                    var request = new HttpRequestMessage(HttpMethod.Get, station.LogoUrl);
                    request.Headers.Add("If-Modified-Since", modifiedAt[station.Id]);
                    
                    var response = await client.SendAsync(request);

                    if (response.StatusCode == HttpStatusCode.OK) // if 304 skip
                    {
                        await File.WriteAllBytesAsync(file, await response.Content.ReadAsByteArrayAsync());
                    }
                }
                else
                {
                    Log.Debug($"Station {station.Id} redownloading");
                    var response = await client.GetAsync(station.LogoUrl);
                    await File.WriteAllBytesAsync(file, await response.Content.ReadAsByteArrayAsync());

                    if (response.Content.Headers.TryGetValues("Last-Modified", out var lastModified))
                    {
                        modifiedAt[station.Id] = lastModified.First();
                    }
                }
            }
        }
        
        await File.WriteAllTextAsync(Constants.ModifiedMapPath, JsonSerializer.Serialize(modifiedAt, new JsonSerializerOptions { WriteIndented = true }));
    }
}