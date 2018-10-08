//
//  UI.swift
//  Chuck
//
//  Created by Sam Corder on 10/7/18.
//

import Foundation

import app

public class UI: Kotlinx_coroutines_core_nativeCoroutineDispatcher {
    override public func dispatch(context: KotlinCoroutineContext, block: Kotlinx_coroutines_core_nativeRunnable) {
        DispatchQueue.main.async {
            block.run()
        }
    }
}
