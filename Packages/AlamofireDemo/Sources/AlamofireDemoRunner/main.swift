import Foundation
import AlamofireDemo

@main
struct Runner {
    static func main() async {
        let client = APIClient()

        let post = await client.fetchPostAsync(id: 1)

        print(post.getSummary())
        print("body:\n\(post.body)")
    }
}
