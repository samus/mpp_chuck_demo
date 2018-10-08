import UIKit

import RxCocoa
import RxSwift

class JokeViewController: UIViewController {
    @IBOutlet weak var jokeLbl: UILabel!
    @IBOutlet weak var nextJokeBtn: UIButton!

    var viewModel: JokeViewModel? = nil

    let disposeBag = DisposeBag()

    override func viewDidLoad() {
        super.viewDidLoad()
        let inputs: JokeViewModelInputs = (nextJokeBtn.rx.tap)
        let vm = JokeViewModel(inputs: inputs)

        vm.jokeText.bind(to: jokeLbl.rx.text).disposed(by: disposeBag)
        viewModel = vm
        
        let dragInteraction = UIDragInteraction(delegate: self)
        jokeLbl.addInteraction(dragInteraction)
        jokeLbl.isUserInteractionEnabled = true

        nextJokeBtn.contentEdgeInsets = UIEdgeInsets(top: 12, left: 12, bottom: 12, right: 12)
    }

    override func viewDidLayoutSubviews() {
        nextJokeBtn.layer.cornerRadius = nextJokeBtn.frame.width / 2.0
    }
}

extension JokeViewController: UIDragInteractionDelegate {
    func dragInteraction(_ interaction: UIDragInteraction, itemsForBeginning session: UIDragSession) -> [UIDragItem] {
        guard let text = jokeLbl.text else { return [] }
        let provider = NSItemProvider(object: text as NSString)
        return [UIDragItem(itemProvider: provider)]
    }
}
