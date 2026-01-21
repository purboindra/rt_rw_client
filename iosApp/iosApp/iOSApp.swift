import SwiftUI
import sharedKit

@main
struct iOSApp: App {

    init(){
        initKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}