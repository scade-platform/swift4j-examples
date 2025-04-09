import Foundation
import Swift4j

@jvm
class WeatherService {
  // Response: (temp, temp_units)

  func currentTemperature(latitude: Double, longitude: Double, _ response: (Float, String) -> Void) async {
    do {
      let url = URL(string: "https://api.open-meteo.com/v1/forecast?latitude=\(latitude)&longitude=\(longitude)&current=temperature_2m")!
      let (data, _) = try await URLSession.shared.data(from: url)

      guard let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
            let current = json["current"] as? [String: Any],
            let current_units = json["current_units"] as? [String: Any],
            let current_temp = current["temperature_2m"] as? Double,
            let current_temp_units = current_units["temperature_2m"] as? String else { return }

      response(Float(current_temp), current_temp_units)

    } catch {
#if os(Android)
      logcat_log(error.localizedDescription)
#else
      print(error)
#endif
      return
    }
  }

  private func currentTemperature(city: String, _ response: (Float, String) -> Void) async {
    do {
      let url = URL(string: "https://nominatim.openstreetmap.org/search?city=\(city)&format=json")!
      let (data, _) = try await URLSession.shared.data(from: url)

      guard let json = try JSONSerialization.jsonObject(with: data) as? [Any],
            let cityData = json.first as? [String: Any] else {
        return
      }

      guard let lat = cityData["lat"] as? String,
            let lon = cityData["lon"] as? String else {
        return
      }

      if let lat = Double(lat), let lon = Double(lon) {
        await currentTemperature(latitude: lat, longitude: lon, response)

      } else {
#if os(Android)
      logcat_log("Cannot parse coordinates: \(lat), \(lon)")
#else
        print("Cannot parse coordinates: \(lat), \(lon)")
#endif
      }

    } catch {
#if os(Android)
      logcat_log(error.localizedDescription)
#else
      print(error)
#endif
      return
    }
  }
}
