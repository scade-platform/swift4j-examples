import Foundation
import Swift4j
import SwiftyJSON
import os

/// A service for fetching current weather information using online APIs.
///
/// This class provides asynchronous methods to retrieve the current temperature
/// for a given city or geographic coordinates. It uses Open-Meteo for weather data
/// and OpenStreetMap Nominatim for geocoding city names to coordinates.
@jvm
class WeatherServiceSwiftyJson {
    
    private let weatherLog = OSLog(subsystem: "com.example.MyApp", category: "Networking")

    /// Fetches the current temperature for the specified latitude and longitude.
    private func currentTemperature(latitude: Double, longitude: Double, _ response: (Float, String) -> Void) async {
        let url = URL(string: "https://api.open-meteo.com/v1/forecast?latitude=\(latitude)&longitude=\(longitude)&current=temperature_2m")!
        do {
            let (data, _) = try await URLSession.shared.data(from: url)

            os_log("Received data: %{public}@", log: weatherLog, type: .debug, String(decoding: data, as: UTF8.self))

            guard let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
                  let current = json["current"] as? [String: Any],
                  let current_units = json["current_units"] as? [String: Any],
                  let current_temp = current["temperature_2m"] as? Double,
                  let current_temp_units = current_units["temperature_2m"] as? String else {
                return
            }

            os_log("Parsed temperature: %f %{public}@", log: weatherLog, type: .info, current_temp, current_temp_units)

            response(Float(current_temp), current_temp_units)
        } catch {
            os_log("Error fetching temperature: %{public}@", log: weatherLog, type: .error, error.localizedDescription)
        }
    }

    /// Fetches the current temperature for the specified city name.
    func currentTemperature(city: String, _ response: (Float, String) -> Void) async {
        do {
            let url = URL(string: "https://nominatim.openstreetmap.org/search?city=\(city)&format=json")!
            let (data, _) = try await URLSession.shared.data(from: url)

            let json = try JSON(data: data)
            guard let first = json.arrayValue.first else { return }
            guard let latStr = first["lat"].string,
                  let lonStr = first["lon"].string,
                  let lat = Double(latStr),
                  let lon = Double(lonStr) else {
                os_log("Cannot parse coordinates: lat=%{public}@ lon=%{public}@", log: weatherLog, type: .error,
                       first["lat"].rawString() ?? "nil", first["lon"].rawString() ?? "nil")
                return
            }

            os_log("Geocoded city '%{public}@' to lat=%f lon=%f", log: weatherLog, type: .info, city as NSString, lat, lon)


            await currentTemperature(latitude: lat, longitude: lon, response)
        } catch {
            os_log("Error fetching city temperature: %{public}@", log: weatherLog, type: .error, error.localizedDescription)
        }
    }
}
