import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(MlkitBarcodescanner)
public class MlkitBarcodescanner: CAPPlugin {

    @objc func scanBarcode(_ call: CAPPluginCall) {
        
        DispatchQueue.main.async {
            let vc = ScannerViewController(nibName: "ScannerViewController", bundle: nil)
            vc.modalPresentationStyle = .fullScreen
            vc.call = call
            vc.callback = self.scannerCallback(_:code:)
            
            self.bridge.presentVC(vc, animated: true, completion: nil)
            
        }
    }
    
    func scannerCallback(_ call: CAPPluginCall?, code: String?) {
        if let code = code {
            let vin = normalizeVin(code)
            call?.resolve(["value": vin])
        } else {
            call?.error("cancelled")
        }
    }
    
    private func normalizeVin(_ vin: String) -> String {
        var result = vin
        if result.hasPrefix("I") {
            result.removeFirst()
        }
        return result
    }
}
