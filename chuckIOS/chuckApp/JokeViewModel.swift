//
//  JokeViewModel.swift
//  Chuck
//
//  Created by Sam Corder on 10/7/18.
//

import Foundation

import app
import RxCocoa
import RxSwift

typealias JokeViewModelInputs = (ControlEvent<Void>)

class JokeViewModel {
    let jokeText: Observable<String>

    init(inputs: JokeViewModelInputs) {
        let apiClient = JokeApiClient(uiContext: UI() as KotlinCoroutineContext, baseURL: "http://api.icndb.com")

        let jokes = inputs.startWith(()).flatMap { _ in
            return apiClient.random()
            }
            .share(replay: 1, scope: SubjectLifetimeScope.whileConnected)

        jokeText = jokes.map { $0.text }
    }
}
