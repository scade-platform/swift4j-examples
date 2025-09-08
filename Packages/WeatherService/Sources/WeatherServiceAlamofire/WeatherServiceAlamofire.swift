import Foundation
import Swift4j
import Alamofire
import os

struct HexEncodingOptions: OptionSet {
  let rawValue: Int
  static let upperCase = HexEncodingOptions(rawValue: 1 << 0)
}

extension Sequence where Element == UInt8 {
  func hexEncodedString(options: HexEncodingOptions = []) -> String {
    let format = options.contains(.upperCase) ? "%02hhX" : "%02hhx"
    return self.map { String(format: format, $0) }.joined()
  }
}

/// A service for fetching current weather information using online APIs and Alamofire.
///
/// This class provides asynchronous methods to retrieve the current temperature
/// for a given city or geographic coordinates. It uses Open-Meteo for weather data
/// and OpenStreetMap Nominatim for geocoding city names to coordinates.
///
/// This class uses Alamofire for network requests.
@jvm
final class WeatherServiceAlamofire: Sendable {

  /// Fetches the current temperature for the specified latitude and longitude.
  ///
  /// - Parameters:
  ///   - latitude: The latitude of the location.
  ///   - longitude: The longitude of the location.
  ///   - response: A closure called with the temperature value and its units.
  ///
  /// This method makes a network request to the Open-Meteo API to retrieve the
  /// current temperature at the given coordinates. The result is returned via the
  /// response closure. If the request fails or the data cannot be parsed, the closure is not called.
  private func currentTemperature(latitude: Double, longitude: Double, _ callback: @escaping (Float, String) -> Void) {
  #if os(Android)
    logcat_log("currentTemperature: latitude=\(latitude), longitude=\(longitude)")
  #else
    print("currentTemperature: latitude=\(latitude), longitude=\(longitude)")
  #endif

    // Construct the API URL for the given coordinates
    // https://api.open-meteo.com/v1/forecast?latitude=52.510885&longitude=13.3989367&current=temperature_2m
    let url = URL(string: "https://api.open-meteo.com/v1/forecast?latitude=\(latitude)&longitude=\(longitude)&current=temperature_2m")!

    // Perform the network request asynchronously
    // NOTE: disable gzip compression
    var headers = HTTPHeaders()
    headers.add(HTTPHeader(name: "Accept-Encoding", value: "identity"));
    AF.request(url, headers: headers).response { response in
  #if os(Android)
        logcat_log("Response: \(response), headers: \(response.response!.allHeaderFields)")
  #else
        print("Response: \(response), headers: \(response.response!.allHeaderFields)")
  #endif

      if let error = response.error {
  #if os(Android)
        logcat_log("Failed to get temperature: \(error)")
  #else
        print("Failed to get temperature: \(error)")
  #endif
        return
      }

      let data = response.data!

      do {
#if os(Android)
        // Log the received data for debugging on Android
        logcat_log("Received data size: \(data.count)")
        logcat_log("Received data: \(String(decoding: data, as: UTF8.self))")
        if let json = try JSONSerialization.jsonObject(with: data) as? [String: Any] {
          logcat_log("Encoded keys: \(json.keys.map(\.description).joined(separator: ", "))")
        } else {
          logcat_log("Failed to encode JSON")
        }
#endif

        // Parse the JSON response to extract temperature and units
        guard let json = try JSONSerialization.jsonObject(with: data) as? [String: Any],
              let current = json["current"] as? [String: Any],
              let current_units = json["current_units"] as? [String: Any],
              let current_temp = current["temperature_2m"] as? Double,
              let current_temp_units = current_units["temperature_2m"] as? String else { return }

        // Call the response closure with the temperature and units
        callback(Float(current_temp), current_temp_units)
      }
      catch {
#if os(Android)
        // Log coordinate parsing errors on Android
        logcat_log("Cannot parse current temperature: \(error)")
#else
        // Print coordinate parsing errors on other platforms
        print("Cannot parse current temperature: \(error)")
#endif
      }
    }
  }

  /// Fetches the current temperature for the specified city name.
  ///
  /// - Parameters:
  ///   - city: The name of the city to fetch the temperature for.
  ///   - response: A closure called with the temperature value and its units.
  ///
  /// This method first geocodes the city name to latitude and longitude using the
  /// OpenStreetMap Nominatim API, then fetches the current temperature for those coordinates.
  /// If the city cannot be found or the request fails, the closure is not called.
  func currentTemperature(city: String, _ callback: @escaping (Float, String) -> Void) async {
    // Construct the geocoding API URL for the city
    let url = URL(string: "https://nominatim.openstreetmap.org/search?city=\(city)&format=json")!
    
    // Perform the network request asynchronously
    // NOTE: set custom User-Agent header to avoid blocking by OSM service
    var headers = HTTPHeaders()
    headers.add(HTTPHeader(name: "User-Agent", value: "SCADE Weather Example"));
    AF.request(url, headers: headers).response { response in
      let data = response.data!

#if os(Android)
      // Log errors on Android
      logcat_log("currentTemperature: data received: \(String(decoding: data, as: UTF8.self))")
#endif

    do {
      // Parse the JSON response to extract latitude and longitude
      guard let json = try JSONSerialization.jsonObject(with: data) as? [Any],
            let cityData = json.first as? [String: Any] else { return }

      guard let lat = cityData["lat"] as? String,
            let lon = cityData["lon"] as? String else { return }

      // Convert latitude and longitude to Double and fetch temperature
      if let lat = Double(lat), let lon = Double(lon) {
        self.currentTemperature(latitude: lat, longitude: lon, callback)
      } else {
#if os(Android)
        // Log coordinate parsing errors on Android
        logcat_log("Cannot parse coordinates: \(lat), \(lon)")
#else
        // Print coordinate parsing errors on other platforms
        print("Cannot parse coordinates: \(lat), \(lon)")
#endif
      }
    } catch {
#if os(Android)
      // Log errors on Android
      logcat_log("Error: \(error.localizedDescription)")
#else
      // Print errors on other platforms
      print(error)
#endif
    }
    }
  }
}
