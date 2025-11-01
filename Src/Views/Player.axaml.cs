using System;
using System.IO;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.Media.Imaging;
using Avalonia.Platform;
using Avalonia.Threading;
using Serilog;

namespace RadioPlayerForDesktop.Views;

public partial class Player : UserControl
{
    public Player()
    {
        InitializeComponent();
        
        Hdbutton.Opacity = Audio.IsHdEnabled() ? 1 : 0.5;
        VolumeSlider.Value = Audio.GetVolume() * 100;

        Task.Run(() => ChangeStation(Audio.GetStationId()));

        StationsList.OnStationChanged += id => Task.Run(() => ChangeStation(id));

        VolumeSlider.ValueChanged += (_, e) =>
        {
            Log.Information("Volume: " + (float)e.NewValue / 100f);
            Audio.SetVolume((float)e.NewValue / 100f);
        };
    }
    
    

    private async Task ChangeStation(int stationId)
    {
        var config = await Api.Get();
        var station = config.Stations.Find(station => station.Id == stationId)!;

        await Dispatcher.UIThread.InvokeAsync(() =>
        {
            Logo.Source = new Bitmap(Path.Combine(Constants.CachePath, stationId + ".png"));
            StationLabel.Content = station.Names["uk"];
        });
    }

    private void HdButtonClick(object? sender, RoutedEventArgs e)
    {
        Audio.SetHd(!Audio.IsHdEnabled());
        Hdbutton.Opacity = Audio.IsHdEnabled() ? 1 : 0.5;
        
        Task.Run(Audio.SaveConfig);
    }

    private void PlayButtonClick(object? sender, RoutedEventArgs e)
    {
        if (Audio.IsPlaying())
        {
            Audio.Stop();
            PlayButton.Source = new Bitmap(AssetLoader.Open(new Uri("avares://RadioPlayerForDesktop/Assets/play.png")));
        }
        else
        {
            Task.Run(Audio.Play);
            PlayButton.Source = new Bitmap(AssetLoader.Open(new Uri("avares://RadioPlayerForDesktop/Assets/pause.png")));
        }
    }

    private void VolumeSlider_OnPointerReleased(object? sender, PointerReleasedEventArgs e)
    {
        Task.Run(Audio.SaveConfig);
    }
}