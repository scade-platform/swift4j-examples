import Foundation
import Alamofire
import Swift4j

@jvm
struct Post: Decodable {
    let userId: Int
    let id: Int
    let title: String
    let body: String
}

@jvm
class APIClient {
    func fetchPost(id: Int) async throws -> Post {
        let url = "https://jsonplaceholder.typicode.com/posts/\(id)"
        print("Starting request to \(url)")
        
        return try await withCheckedThrowingContinuation { continuation in
            AF.request(url)
                .validate()
                .responseDecodable(of: Post.self) { response in
                    print("Response received")
                    switch response.result {
                    case .success(let post):
                        continuation.resume(returning: post)
                    case .failure(let error):
                        continuation.resume(throwing: error)
                    }
                }
        }
    }
}
