//
//  JokeApiClient+rx.swift
//  Chuck
//
//  Created by Sam Corder on 10/7/18.
//

import Foundation

import app
import RxSwift

extension JokeApiClient {
    func random(count: Int32 = 1, exclusions: JokeExclusions = JokeExclusions.Explicit()) -> Observable<Joke> {
        return Observable.create {[weak self] (observer) -> Disposable in
            self?.random(count: count, exclusions: exclusions, completion: { (message, error) -> KotlinUnit in
                if let _ = error {
                    observer.onCompleted()
                    return KotlinUnit()
                }
                guard let joke = message?.jokes.first else {
                    observer.onCompleted()
                    return KotlinUnit()
                }
                observer.onNext(joke)
                observer.onCompleted()
                return KotlinUnit()
            })

            return Disposables.create()
        }
    }
}
