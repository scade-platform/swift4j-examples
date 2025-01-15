import Foundation
import Swift4j

@jvm
public class WeatherService {
  // Response: (temp, temp_units)
  func currentTemperature(latitude: Float, longitude: Float, _ response: (Float, String) -> Void) async {
    let url = URL(string: "http://api.open-meteo.com/v1/forecast?latitude=\(latitude)&longitude=\(longitude)&current=temperature_2m")!

    do {
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
}
