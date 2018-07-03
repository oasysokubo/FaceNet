//
//  ViewCamera.hpp
//  webcam

#ifndef ViewCamera_hpp
#define ViewCamera_hpp

#include "opencv2/opencv.hpp"

class ViewCamera {
private:
    cv::Mat detect_face(cv::Mat);
public:
    int viewcamera();
};

#endif /* ViewCamera_hpp */
