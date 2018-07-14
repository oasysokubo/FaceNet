#ifndef ViewCamera_hpp
#define ViewCamera_hpp

#include "opencv2/opencv.hpp"

class ViewCamera {
private:
    void detectAndDisplay(cv::Mat);
public:
    int viewcamera();
};

#endif /* ViewCamera_hpp */
