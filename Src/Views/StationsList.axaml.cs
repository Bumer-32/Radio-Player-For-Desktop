using System;
using System.IO;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Input;
using Avalonia.Media;
using Avalonia.Media.Imaging;

namespace RadioPlayerForDesktop.Views;

public partial class StationsList : UserControl
{
    public static event Action<int> OnStationChanged; 
    
    public StationsList()
    {
        InitializeComponent();
        AddStations();
    }
    
    private async void AddStations()
    {
        var config = await Api.Get();

        foreach (var station in config.Stations)
        {
            if (station.Enabled)
            {
                var button = new Button
                {
                    Content = new Image
                    {
                        Source = new Bitmap(Path.Combine(Constants.CachePath, station.Id + ".png")),
                        Width = 128,
                        Height = 128,
                    },
                    Background = Brushes.Transparent,
                };

                button.Click += (_, _) =>
                {
                    Audio.SetStation(station.Id);
                    OnStationChanged.Invoke(station.Id);
                };
                
                Stations.Items.Add(button);
            }
        }
    }
    
    private void InputElement_OnPointerWheelChanged(object? sender, PointerWheelEventArgs e)
    {
        Scroll.Offset = new Vector(Scroll.Offset.X - e.Delta.Y * 50, Scroll.Offset.Y);
    }
}