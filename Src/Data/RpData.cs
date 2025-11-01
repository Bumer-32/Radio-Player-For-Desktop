using System.Text.Json.Serialization;
using System.Collections.Generic;

namespace RadioPlayerForDesktop.Data;

public record RpData([property: JsonPropertyName("stations")] List<RpData.StationData> Stations)
{
    public record StationData(
        [property: JsonPropertyName("id")] int Id,
        [property: JsonPropertyName("enabled")] bool Enabled,
        // [property: JsonPropertyName("sorting_id")] int SortingId,
        // [property: JsonPropertyName("url")] string Url,
        [property: JsonPropertyName("names")] Dictionary<string, string> Names,
        // [property: JsonPropertyName("tags")] Dictionary<string, string> Tags,
        [property: JsonPropertyName("logo")] string LogoUrl,
        // [property: JsonPropertyName("current_song")] string CurrentSongUrl,
        [property: JsonPropertyName("streams")] StationData.StreamsData Streams
    )
    {

        public record StreamsData(
            [property: JsonPropertyName("regular")] Dictionary<string, string> Regular,
            [property: JsonPropertyName("hd")] Dictionary<string, string> Hd
        );
    }
    
}