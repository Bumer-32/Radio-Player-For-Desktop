using System;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using NAudio.Wave;
using RadioPlayerForDesktop.Data;
using Serilog;

namespace RadioPlayerForDesktop;

public static class Audio
{
    private static int? _currentStationId;
    private static bool _isHdEnabled;
    private static float _volume = 1.0f;
    private static MediaFoundationReader? _mf;
    private static WaveOutEvent? _wo;
    
    public static async Task Play()
    {
        if (_currentStationId == null)
        {
            Log.Fatal("Current station not found");
            Environment.Exit(1);
        }

        var config = await Api.Get();
        var station = config.Stations.Find(station => station.Id == _currentStationId);

        string url = _isHdEnabled ? station!.Streams.Hd["android"] : station!.Streams.Regular["android"];
        
        Log.Information($"Playing station {_currentStationId} : {url}");
        
        _mf = new MediaFoundationReader(url);
        _wo = new WaveOutEvent();

        _wo.Volume = _volume;
        _wo.Init(_mf);
        _wo.Play();

        while (IsPlaying())
        {
            Thread.Sleep(100);
        }
    }

    public static void Stop()
    {
        _wo?.Stop();
        _wo?.Dispose();
        _mf?.Dispose();
        _mf = null;
        _wo = null;
    }

    public static bool IsPlaying() => _wo?.PlaybackState == PlaybackState.Playing;

    public static void SetStation(int stationId)
    {
        Log.Information("Current station " + stationId);
        _currentStationId = stationId;

        if (IsPlaying())
        {
            Stop();
            Task.Run(Play);
        }
        
        Task.Run(SaveConfig);
    }
    
    public static int GetStationId() => _currentStationId ?? -1;

    public static void SetHd(bool isHd)
    {
        _isHdEnabled = isHd;
        
        if (IsPlaying())
        {
            Stop();
            Task.Run(Play);
        }
    }
    
    public static bool IsHdEnabled() => _isHdEnabled;

    public static void SetVolume(float volume)
    {
        _volume = volume;
        if (_wo != null) _wo.Volume = volume;
    }
    
    public static float GetVolume() => _volume;

    public static async void LoadConfig()
    {
        if (File.Exists(Constants.ConfigFile))
        {
            var config = JsonSerializer.Deserialize<Config>(File.ReadAllText(Constants.ConfigFile));
            if (config == null) return;
            
            SetVolume(config.Volume);
            SetStation(config.StationId);
            SetHd(config.Hd);
            
        }
        else
        {
            var config = await Api.Get();
            var station = config.Stations.First(station => station.Enabled);
            SetStation(station.Id);
        }
    }

    public static async Task SaveConfig()
    {
        await File.WriteAllTextAsync(Constants.ConfigFile, JsonSerializer.Serialize(new Config(GetStationId(), IsHdEnabled(), GetVolume()), new JsonSerializerOptions { WriteIndented = true}));
    }
}