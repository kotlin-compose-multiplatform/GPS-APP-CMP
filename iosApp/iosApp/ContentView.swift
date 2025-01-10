import UIKit
import SwiftUI
import composeApp
import GoogleMaps

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        GMSServices.provideAPIKey("AIzaSyAvoJgGXvptACEqzMv8aMlc2Wl7YPHfpNA")
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}
