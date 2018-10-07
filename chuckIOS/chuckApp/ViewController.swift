import UIKit
import app

class ViewController: UIViewController {
    let api = JokeApiClient(uiContext: UI() as KotlinCoroutineContext, baseURL: "http://api.icndb.com")

    override func viewDidLoad() {
        super.viewDidLoad()
        api.random(count: 1, exclusions: JokeExclusions.None()) {[weak self] (jokeMessage, error) -> KotlinUnit in
            if let error = error {
                self?.label.text = error.message
                return KotlinUnit()
            }
            guard let joke = jokeMessage?.jokes.first else {
                self?.label.text = "No Joke Found"
                return KotlinUnit()
            }
            self?.label.text = joke.text
            return KotlinUnit()
        }
        label.text = Proxy().proxyHello()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
}

public class UI: Kotlinx_coroutines_core_nativeCoroutineDispatcher {
    override public func dispatch(context: KotlinCoroutineContext, block: Kotlinx_coroutines_core_nativeRunnable) {
        DispatchQueue.main.async {
            block.run()
        }
    }
}
