import Foundation
import Alamofire
import Swift4j

public struct Post: Sendable {
    public let userId: Int
    public let id: Int
    public let title: String
    public let body: String

    public func getSummary() -> String {
        "Post \(id) by user \(userId): \(title)"
    }
}

public final class APIClient {

    private let session: Session

    public init(timeout: TimeInterval = 15) {
        let config = URLSessionConfiguration.af.default
        config.timeoutIntervalForRequest = timeout
        config.timeoutIntervalForResource = timeout
        self.session = Session(configuration: config)
    }

    public func fetchPostAsync(id: Int) async -> Post {
        await withCheckedContinuation { cont in
            let url = "https://dummyjson.com/posts/\(id)"
            print("Starting request to \(url)")

            session.request(url)
                .validate()
                .responseDecodable(of: PostDTO.self,
                                   queue: .global(qos: .userInitiated)) { res in
                    let post: Post
                    switch res.result {
                    case .success(let dto):
                        post = Post(userId: dto.userId, id: dto.id, title: dto.title, body: dto.body)
                    case .failure(let error):
                        post = Post(userId: 0, id: id, title: "Error", body: "Failed: \(error.localizedDescription)")
                    }

                    DispatchQueue.main.async {
                        cont.resume(returning: post)
                    }
                }
        }
    }
}

private struct PostDTO: Decodable {
    let id: Int
    let title: String
    let body: String
    let userId: Int
}
