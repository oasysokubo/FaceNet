#include "ViewCamera.hpp"
#include "DetectFace.hpp"

#include "opencv2/imgproc.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/objdetect.hpp"

#include <iostream>

using namespace cv;
using namespace std;

/** Function Headers */
void detectAndDisplay(Mat frame);

/** Global Variables */
CascadeClassifier face_cascade;
CascadeClassifier eye_cascade;
String face_cascade_LOCAL = "";
String eye_cascade_LOCAL = "";
String window_name = "Cammy";


/**
 * @function    viewcamera
 */
int ViewCamera::viewcamera() {
    
    VideoCapture cap;
    Mat frame;
    
    // Load Cascades
    if(!face_cascade.load(face_cascade_LOCAL)) { cout << "Error loading face cascade." << endl; return -1; }
    if(!eye_cascade.load(eye_cascade_LOCAL)) { cout << "Error loading eye cascade." << endl; return -1; }
    
    // Read video stream from webcam
    cap.open(0);
    if(!cap.isOpened()) {
        cout << "Error opening video stream\n"
        "Port number may be invalid" << endl;
        return -1;
    }
    
    namedWindow(window_name, WINDOW_NORMAL);
    
    while(true) {
        
        cap.read(frame);   // Get new frame from camera
        if(frame.empty()) {
            break;
        }
        
        // Set frame to sufficient size
        resize(frame, frame, cv::Size(852, 480));
        
        // Open window to show frames
        imshow(window_name, frame);
        
        // Key press action to quit
        if(waitKey(1) == 'q' || waitKey(1) == 'Q')  // ASCII digit
            break;
    }
    return 0;
}

/**
 * @function    detectAndDisplay
 */
void detectAndDisplay(Mat frame) {
    
}

