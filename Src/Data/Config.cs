using System.Text.Json.Serialization;

namespace RadioPlayerForDesktop.Data;

public record Config(
    [property: JsonPropertyName("current_station_id")] int StationId,
    [property: JsonPropertyName("hd")] bool Hd,
    [property: JsonPropertyName("volume")] float Volume
);