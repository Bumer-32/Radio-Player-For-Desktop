using System.Text.Json.Serialization;
using System.Collections.Generic;

namespace RadioPlayerForDesktop.Services.Data;

public class RpData
{
    [JsonPropertyName("stations")] public required List<StationData> Stations { get; init; }

    public class StationData
    {
        [JsonPropertyName("id")] public required int Id { get; init; }
        [JsonPropertyName("enabled")] public required bool Enabled { get; init; }
        [JsonPropertyName("sorting_id")] public required int SortingId { get; init; }
        [JsonPropertyName("url")] public required string Url { get; init; }
        [JsonPropertyName("names")] public required Dictionary<string, string> Names { get; init; }
        [JsonPropertyName("tags")] public required Dictionary<string, string> Tags { get; init; }
        [JsonPropertyName("logo")] public required string LogoUrl { get; init; }
        [JsonPropertyName("current_song")] public required string CurrentSongUrl { get; init; }
        [JsonPropertyName("streams")] public required StreamsData Streams { get; init; }

        public class StreamsData
        {
            [JsonPropertyName("regular")] public required Dictionary<string, string> Regular { get; init; }
            [JsonPropertyName("hd")] public required Dictionary<string, string> Hd { get; init; }
        } 
    }
    
}