#include "ViewCamera.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/videoio.hpp"
#include "opencv2/core.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/objdetect.hpp"
#include <iostream>

using namespace cv;
using namespace std;

int ViewCamera::viewcamera() {
    VideoCapture cap(0);
    
    if(!cap.isOpened()) {
        cout << "Error opening video stream\n"
        "Port number may be invalid" << endl;
        return -1;
    }
    
    namedWindow("Cammy", 1);
    
    while(true) {
        
        Mat frame;
        
        cap.read(frame);   // Get new frame from camera
        
        // Update frame with detected face
        //frame = detect_face(frame);
        
        imshow("Cammy", frame);
        
        // Key press action to quit
        if(waitKey(1) == 'q')  // ASCII digit
            break;
    }
    return 0;
}

Mat detect_face(Mat frame) {
    
    // Load Face Cascade
    CascadeClassifier face_cascade;
    face_cascade.load("resources/haarcascades/haarcascade_frontalface_alt2.xml");
    
    // Detect Face
    std::vector<Rect> faces;
    face_cascade.detectMultiScale(frame, faces, 1.1, 2, 0 | CV_HAAR_SCALE_IMAGE, Size(30, 30));
    
    // Draw circle on detected face
    for(int i = 0; i < faces.size(); i++) {
        Point center(faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5);
        ellipse(frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar(255, 0 ,255), 4, 8, 0);
    }
    return frame;
}
