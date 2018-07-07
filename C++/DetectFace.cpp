//
//  DetectFace.cpp
//  Face Detection
//
//  Created by Oasys Okubo on 7/6/18.
//  Copyright © 2018 Oasys Okubo. All rights reserved.
//

#include "DetectFace.hpp"

using namespace cv;
using namespace std;

Mat DetectFace::detect_face(Mat frame) {
    
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
