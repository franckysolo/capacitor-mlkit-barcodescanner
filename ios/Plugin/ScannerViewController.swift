//
//  ScannerViewController.swift
//  Plugin
//
//  Created by Chris Ta on 2021-02-09.
//  Copyright © 2021 Max Lynch. All rights reserved.
//

import UIKit
import AVFoundation
import MLKitVision
import MLKitBarcodeScanning
import Capacitor

@objc
class ScannerViewController: UIViewController, AVCaptureVideoDataOutputSampleBufferDelegate {

    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var cancelButton: UIBarButtonItem!
    
    let session = AVCaptureSession()
    lazy var format = BarcodeFormat.init(arrayLiteral: [.all])
    lazy var barcodeOptions = BarcodeScannerOptions(formats: format)
    var barcodeScanner: BarcodeScanner?
    
    var call: CAPPluginCall?
    var callback: ((CAPPluginCall?, String?) -> Void)?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupViews()
        
        startLiveVideo()
        self.barcodeScanner = BarcodeScanner.barcodeScanner(options: barcodeOptions)
    }
    
    private func setupViews() {
        view.backgroundColor = .white
        cancelButton.action = #selector(cancelTapped)
    }
    
    @objc private func cancelTapped() {
        self.session.stopRunning()
        self.callback?(self.call, nil)
        self.dismiss(animated: true, completion: nil)
    }
    
    func captureOutput(_ output: AVCaptureOutput,
                               didOutput sampleBuffer: CMSampleBuffer,
                               from connection: AVCaptureConnection) {
        
        if let barcodeScanner = self.barcodeScanner {
            
            let visionImage = VisionImage(buffer: sampleBuffer)
            
            barcodeScanner.process(visionImage) { (barcodes: [Barcode]?, error: Error?) in
                if let error = error {
                    print(error.localizedDescription)
                    return
                }
                
                for barcode in barcodes! {
                    print(barcode.rawValue!)
                    self.session.stopRunning()
                    AudioServicesPlayAlertSound(SystemSoundID(1200))
                    self.callback?(self.call, barcode.rawValue!)
                    self.dismiss(animated: true, completion: nil)
                    return
                }
            }
            
        }
    }
    
    private func startLiveVideo() {
        
        session.sessionPreset = AVCaptureSession.Preset.hd1920x1080
        let captureDevice = AVCaptureDevice.default(for: AVMediaType.video)
        
        let deviceInput = try! AVCaptureDeviceInput(device: captureDevice!)
        let deviceOutput = AVCaptureVideoDataOutput()
        deviceOutput.videoSettings = [kCVPixelBufferPixelFormatTypeKey as String: Int(kCVPixelFormatType_32BGRA)]
        deviceOutput.setSampleBufferDelegate(self, queue: DispatchQueue.global(qos: DispatchQoS.QoSClass.default))
        session.addInput(deviceInput)
        session.addOutput(deviceOutput)
        
        let imageLayer = AVCaptureVideoPreviewLayer(session: session)
        
        imageLayer.frame = CGRect(x: 0, y: 0, width: self.imageView.frame.size.width + 100, height: self.imageView.frame.size.height)
        imageLayer.videoGravity = .resizeAspectFill
        imageView.layer.addSublayer(imageLayer)
        
        session.startRunning()
    }
}
